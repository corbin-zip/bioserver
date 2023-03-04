/*
    BioServer2 -Emulation of the long gone server for 
                Biohazard Outbreak File #2 (Playstation 2)

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

import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * Process messages
 */
class GameServerPacketHandler implements Runnable {
    // database for sessionhandling
    private Database db;
    
    // the list with packets :-)
    private final List queue = new LinkedList();

    // list of connected clients
    private final ClientList clients = new ClientList();
    
    // server packet id if needed
    private int packetidcounter;
        
    @Override
    public void run() {
        GameServerDataEvent dataEvent;
    
        // get configuration
        Configuration conf = new Configuration();
        
        this.db = new Database(conf.db_user, conf.db_password);
        packetidcounter = 0;
       
        while(true) {
        // Wait for data to become available
        synchronized(queue) {
            while(queue.isEmpty()) {
                try {
                    queue.wait();
                } catch (InterruptedException e) { }
            }
            dataEvent = (GameServerDataEvent) queue.remove(0);
        }

        // send the data
        dataEvent.server.send(dataEvent.socket, dataEvent.data);
        }
    }
    
    public int countInGamePlayers () {
        return clients.getList().size();
    }
    
    // simple getter to let the serverthread manage disconnected clients
    public ClientList getClients() {
        return clients;
    }
    
    // increase the server packet id
    public int getNextPacketID() {
        return(++packetidcounter);
    }
    
    // here comes the raw packet data and is tranformed into messages
    void processData(GameServerThread server, SocketChannel socket, byte[] data, int count) {
        if(count > 0) {
            switch(data[0]) {
                case (byte)0x82:
                    if(data[1] == 0x02) {
                        // some session checking etc.
                        Packet p = new Packet(data);
                        if(p.getCmd() == Commands.GSLOGIN) {
                            // check this session and if ok create client
                            if(!check_session(server, socket, p)) {
                                Logging.println("Session check gameserver failed!");
                            }
                        }
                    }
                    break;

                default:
                    // broadcast to connected clients in gamesession but not the sender
                    byte[] acopy = new byte[count];
                    System.arraycopy(data, 0, acopy, 0, count);

                    Client cl = clients.findClient(socket);
                    cl.connalive = true;
                    int gamenum = cl.gamenumber;
                    List cls = clients.getList();
                    synchronized(queue) {
                        for(int i=0; i<cls.size(); i++) {
                            cl = (Client) cls.get(i);
                            if(cl.gamenumber==gamenum && cl.getSocket()!=socket) {
                                queue.add(new GameServerDataEvent(server, cl.getSocket(), acopy));
                            }
                        }
                        queue.notify();
                    }
                }
        }
    }

    // add a packet to the queue
    void addOutPacket(GameServerThread server, SocketChannel socket, Packet packet) {
        synchronized(queue) {
            queue.add(new GameServerDataEvent(server, socket, packet.getPacketData()));
            queue.notify();
        }
    }

    // broadcast a packet to all connected clients
    void broadcastPacket(GameServerThread server, Packet packet) {
        List cls = clients.getList();
        synchronized(queue) {
            for(int i=0; i<cls.size(); i++) {
                Client cl = (Client) cls.get(i);
                queue.add(new GameServerDataEvent(server, cl.getSocket(), packet.getPacketData()));
            }
            queue.notify();
        }
    }

    public void GSsendLogin(GameServerThread server, SocketChannel socket) {
        Packet p = new Packet(Commands.GSLOGIN, Commands.QUERY, Commands.GAMESERVER, this.getNextPacketID());
        this.addOutPacket(server, socket, p);
    }

    // check the login for gameserver!
    private boolean check_session(GameServerThread server, SocketChannel socket, Packet p) {
        int seed  = p.getPacketID();
        int sessA =  ((int) p.getPayload()[0] - 0x30)*10000
                    +((int) p.getPayload()[1] - 0x30)*1000
                    +((int) p.getPayload()[2] - 0x30)*100
                    +((int) p.getPayload()[3] - 0x30)*10
                    +((int) p.getPayload()[4] - 0x30);
        int sessB =  ((int) p.getPayload()[5] - 0x30)*10000
                    +((int) p.getPayload()[6] - 0x30)*1000
                    +((int) p.getPayload()[7] - 0x30)*100
                    +((int) p.getPayload()[8] - 0x30)*10
                    +((int) p.getPayload()[9] - 0x30);
        String session = String.format("%04d%04d", sessA-seed, sessB-seed);

        String userid = db.getUserid(session);
        
        Client cl;
        
        // session check is OK, a user with this session is in database
        if(!"".equals(userid)) {
            // kill old connections of this client
            cl = clients.findClient(userid);
            if(cl != null) {
                List cls = clients.getList();
                for(int i=0; i<cls.size(); i++) {
                    cl = (Client) cls.get(i);
                    if(cl.getUserID().equals(userid)) {
                        Logging.println("GS: Disconnect double session for userid "+userid);
                        server.disconnect(cl.getSocket());
                        this.removeClient(server, cl);
                    }
                }
            }

            // setup client object for this user/session
            int gamenr = db.getGameNumber(userid);
            clients.add((new Client(socket, userid, session)));
            cl = clients.findClient(socket);
            cl.gamenumber = gamenr;

            // set this user to ingame status
            db.updateClientOrigin(userid, PacketHandler.STATUS_GAME, 0, 0, 0);
            return(true);
        } else {
            // the session check failed, disconnect this client
            Logging.println("GS: Disconnect invalid session "+session+" for userid "+userid);
            server.disconnect(socket);
            return(false);
        }        
    }

    // remove a client by userid
    public void removeClient(GameServerThread server, String userid) {
        Client cl = clients.findClient(userid);
        if(cl == null) {
            Logging.println("GS: kicking of " + userid + " failed, missing connection");
        }
        else {
            this.removeClient(server, cl);
            Logging.println("GS: " + userid + " kicked");
        }
    }
    
    public void removeClientNoDisconnect(GameServerThread server, SocketChannel socket) {
        Client cl = clients.findClient(socket);
        if(cl == null) return;

        // set user to offline status in database
        db.updateClientOrigin(cl.getUserID(), PacketHandler.STATUS_OFFLINE, -1, 0, 0);
        clients.remove(cl);     
    }

    // when a host or client leaves unexpected, we have to handle this
    public void removeClient(GameServerThread server, Client cl) {
        if(cl == null) return;

        SocketChannel socket = cl.getSocket();
        // set user to offline status in database
        db.updateClientOrigin(cl.getUserID(), PacketHandler.STATUS_OFFLINE, -1, 0, 0);
        clients.remove(cl);     
        
        // close connection from server side
        server.disconnect(socket);
    }

    // check for frozen players and remove their connection after one minute
    public void connCheck(GameServerThread server) {
        List cls = clients.getList();
        for(int i=0; i<cls.size(); i++) {
            Client cl = (Client) cls.get(i);
            // check if connection is alive and initiate next check
            // the connection flag is set to true by every incoming new packet
            if(cl.connalive) {
                cl.connalive = false;
            } else {
                // this client left us :-(
                Logging.println("GS connCheck: trying to remove " + cl.getUserID());
                this.removeClient(server, cl);
            }
        }
    }
    
}
