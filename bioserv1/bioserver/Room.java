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
 * Object for rooms within areas
 */
public class Room {
    public static final byte STATUS_ACTIVE = 3;
    public static final byte STATUS_INACTIVE = 0;
    
    private int areanumber;
    private String name;
    private byte status;
    
    public Room(int area, String name, byte status) {
        this.name = name;
        this.status = status;
        this.areanumber = area;
    }
    
    public String getName() {
        return this.name;
    }
    
    public byte getStatus() {
        return this.status;
    }
    
    public int getAreaNumber() {
        return this.areanumber;
    }

}
