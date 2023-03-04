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

import java.util.LinkedList;
import java.util.List;

/**
 * All areas
 */
public class Areas {
    private List areas;
    
    // setup our areas
    public Areas() {
        areas = new LinkedList();
        areas.add(new Area(1, "East Town", "<BODY><SIZE=3>standard rules<END>", Area.STATUS_ACTIVE));
        areas.add(new Area(2, "West Town", "<BODY><SIZE=3>individual games<END>",Area.STATUS_ACTIVE));
    }
    
    // how many areas do we have ?
    public int getAreaCount() {
        return areas.size();
    }
    
    public String getName(int areanumber) {
        Area area = (Area) areas.get(areanumber-1);
        return(area.getName());
    }
    
    public String getDescription(int areanumber) {
        Area area = (Area) areas.get(areanumber-1);
        return(area.getDescription());
    }
    
    public byte getStatus(int areanumber) {
        Area area = (Area) areas.get(areanumber-1);
        return(area.getStatus());
    }
}
