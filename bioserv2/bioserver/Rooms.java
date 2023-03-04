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

import java.util.LinkedList;
import java.util.List;

/**
 * a class containing all rooms
 */
public class Rooms {
    private List rooms;
    private int numberOfAreas;
    private final int numberOfRooms = 6;

    public Rooms(int numberOfAreas) {
        rooms = new LinkedList();
        this.numberOfAreas = numberOfAreas;

        for(int i=1; i<=numberOfAreas; i++) {
            rooms.add(new Room(i, "free", Room.STATUS_ACTIVE));
            rooms.add(new Room(i, "R1", Room.STATUS_INACTIVE));
            rooms.add(new Room(i, "R2", Room.STATUS_INACTIVE));
            rooms.add(new Room(i, "R3", Room.STATUS_INACTIVE));
            rooms.add(new Room(i, "R4", Room.STATUS_INACTIVE));
            rooms.add(new Room(i, "R5", Room.STATUS_INACTIVE));
        }
    }
    
    public byte getStatus(int areanr, int roomnr) {
        Room r = (Room) rooms.get((areanr-1)*this.numberOfRooms + roomnr-1);
        return r.getStatus();
    }
    
    public String getName(int areanr, int roomnr) {
        Room r = (Room) rooms.get((areanr-1)*this.numberOfRooms + roomnr-1);
        return r.getName();
    }

    public int getRoomCount() {
        return this.numberOfRooms;
    }
}
