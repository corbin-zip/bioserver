/*
    BioServer - Emulation of the long gone server for 
                Biohazard Outbreak File #1 (Playstation 2)

    Copyright (C) 2013-2019 obsrv.org (no23@deathless.net)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package bioserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * this thread handles connections and traffic queues
 */
class ServerThread implements Runnable {

    // host:port combination to listen
    private InetAddress hostAddress;
    private int port;
    
    // selector to monitor
    private Selector selector;

    // The channel on which we'll accept connections
    private ServerSocketChannel serverChannel;
    
    // thread of the packethandler
    private PacketHandler packethandler;
    
    // A list of ChangeRequest instances
    private List changeRequests = new LinkedList();

    // Maps a SocketChannel to a list of ByteBuffer instances
    private Map pendingData = new HashMap();

    // The buffer into which we'll read data when it's available
    private ServerStreamBuffer readBuffer;
    
    // Maps a SocketChannel to a ServerStreamBuffer (allows messaging)
    private Map readbuffers = new HashMap();

    // was initialisation ok ?
    private boolean initOK;
    
    // function to initialise the selector
    private Selector initSelector() throws IOException {
        // Create a new selector
        Selector socketSelector = SelectorProvider.provider().openSelector();

        // Create a new non-blocking server socket channel
        this.serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        // Bind the server socket to the specified address and port
        InetSocketAddress isa = new InetSocketAddress(this.hostAddress, this.port);
        serverChannel.socket().bind(isa);

        // Register the server socket channel, indicating an interest in 
        // accepting new connections
        serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

        return socketSelector;
      }
    
    public ServerThread(InetAddress hostAddress, int port, PacketHandler packethandler) {
        this.initOK = true;
        try {
            this.hostAddress = hostAddress;
            this.port = port;
            this.selector = this.initSelector();
            this.packethandler = packethandler;
        } catch (IOException ex) {
            Logging.println("Lobbyserver constructor exception caught!");
            this.initOK = false;
        }
    }
    
    // iteration of selector, deal accept, read and write
    @Override
    public void run() {
        SelectionKey key;
        
        while (this.initOK) {
          try {
              // Process any pending changes
              synchronized(this.changeRequests) {
                  Iterator changes = this.changeRequests.iterator();
                  while (changes.hasNext()) {
                      ServerChangeEvent change = (ServerChangeEvent) changes.next();
                      key = change.socket.keyFor(this.selector);
                      if(key != null) {
                        if(key.isValid()) {     // drop pending request for closed/cancelled channel
                          switch(change.type) {
                              case ServerChangeEvent.CHANGEOPS:
                                  key.interestOps(change.ops);
                                  break;

                              case ServerChangeEvent.FORCECLOSE:
                                  this.close(key);
                                  break;

                              default:

                          }
                        }
                      }
                  }
                  this.changeRequests.clear();
              }

              // Wait for an event on the registered channels
              this.selector.select();

              // Iterate over the set of keys for which events are available
              Iterator selectedKeys = this.selector.selectedKeys().iterator();
              while (selectedKeys.hasNext()) {
                  key = (SelectionKey) selectedKeys.next();
                  selectedKeys.remove();

                  if (!key.isValid()) {
                      continue;
                  }

                  // Check what event is available and deal with it
                  if (key.isValid() && key.isAcceptable()) {
                      this.accept(key);
                  } else if (key.isValid() && key.isReadable()) {
                      this.read(key);
                  } else if (key.isValid() && key.isWritable()) {
                      this.write(key);
                  }         
              }
          } catch (Exception e) {
              Logging.println("Lobbyserver iteration exception caught!");
              e.printStackTrace();
          }
        }
  } 
  
    // closing a connection
    private synchronized void close(SelectionKey key) throws IOException {
        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            Logging.println("Lobbyserver closes connection to :" + socketChannel.getRemoteAddress());
            key.channel().close();
            key.cancel();
            
            // cleanup
            this.readbuffers.remove(socketChannel);
            this.pendingData.remove(socketChannel);
            this.packethandler.removeClientNoDisconnect(this, socketChannel);
        } catch (Exception e){
            // nothing ...
        }
    }
    
    // accepting a new connection
    private void accept(SelectionKey key) throws IOException {
        // For an accept to be pending the channel must be a server socket channel.
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

        // Accept the connection and make it non-blocking
        SocketChannel socketChannel = serverSocketChannel.accept();
        Socket socket = socketChannel.socket();
        socketChannel.configureBlocking(false);
        
        // Register the new SocketChannel with our Selector, indicating
        // we'd like to be notified when there's data waiting to be read
        socketChannel.register(this.selector, SelectionKey.OP_READ);

        // and we need a readbuffer for this channel
        if(readbuffers.get(socketChannel) == null) {
            readbuffers.put(socketChannel, new ServerStreamBuffer());
        }
        
        // send the first packet to initiate the protocol
        this.packethandler.sendLogin(this, socketChannel);
    } 
   
    // read from socket
    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        int numRead;
        
        // get the buffer for this channel
        this.readBuffer = (ServerStreamBuffer) readbuffers.get(socketChannel);

        try {
            // read / append to buffer
            numRead = socketChannel.read(this.readBuffer.buf);
        } catch (IOException e) {
            // The remote forcibly closed the connection, cancel
            // the selection key and close the channel.
            // also remove this client list
            this.close(key);
            return;
        }

        if (numRead == -1) {
            // Remote entity shut the socket down cleanly. Do the
            // same from our end and cancel the channel.
            this.close(key);
            return;
        }
    
        // Hand the data off to our worker thread
        byte[] data = this.readBuffer.getCompleteMessages();
        if(data != null) this.packethandler.processData(this, socketChannel, data, data.length);
    } 

  
    // write to socket
    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        synchronized (this.pendingData) {
            List queue = (List) this.pendingData.get(socketChannel);

            // Write until there's no more data ...
            while (!queue.isEmpty()) {
                ByteBuffer buf = (ByteBuffer) queue.get(0);
                try {
                    socketChannel.write(buf);
                } catch (Exception e) {
                    // something's wrong on writing, e.g. timeout
                    queue.clear();
                    this.close(key);
                    return;
                }
                if (buf.remaining() > 0) {
                    // ... or the socket's buffer fills up
                    queue.clear();
                    this.close(key);
                    return;
                }
                queue.remove(0);
              }

              if (queue.isEmpty()) {
                  // We wrote away all data, so we're no longer interested
                  // in writing on this socket. Switch back to waiting for
                  // data.
                  key.interestOps(SelectionKey.OP_READ);
            }
        }
    } 
     
    // send to a connection
    public void send(SocketChannel socket, byte[] data) {
        synchronized (this.changeRequests) {
            // Indicate we want the interest ops set changed
            this.changeRequests.add(new ServerChangeEvent(socket, ServerChangeEvent.CHANGEOPS, SelectionKey.OP_WRITE));

            // And queue the data we want written
            synchronized (this.pendingData) {
                List queue = (List) this.pendingData.get(socket);
                if (queue == null) {
                    queue = new ArrayList();
                    this.pendingData.put(socket, queue);
                }
                queue.add(ByteBuffer.wrap(data));
            }
        }
         // Finally, wake up our selecting thread so it can make the required changes
        this.selector.wakeup();
    } 
    
    // close a connection server sided
    public void disconnect(SocketChannel socket) {
        synchronized (this.changeRequests) {
            this.changeRequests.add(new ServerChangeEvent(socket, ServerChangeEvent.FORCECLOSE, 0));
        }
        this.selector.wakeup();
    } 
  
}
