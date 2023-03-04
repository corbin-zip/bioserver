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

import java.nio.channels.SocketChannel;

/**
 * class for a gameslot
 * here are games organized
 */
public class Slot {
    public final static byte STATUS_DISABLED = 0;
    public final static byte STATUS_FREE = 1;
    public final static byte STATUS_INCREATE = 2;    // TODO: find this out
    public final static byte STATUS_GAMESET = 3;    // while creating ruleset
    public final static byte STATUS_BUSY = 4;       // FULL
    
    // 0=training ground, 1=WT, ..., 6=elimination1
    // TODO: possibly more ?
    public final static byte SCENARIO_TRAINING = 0;
    public final static byte SCENARIO_WILDTHINGS = 1;
    public final static byte SCENARIO_UNDERBELLY = 2;
    public final static byte SCENARIO_FLASHBACK = 3;
    public final static byte SCENARIO_DESPERATETIMES = 4;
    public final static byte SCENARIO_ENDOFTHEROAD = 5;
    public final static byte SCENARIO_ELIMINATION1 = 6;
    
    // 0 = not set 1 = dvd-rom  2 = hdd
    // TODO: there are more! what means 0x11 ?
    public final static byte LOAD_NOTSET = 0;
    public final static byte LOAD_DVDROM = 1;
    public final static byte LOAD_HARDSK = 2;
    
    public final static byte PROTECTION_OFF = 0;
    public final static byte PROTECTION_ON  = 1;

    public final static long WAITTIME_MILLSEC = 30*1000*1000;
    
    public int area;
    public int room;
    public int slotnum;
    public int gamenr;
    public int betatest;
    
    private byte[] name;
    private byte status;
    private byte[] password;
    
    private byte protection;    // using password ?
    private byte scenario;
    private byte slottype;
    
    // rules for the game in slot
    private RuleSet rules;
    
    // timeout
    private long livetime;
    
    // room master's userid
    private String host;
    
    // create an empty slot
    public Slot(int area, int room, int slotnum) {
        this.area = area;
        this.room = room;
        this.slotnum = slotnum;
        this.gamenr = 0;
        this.betatest = 0;
        
        this.name = "(free)".getBytes();
        this.status = Slot.STATUS_FREE;
        this.scenario = Slot.SCENARIO_TRAINING;
        this.slottype = Slot.LOAD_NOTSET;
        this.protection = Slot.PROTECTION_OFF;
        this.rules = new RuleSet();
        this.livetime = -1;
    }
    
    public void reset() {
        this.name = "(free)".getBytes();
        this.status = Slot.STATUS_FREE;
        this.scenario = Slot.SCENARIO_TRAINING;
        this.slottype = Slot.LOAD_NOTSET;
        this.protection = Slot.PROTECTION_OFF;
        this.gamenr = 0;
        this.betatest = 0;
        rules.reset();
    }
    
    public byte[] getName() {
        return this.name;
    }
    
    public void setName(byte[] name) {
        this.name = name;
    }
    
    public byte[] getPassword() {
        return this.password;
    }
    
    public void setPassword(byte[] passwd) {
        this.password = passwd;
        if(passwd.length > 0) this.protection = Slot.PROTECTION_ON;
    }
    
    public byte getStatus() {
        return this.status;
    }
    
    public void setStatus(byte status) {
        this.status= status;
    }
    
    public byte getProtection() {
        return this.protection;
    }

    public byte getSscenario() {
        return this.scenario;
    }

    public void setSscenario(byte scenario) {
        this.scenario = scenario;
    }

    public byte getSlotType() {
        return this.slottype;
    }

    public void setSlotType(byte slottype) {
        this.slottype = slottype;
    }

    public byte getRulesCount() {
        return (byte) (this.rules.getRulesCount());
    }
    
    public byte getRulesAttCount(int rulenr) {
        return (byte) (this.rules.getRulesAttCount(rulenr));
    }
    
    public String getRuleName(int rulenr) {
        return rules.getRuleName(rulenr);
    }
    
    public byte getRuleValue(int rulenr) {
        return rules.getRuleValue(rulenr);
    }

    public void setRuleValue(int rulenr, byte value) {
        rules.setRuleValue(rulenr, value);
    }
    
    public byte getRuleAttribute(int rulenr) {
        return rules.getRuleAttribute(rulenr);
    }

    public String getRuleAttributeDescription(int rulenr, int attnr) {
        return rules.getRuleAttName(rulenr, attnr);
    }
    
    public byte getRuleAttributeAtt(int rulenr, int attnr) {
        return rules.getRuleAttAtt(rulenr, attnr);
    }
    
    public void setLivetime() {
        this.livetime = System.currentTimeMillis() + this.rules.getWaitTime()*60*1000;
    }
    
    // 0x0708 = 1800 seconds = 30 minutes
    // on timeout always return 0, game will be started
    public long getLivetime() {
        long retval = (livetime - System.currentTimeMillis())/1000;
        if (retval <0) retval = 0;
        return retval;
    }
    
    public RuleSet getRuleSet() {
        return this.rules;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public String getHost() {
        return this.host;
    }
}
