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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Handle / Nickname pair
 */
public class HNPair {
    private byte[] handle;
    private byte[] nickname;
    
    // constructor for given handle and nickname Byte
    public HNPair(byte[] handle, byte[] nickname) {
        this.handle = handle;
        this.nickname = nickname;
    }
    
    // constructor for given handle and nickname array
    public HNPair(String handle, String nickname) {
        this.handle = handle.getBytes();
        try {
            this.nickname = nickname.getBytes("SJIS");
        } catch (UnsupportedEncodingException ex) {
            this.nickname = "sjis".getBytes();
        }
    }

    // create a random handle
    public void createHandle(Database db) {
        byte[] d = ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").getBytes();
        boolean flag = false;
        while(!flag) {
            for(int i=0; i<6; i++) {
                Double dd = 36*Math.random();
                this.handle[i] = d[dd.intValue()];
            }
            flag = db.checkHandle(handle.toString());
        }
    }
    
    public byte[] getHandle() {
        return handle;
    }
    
    public byte[] getNickname() {
        return nickname;
    }
    
    // create an array with the HNpair
    public byte[] getHNPair() {
        byte[] hnpair = new byte[handle.length + nickname.length + 4];
        
        hnpair[0] = 0;
        hnpair[1] = 6;
        System.arraycopy(handle, 0, hnpair, 2, 6);
        hnpair[8] = 0;
        hnpair[9] = (byte) nickname.length;
        System.arraycopy(nickname, 0, hnpair, 10, nickname.length);
        
        return hnpair;
    }
    
}
