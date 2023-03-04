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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Send a PING packet to all connected clients every 30 secs
 */
public class HeartBeatThread implements Runnable {
    PacketHandler packethandler;
    GameServerPacketHandler gspackethandler;
    ServerThread server;
    GameServerThread gsserver;    
    int counter, counter2;
    
    public HeartBeatThread(ServerThread server, PacketHandler packethandler, 
                           GameServerThread gsserver, GameServerPacketHandler packethandler2) {
        this.packethandler = packethandler;
        this.gspackethandler = packethandler2;
        this.server = server;
        this.gsserver = gsserver;
    }
    
    @Override
    public void run() {
        counter = 0;
        counter2 = 0;
        
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(HeartBeatThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // loop forever
        while(true) {
            try {
                // servers send every 30 secs that he is alive, no answer needed
                packethandler.broadcastPing(server);
                
                // remove dead clients in gameserver
                gspackethandler.connCheck(gsserver);
                
                // also check if any slots have to be autostarted
                packethandler.checkAutoStart(server);

                // server asks for client every 60 secs
                // client will be deleted from list and disconnected if not answered
                if(counter == 1) {
                    packethandler.broadcastConnCheck(server);
                    counter = 0;
                } else {
                    counter++;
                }
                
                // clean up the rooms every 5 minutes
                if(counter2 == 9) {
                    packethandler.cleanGhostRooms(server);
                    counter2 = 0;
                } else {
                    counter2++;
                }

                // sleep for 30 secs
                Thread.sleep(30*1000);
            } catch (InterruptedException ex) {
                Logging.println("Heartbeat exception caught!");
            }
        }
    }
}
