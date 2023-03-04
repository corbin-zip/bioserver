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

public class Packet {
    final static int HEADERSIZE = 12;

    private byte    who;        // server or client packet
    private byte    qsw;        // question or answer
    private int     cmd;        // command
    private int     len;        // length including header
    private int     pid;        // packet ID
    private byte    err;        // error occured
    private byte[]  pay;        // the payload
    
    // constructor with payload
    public Packet(int command, byte questionanswer, byte whosends, int packetid, byte[] payload) {
        cmd = command;
        who = whosends;
        qsw = questionanswer;
        len = payload.length;
        pid = packetid;
        pay = payload;
        err = 0;
    }
    
    // constructor without payload
    public Packet(int command, byte questionanswer, byte whosends, int packetid) {
        cmd = command;
        who = whosends;
        qsw = questionanswer;
        len = 0;
        pay = null;
        pid = packetid;
        err = 0;
    }

    // constructor for raw data
    public Packet(byte[] packetdata) {
        who = (byte) packetdata[0];
        qsw = (byte) packetdata[1];
        cmd = (((int) packetdata[2] << 8)&0xFF00) | ((int) packetdata[3] &0xFF);
        len = (((int) packetdata[4] << 8)&0xFF00) | ((int) packetdata[5] &0xFF);
        pid = (((int) packetdata[6] << 8)&0xFF00) | ((int) packetdata[7] &0xFF);
        err = (byte) packetdata[8];
        pay = new byte[len];
        if((len + HEADERSIZE) > packetdata.length) {
            System.out.println("ERROR: packet constructor!!\n");
            Logging.printBuffer(packetdata);
        }
        System.arraycopy(packetdata, HEADERSIZE, pay, 0, len);       
    }
    
    // construct the raw packet
    public byte[] getPacketData() {
        byte[] result = new byte[len + HEADERSIZE];
        
        // build the header
        result[0]  = (byte) who;
        result[1]  = (byte) qsw;
        result[2]  = (byte) ((cmd >> 8) & 0xff);
        result[3]  = (byte) (cmd & 0xff);
        result[4]  = (byte) ((len >> 8) & 0xff);
        result[5]  = (byte) (len & 0xff);
        result[6]  = (byte) ((pid >> 8) & 0xff);
        result[7]  = (byte) (pid & 0xff);
        result[8]  = (byte) err;
        result[9]  = (byte) 0xff;
        result[10] = (byte) 0xff;
        result[11] = (byte) 0xff;

        // copy the payload
        if(len >0) System.arraycopy(pay, 0, result, HEADERSIZE, pay.length);

        return result;
    }

    public int getLength() {
        return len;
    }
    
    public int getCmd() {
        return cmd;
    }
    
    public byte getqsw() {
        return qsw;
    }

    public byte getwho() {
        return who;
    }
    
    public int getPacketID() {
        return pid;
    }
    
    public byte[] getPayload() {
        return pay;
    }
    
    public void setPacketID(int packetid) {
        this.pid = packetid;
    }
    
    // set the error flag for a packet !
    public void setErr() {
        this.err = (byte) 0xff;
    }
    
    // This is not complete. Somehow fixVals and masks have to be initialised
    // Maybe it's the 0x2837 sent by the server ?
    private byte calc_shift(byte i, byte p) {
        byte[] fixval = {  21,   23,   10,   17,   23,   19,    6,   13};
        byte[] masks  = {0x33, 0x30, 0x3c, 0x34, 0x2d, 0x30, 0x3c, 0x34};
        return(byte) (fixval[i&7] - (i&(byte)0xf8) - p + ((p - 9 + i)&masks[i&7])*2);
    }
    
    // encrypt/decrypt the payload
    // 2 bytes len, 2 bytes sum, rest string
    public void cryptString() {
        int length = (((int)pay[0] << 8)|((int)pay[1])) -2; // skip the sum
        for(int i=0; i<length; i++) pay[4+i] = (byte) (pay[4+i] ^ calc_shift((byte)i, (byte)(pid & (byte)0xff)));
    }
    
    // return a byte array with decrypted string
    public byte[] getDecryptedString() {
        int length = (((int)pay[0] << 8)|((int)pay[1])) -2; // skip the sum
        for(int i=0; i<length; i++) pay[4+i] = (byte) (pay[4+i] ^ calc_shift((byte)i, (byte)(pid & (byte)0xff)));
        
        byte[] retval = new byte[length];
        System.arraycopy(pay, 4, retval, 0, length);
        return retval;        
    }
    
    // decrypt the slotpassword
    public byte[] getPassword() {
        int length = (((int)pay[2] << 8)|((int)pay[3])) -2; // skip the sum
        for(int i=0; i<length; i++) pay[6+i] = (byte) (pay[6+i] ^ calc_shift((byte)i, (byte)(pid & (byte)0xff)));
        
        byte[] retval = new byte[length];
        System.arraycopy(pay, 6, retval, 0, length);
        return retval;                
    }
    
