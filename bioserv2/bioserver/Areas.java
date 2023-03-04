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
 * All areas
 */
public class Areas {
    private final List areas;
    
    // setup our areas
    public Areas(byte secretAreaStatus) {
        areas = new LinkedList();
        areas.add(new Area(1, "Free Area",    "<BODY><SIZE=3>Join games or create your own<BR><BODY><C=3>keep an eye on PS2 and emu problems<END>", Area.STATUS_ACTIVE));
        areas.add(new Area(2, "Nightmare",    "<BODY><SIZE=3>Nightmare mode is ON by default<BR><BODY><C=3><END>",Area.STATUS_ACTIVE));
        areas.add(new Area(3, "Survival",     "<BODY><SIZE=3>Friendly Fire is ON by default<BR><BODY><C=3>Beware of stray bullets ;-)<END>", Area.STATUS_ACTIVE));
        areas.add(new Area(4, "Panic",        "<BODY><SIZE=3>Nightmare mode and friendly fire is on!<BR><BODY><C=3>just keep alive...<END>", Area.STATUS_ACTIVE));
        areas.add(new Area(5, "Infinity",     "<BODY><SIZE=3>Infinitive bullets<BR><BODY><C=3>Games are not considered for rankings!<END>", Area.STATUS_ACTIVE));
        areas.add(new Area(6, "reserved",     "<BODY><SIZE=3>reserved<END>", Area.STATUS_INACTIVE));
        areas.add(new Area(7, "TESTING",      "<BODY><SIZE=3>for tests<BR><BODY>expect problems and crashes, <C=3>use at your own risk<END>", Area.STATUS_ACTIVE));
        areas.add(new Area(8, "Elimination",  "<BODY><SIZE=3>Play scenarios elimination 1-3<BR><BODY><C=3>defeat enemies within given time with your colleagues<END>", Area.STATUS_ACTIVE));
        areas.add(new Area(9, "Showdown",     "<BODY><SIZE=3>Play scenarios showdown 1-3<BR><BODY><C=3>defeat bosses with joined forces!<END>", Area.STATUS_ACTIVE));
        areas.add(new Area(10, "SECRET Area", "<BODY><SIZE=3>?????<BR><BODY><C=3>Games are not considered for rankings!<END>", secretAreaStatus));
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

    public void setStatus(int areanumber, byte newstatus) {
        Area area = (Area) areas.get(areanumber-1);
        area.setStatus(newstatus);
    }
}
