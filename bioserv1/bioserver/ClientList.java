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

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * List of connected clients
 */
public class ClientList {
    private List clients;
    private Client client;
    
    public ClientList() {
        clients = new LinkedList();
    }
    
    public List getList() {
        return clients;
    }
    
    public void add(Client client) {
        clients.add(client);
    }
    
    // find a client by its sockets
    public Client findClient(SocketChannel socket) {
        for(int i=0; i< clients.size(); i++) {
            client = (Client) clients.get(i);
            if(client.getSocket() == socket) {
                return(client);
            }
        }
        return null;
    }
    
    // find a client by userid
    public Client findClient(String userid) {
        for(int i=0; i<clients.size(); i++) {
            client = (Client) clients.get(i);
            if(client.getUserID().equals(userid)) return client;
        }
        return null;
    }
    
    // find a client by its handle
    public Client findClientByHandle(byte[] handle) {
        byte[] clhand;
        if(handle == null) return null;
        for(int i=0; i<clients.size(); i++) {
            client = (Client) clients.get(i);
            try {
                clhand = client.getHNPair().getHandle();
            } catch (Exception e) {
                //TODO: for the moment we ignore the nullpointer execeptions, but we have to handle clientlist different!
                clhand = null;
            } 
            if(clhand != null)
                if(Arrays.equals(handle, clhand)) return client;
        }
        return null;
    }
    
    // find client with certain playernumber in same gameslot like client
    public Client findClient(int area, int room, int slot, byte playernum) {    
        for(int i=0; i<clients.size(); i++) {
            client = (Client) clients.get(i);
            if(client.getArea()==area && 
               client.getRoom()==room && 
               client.getSlot()==slot && 
               client.getPlayerNum() == playernum) { return(client); }
        }
        return null;
    }

    public byte getFreePlayerNum(int area, int room, int slot) {
        byte[] fpn = {0,0,0,0,0};
        for(int i=0; i<clients.size(); i++) {
            client = (Client) clients.get(i);
            if(client.getArea()==area && client.getRoom()==room && client.getSlot()==slot) {
                fpn[client.getPlayerNum()] = 1;
            }
        }
        for(int i=2; i<5; i++) if(fpn[i] == 0) return((byte) i);
        return(0);
    }
    
    // find a client by its socket and remove it
    public void remove(SocketChannel socket) {
        for(int i=0; i< clients.size(); i++) {
            client = (Client) clients.get(i);
            if(client.getSocket() == socket) {
                clients.remove(i);
                continue;
            }
        }
    }

    // remove a client
    public void remove(Client cl) {
        for(int i=0; i< clients.size(); i++) {
            client = (Client) clients.get(i);
            if(client == cl) {
                clients.remove(i);
                continue;
            }
        }
    }
    
    // find the host of a slot if there's any
    public Client getHostofSlot(int area, int room, int slot) {
        Client retval = null;
        for(int i=0; i<clients.size(); i++) {
            client = (Client) clients.get(i);
            if(client.getArea()==area && client.getRoom()==room && client.getSlot()==slot && client.getHostFlag()==1) {
                return client;
            }
        }
        return retval;
    }
    
    public byte[] getPlayerStats(int area, int room, int slotnr) {
        ByteBuffer retval = ByteBuffer.wrap(new byte[1024]);
        
        byte playercnt = (byte) (this.countPlayersInSlot(area, room, slotnr) & 0xff);
        retval.putShort((short) slotnr);
        retval.put((byte) 3);   // ???
        retval.put(playercnt);
        
        for(int i = 0; i<clients.size(); i++) {
            client = (Client) clients.get(i);
            if(client.getArea()==area && client.getRoom()==room && client.getSlot() == slotnr) {
                retval.put(client.getHNPair().getHNPair());
                retval.putShort((short)client.getCharacterStats().length);
                retval.put(client.getCharacterStats());
            }
        }

        byte[] r = new byte[retval.position()];
        retval.rewind();
        retval.get(r);
        return r;
    }
    
    // count players in a specific game
    public int countPlayersInGame(int gamenr) {
        int retval = 0;
        for(int i = 0; i<clients.size(); i++) {
            client = (Client) clients.get(i);
            if(client.gamenumber == gamenr) retval++;
        }
        return retval;
    }

    // Count players in given slot
    public int countPlayersInSlot(int area, int room, int slotnr) {
        int retval = 0;
        for(int i = 0; i<clients.size(); i++) {
            client = (Client) clients.get(i);
            if(client.getArea() == area && client.getRoom() == room && client.getSlot() == slotnr) retval++;
        }
        return retval;
    }
    
    // Count players in given room
    public int countPlayersInRoom(int area, int room) {
        int retval = 0;
        for(int i = 0; i<clients.size(); i++) {
            client = (Client) clients.get(i);
            if(client.getArea() == area && client.getRoom() == room) retval++;
        }
        return retval;
    }

    // count players in a given meeting room
    public byte getPlayerCountAgl(int nr) {
        byte retval = 0;
        for(int i=0; i<clients.size(); i++) 
            if(((Client)clients.get(i)).gamenumber == nr)
                retval++;
        return retval;        
    }
    
    // count the players in a given area lobby
    public int[] countPlayersInArea(int nr) {
        // TODO: find out the unknown 3rd value. is it ingame ?
        int retval[] = {0,0,0};     // arealobby, arearoom, ingame?
        for(int i=0; i<clients.size(); i++) {
            // right area
            if(((Client)clients.get(i)).getArea() == nr) {
                // in lobby or in room ?
                if(((Client)clients.get(i)).getRoom() == 0) {
                    retval[0]++;
                }
                else {
                    retval[1]++;
                }
            } else {
                if (((Client)clients.get(i)).getArea() == 51) {
                    retval[2]++;
                }
            }
        }
        return retval;
    }
    
    public byte getClientStatus(byte[] handle) {
        Client cl = this.findClientByHandle(handle);
        
        // offline or playing
        if(cl == null) return 0;
        
        // ingame
        if(cl.gamenumber != 0) return 3;
        
        // assume online
        return 1;
    }
    
    
}
