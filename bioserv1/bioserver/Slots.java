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

public class Slots {
    private List slots;
    private int numberOfAreas;
    private int numberOfRooms;
    private final int numberOfSlots = 20;
    
    // create a list of empty slots
    public Slots(int numberOfAreas, int numberOfRooms) {
        slots = new LinkedList();
        this.numberOfAreas = numberOfAreas;
        this.numberOfRooms = numberOfRooms;

        for(int area=1; area<=numberOfAreas; area ++) {
            for(int room=1; room<=numberOfRooms; room++) {
                for(int slot=0; slot<20; slot++) {
                    slots.add(new Slot(area, room, slot));
                }
            }
        }
    }

    private int calcSlotnr(int area, int room, int slotnr) {
        return ((slotnr-1) + ((room-1)*this.numberOfSlots) + ((area-1)*this.numberOfRooms*this.numberOfSlots));
    }
    
    public Slot getSlot(int area, int room, int slotnr) {
        return (Slot) slots.get(calcSlotnr(area, room, slotnr));
    }
    
    public int getSlotCount() {
        return this.numberOfSlots;
    }
    
    public byte getStatus(int area, int room, int slotnr) {
        Slot slot = (Slot) slots.get(calcSlotnr(area, room, slotnr));
        return slot.getStatus();
    }

    public byte[] getName(int area, int room, int slotnr) {
        Slot slot = (Slot) slots.get(calcSlotnr(area, room, slotnr));
        return slot.getName();
    }
    
    public byte getScenario(int area, int room, int slotnr) {
        Slot slot = (Slot) slots.get(calcSlotnr(area, room, slotnr));
        return slot.getSscenario();
    }
    
    public byte getProtection(int area, int room, int slotnr) {
        Slot slot = (Slot) slots.get(calcSlotnr(area, room, slotnr));
        return slot.getProtection();
    }

    public byte getSlotType(int area, int room, int slotnr) {
        Slot slot = (Slot) slots.get(calcSlotnr(area, room, slotnr));
        return slot.getSlotType();
    }
    
    public byte getRulesCount(int area, int room, int slotnr) {
        Slot slot = (Slot) slots.get(calcSlotnr(area, room, slotnr));
        return slot.getRulesCount();
    }

    public byte getRulesAttCount(int area, int room, int slotnr, int rulenr) {
        Slot slot = (Slot) slots.get(calcSlotnr(area, room, slotnr));
        return slot.getRulesAttCount(rulenr);
    }
    
    public String getRuleName(int area, int room, int slotnr, int rulenr) {
        Slot slot = (Slot) slots.get(calcSlotnr(area, room, slotnr));
        return slot.getRuleName(rulenr);
    }

    public byte getRuleValue(int area, int room, int slotnr, int rulenr) {
        Slot slot = (Slot) slots.get(calcSlotnr(area, room, slotnr));
        return slot.getRuleValue(rulenr);
    }

    public byte getRuleAttribute(int area, int room, int slotnr, int rulenr) {
        Slot slot = (Slot) slots.get(calcSlotnr(area, room, slotnr));
        return slot.getRuleAttribute(rulenr);
    }

    public String getRuleAttributeDescription(int area, int room, int slotnr, int rulenr, int attnr) {
        Slot slot = (Slot) slots.get(calcSlotnr(area, room, slotnr));
        return slot.getRuleAttributeDescription(rulenr, attnr);
    }
    
    public byte getRuleAttributeAtt(int area, int room, int slotnr, int rulenr, int attnr) {
        Slot slot = (Slot) slots.get(calcSlotnr(area, room, slotnr));
        return slot.getRuleAttributeAtt(rulenr, attnr);
    }
    
    public byte getDifficulty(int area, int room, int slotnr) {
        Slot slot = (Slot) slots.get(calcSlotnr(area, room, slotnr));
        return slot.getRuleSet().getDifficulty();
    }

    public byte getFriendlyFire(int area, int room, int slotnr) {
        Slot slot = (Slot) slots.get(calcSlotnr(area, room, slotnr));
        return slot.getRuleSet().getFriendlyFire();
    }

    public byte getMaximumPlayers(int area, int room, int slotnr) {
        Slot slot = (Slot) slots.get(calcSlotnr(area, room, slotnr));
        return slot.getRuleSet().getNumberOfPlayers();
    }
    
 }
