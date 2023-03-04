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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * File#1 had a patch to version 1.01
 * this class holds the patch data
 */
public class Patch {
    
    // patch length is 0x7aa0
    private byte[] patchData;

    public Patch() {
        try {
            File file = new File("patch.raw");
            this.patchData = new byte[(int) file.length()];
            
            FileInputStream fis = new FileInputStream(file);
            fis.read(this.patchData);
            fis.close();
        } catch (IOException ex) {
            Logger.getLogger(Patch.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logging.println("Patch loaded: " + this.patchData.length + " bytes");
    }
    
    public byte[] getData(int nr) {
        // use a chunk of 0x100 bytes, last chunk might be different
        int size = 0x100;

        // check if more bytes are requested than left in buffer
        if(this.patchData.length < (nr * 0x100 + 0x100)) size = patchData.length - (nr * 0x100);
        if(size < 0 ) {
            size = 0;
            nr = 0;
        }
        byte[] d = new byte[size + 4];

        // set chunk number and size
        d[0] = (byte) ((nr >> 8) & 0xff);
        d[1] = (byte) (nr & 0xff);
        d[2] = (byte) ((size >> 8) & 0xff);
        d[3] = (byte) (size & 0xff);
        System.arraycopy(this.patchData, nr * 0x100, d, 4, size);
        return(d);
    }

    // calculate the amount of chunks we can send
    public int cntChunks(int nr) {
        int max = 8;
        int chunksLeft = (byte)((this.patchData.length - (nr * 0x100)) >> 8);
        if(chunksLeft < 0) chunksLeft = 0;
        if(chunksLeft < max) max = chunksLeft + 1;
        return(max);
    }
}
