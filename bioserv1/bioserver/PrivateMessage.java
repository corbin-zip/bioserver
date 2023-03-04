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

/**
 * Object for the private messaging system
 */
public class PrivateMessage {
    private byte[] senderhandle;
    private byte[] sendername;
    private byte[] recipient;
    private byte[] message;
    
    public PrivateMessage(byte[] senderhandle, byte[] sendername, byte[] recipient, byte[] message) {
        this.senderhandle = senderhandle;
        this.sendername = sendername;
        this.recipient = recipient;
        this.message = message;
    }
    
    public byte[] getRecipient() {
        return this.recipient;
    }
    
    public byte[] getSenderHandle() {
        return this.senderhandle;
    }
    
    public byte[] getMessage() {
        return this.message;
    }
    
    // create a packet for broadcast
    public byte[] getPacketData() {
        ByteBuffer z = ByteBuffer.wrap(new byte[200]);
        
        // handle and name of sender
        z.putShort((short) senderhandle.length);
        z.put(senderhandle);
        z.putShort((short) sendername.length);
        z.put(sendername);
        
        // message
        z.putShort((short) message.length);
        z.put(message);
        
        byte[] retval = new byte[z.position()];
        z.rewind();
        z.get(retval);
        return retval;
    }
}
