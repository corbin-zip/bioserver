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

/**
 * special class for multiple HNPairs
 */
public class HNPairs {
    // byte number of ids (max 3)
    // int length id (6)
    // string id
    // int length 
    // string handle (16)
    // word end marker

    // for simplicity we create a maxmimum buffer for 3 handles
    private byte[] hnpairs = new byte[85];
    private int count;
    private int length;
    
    public HNPairs() {
        count = 0;
        length = 1;
    }
    
    public byte[] getArray() {
        hnpairs[0] = (byte) count;  // set count

        byte[] retval = new byte[length];
        System.arraycopy(this.hnpairs, 0, retval, 0, length);
        return retval;
    }
    
    // add a pair to buffer
    public void add(HNPair hnpair) {
        byte[] hn = hnpair.getHNPair();
        
        System.arraycopy(hn, 0, hnpairs, length, hn.length);
        length = length+hn.length;
        hnpairs[length++] = 0;
        hnpairs[length++] = 0;  // add end marker

        count++;
    }
    
}