    // decrypt chosen handle/nickname
    public HNPair getDecryptedHNpair() {
        int hlen = (((int)pay[0] << 8)|((int)pay[1])) -2; // skip the sum
        int nlen = (((int)pay[hlen +4] << 8)|((int)pay[hlen +5])) -2; // skip the sum

        for(int i=0; i<hlen; i++) pay[4+i] = (byte) (pay[4+i] ^ calc_shift((byte)i, (byte)(pid & (byte)0xff)));
        for(int i=0; i<nlen; i++) pay[hlen+8+i] = (byte) (pay[hlen+8+i] ^ calc_shift((byte)i, (byte)(pid & (byte)0xff)));

        byte[] handle = new byte[hlen];
        byte[] nickname = new byte[nlen];
        System.arraycopy(pay, 4, handle, 0, hlen);
        System.arraycopy(pay, hlen+8, nickname, 0, nlen);
        return(new HNPair(handle, nickname));
    }
    
    public byte[] getCharacterStats(){
        for(int i=0; i<0xD0; i++) pay[4+i] = (byte) (pay[4+i] ^ calc_shift((byte)i, (byte)(pid & (byte)0xff)));
        byte[] retval = new byte[0xD0];
        System.arraycopy(pay, 4, retval, 0, 0xD0);
        return retval;
    }
        
    // this returns the first two bytes of the payload as int
    public int getNumber() {
        return((((int)pay[0] << 8)&0xFF00) | ((int)pay[1] & 0xFF));
    }
    
    // decrypts and returns chat data
    public byte[] getChatOutData() {
        this.cryptString();
        int length = (((int)pay[0] << 8)|((int)pay[1])) -2;
        byte[] retval = new byte[length];
        System.arraycopy(pay, 4, retval, 0, length);
        return retval;
    }
    
    // decrypts handle and event data in pregame and after game lobby
    // and returns the broadcast data
    public byte[] getEvenData() {
        int hlen = (((int)pay[0] << 8)|((int)pay[1])) -2; // skip the sum
        int elen = ((((int)pay[hlen +4]&0xff) << 8)|((int)pay[hlen +5])&0xff) -2; // skip the sum

        for(int i=0; i<hlen; i++) pay[4+i] = (byte) (pay[4+i] ^ calc_shift((byte)i, (byte)(pid & (byte)0xff)));
        for(int i=0; i<elen; i++) pay[hlen+8+i] = (byte) (pay[hlen+8+i] ^ calc_shift((byte)i, (byte)(pid & (byte)0xff)));

        ByteBuffer z = ByteBuffer.wrap(new byte[hlen+elen+4]);
        z.putShort((short)hlen);
        z.put(pay,4,hlen);
        z.putShort((short)elen);
        z.put(pay,hlen+8,elen);
        byte[] retval = new byte[z.position()];
        z.rewind();
        z.get(retval);
        return retval;
    }
    
    // decrypt a private message and create broadcast in one step
    public PrivateMessage getDecryptedPvtMess(Client sender) {
        int hlen = (((int)pay[0] << 8)|((int)pay[1])) -2; // skip the sum
        int nlen = (((int)pay[hlen +4] << 8)|((int)pay[hlen +5])) -2; // skip the sum

        for(int i=0; i<hlen; i++) pay[4+i] = (byte) (pay[4+i] ^ calc_shift((byte)i, (byte)(pid & (byte)0xff)));
        for(int i=0; i<nlen; i++) pay[hlen+8+i] = (byte) (pay[hlen+8+i] ^ calc_shift((byte)i, (byte)(pid & (byte)0xff)));

        byte[] recipient = new byte[hlen];
        byte[] message = new byte[nlen];
        System.arraycopy(pay, 4, recipient, 0, hlen);
        System.arraycopy(pay, hlen+8, message, 0, nlen);
        return(new PrivateMessage(sender.getHNPair().getHandle(), sender.getHNPair().getNickname(), recipient, message));
    }
    
    // helper function
    private int decryptBuff(byte[] b, int offset) {
        int mlen = ((((int)b[offset]&0xff) << 8)|((int)b[offset+1]&0xff)) -2;
        for(int i=0; i<mlen; i++) pay[offset+4+i] = (byte) (pay[offset+4+i] ^ calc_shift((byte)i, (byte)(pid & (byte)0xff)));
        return mlen;
    }
    
    // decrypt the game information for further use in database and ranklists
    public byte[] getDecryptedPostGameInfo() {
        int l;
        
        // game session
        l = 2 + 4 + this.decryptBuff(pay, 2) + 15 + 2;

        // co players
        l = l + this.decryptBuff(pay, l) + 4;
        l = l + this.decryptBuff(pay, l) + 4 + 2;
        
        l = l + this.decryptBuff(pay, l) + 4;
        l = l + this.decryptBuff(pay, l) + 4 + 2;
        
        l = l + this.decryptBuff(pay, l) + 4;
        l = l + this.decryptBuff(pay, l) + 4 + 2;

        return pay;
    }
}
