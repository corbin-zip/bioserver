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

/**
 *  object for representation of strings in packetdata
 */
public class PacketString {
    private byte[] buffer;
    
    // construct it from a string
    public PacketString(String string) {
        this.buffer = string.getBytes();
    }
    
    // convert to data used in packets
    public byte[] getData() {
        ByteBuffer zwi = ByteBuffer.wrap(new byte[buffer.length + 2]);
        zwi.putShort((short) buffer.length);
        zwi.put(buffer);
        return zwi.array();
    }
}
