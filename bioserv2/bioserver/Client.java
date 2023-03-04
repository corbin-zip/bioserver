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

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * information about the clients
 */
public class Client {
    private SocketChannel socket;
    private String userid;
    private String session;
    private HNPair hnpair;      // chosen handle / nickname
    private byte[] characterstats;  // 0xd0 in len

    private int area;           // special case 51 = post-game lobby
    private int room;
    private int slot;
    public int gamenumber;
    private byte player;        // number of this player (1-4)
    
    public boolean connalive;   // set back every 60 secs or be disconnected ;-)

    private byte host;          // this is the host of a gameslot
    
    public Client(SocketChannel socket, String userid, String session) {
        this.socket = socket;
        this.userid = userid;
        this.session = session;
        this.area = 0;      // in no area (area selection screen)
        this.room = 0;      // in no room
        this.slot = 0;      // in no slot
        this.host = 0;
        this.connalive = true;  // begin with active connection
    }
    
    public SocketChannel getSocket() {
        return socket;
    }
    
    public String getUserID() {
        return userid;
    }

    public HNPair getHNPair() {
        return hnpair;
    }
    
    public void setHNPair(HNPair hnpair) {
        this.hnpair = hnpair;
    }
    
    public void setCharacterStats(byte[] charstats) {
        this.characterstats = charstats;
    }
    
    public void setArea(int number) {
        this.area = number;
    }
    
    public int getArea() {
        return this.area;
    }
    
    public void setRoom(int number) {
        this.room = number;
    }
    
    public int getRoom() {
        return this.room;
    }

    public void setSlot(int number) {
        this.slot = number;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public byte[] getCharacterStats() {
        return this.characterstats;
    }

    public byte getHostFlag() {
        return this.host;
    }
    
    public void setHostFlag(byte number) {
        this.host = number;
    }

    public void setPlayerNum(byte number) {
        this.player = number;
    }

    public byte getPlayerNum() {
        return this.player;
    }
    
    public byte[] getPreGameStat(byte playernum) {
        ByteBuffer z = ByteBuffer.wrap(new byte[300]);
        
        z.put(playernum);
        z.put((byte) 1);
        z.put(this.getHNPair().getHNPair());
        z.putShort((short) this.characterstats.length);
        z.put(this.characterstats);
        z.put((byte) 0);
        z.put((byte) 0);
        z.put((byte) 6);
        
        byte[] retval = new byte[z.position()];
        z.rewind();
        z.get(retval);
        return retval;
    }
    
   public byte[] getCharacterStat() {
        ByteBuffer z = ByteBuffer.wrap(new byte[300]);
        
        z.put(this.getHNPair().getHNPair());
        z.putShort((short) this.characterstats.length);
        z.put(this.characterstats);
        
        byte[] retval = new byte[z.position()];
        z.rewind();
        z.get(retval);
        return retval;
    }

}
