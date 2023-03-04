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
 * Object for a rule
 * rules are organzied in rulesets
 * each slot has one ruleset
 */
public class RuleSet {
    public class Rule {
        private String name;
        private byte attribute;     // TODO: what happens if <>1 ?
        private byte value;
        
        public Rule(String name, int attribute, int value){
            this.name = name;
            this.attribute = (byte) (attribute & 0xff);
            this.value = (byte) (value & 0xff);
        }
        
        public String getName() {
            return name;
        }
        
        public byte getAttribute() {
            return attribute;
        }
        
        public byte getValue() {
            return value;
        }
        
        public void setValue(byte value) {
            this.value = value;
        }
    }
    
    // standard setting
    // four players, ten mins wait, very hard
    public Rule[] ruleset = {
        new Rule("number of players", 1, 2),
        new Rule("wait limit", 1, 2),
        new Rule("difficulty level", 1, 3),
        new Rule("friendly fire", 1, 0),        
    };
    
    public Rule[][] attributes = {
        {
            new Rule("two players", 0, 0),
            new Rule("three players", 0, 0),
            new Rule("four players", 0, 0)
        }, {
            new Rule("three minutes", 0, 0),
            new Rule("five minutes", 0, 0),
            new Rule("ten minutes", 0, 0),
            new Rule("fifteen minutes", 0, 0),
            new Rule("thirty minutes", 0, 0)
        }, {
            new Rule("easy", 0, 0),
            new Rule("normal", 0, 0),
            new Rule("hard", 0, 0),
            new Rule("very hard", 0, 0)
        }, {
            new Rule("off", 0,0),
            new Rule("on", 0,0),
        }
    };
    
    public void RuleSet() {
        
    };
    
    // helper function to get a database field from the different rulesets of areas
    public static String getRuleField(int area, byte rulenr) {
        switch(rulenr) {
            case 0:     return("maxplayers");
            case 1:     return(null);   //return("waittime");
            case 2:     return("difficulty");
            case 3:     return("friendlyfire");
            default:    return(null);
        }
    }

    public void reset() {
        // reset to standard
        ruleset[0].setValue((byte) 2);
        ruleset[1].setValue((byte) 2);
        ruleset[2].setValue((byte) 3);
        ruleset[3].setValue((byte) 0);
    };
    
    public String getRuleName(int nr) {
        return ruleset[nr].getName();
    };

    public byte getRuleAttribute(int nr) {
        return ruleset[nr].getAttribute();        
    };
    
    public String getRuleAttName(int nr, int nratt) {
        return attributes[nr][nratt].getName();
    };
    
    public byte getRuleAttAtt(int nr, int nratt) {
        return attributes[nr][nratt].getAttribute();        
    };
    
    public int getRulesCount() {
        return ruleset.length;
    }
    
    public int getRulesAttCount(int rulenr) {
        return attributes[rulenr].length;
    }

    public byte getRuleValue(int rulenr) {
        return ruleset[rulenr].getValue();
    }
    
    public void setRuleValue(int rulenr, byte value) {
        ruleset[rulenr].setValue(value);
    }
    
    public byte getDifficulty() {
        return ruleset[2].getValue();
    }
    
    public byte getFriendlyFire() {
        return ruleset[3].getValue();
    }
    
    public long getWaitTime() {
        switch (ruleset[1].getValue()) {
            case 0:     return(3);
            case 1:     return(5);
            case 2:     return(10);
            case 3:     return(15);
            case 4:     return(30);
            default:    return(30);
        }
    }
    
    public byte getNumberOfPlayers() {
        switch (ruleset[0].getValue()) {
            case 0:     return(2);
            case 1:     return(3);
            case 2:     return(4);
            default:    return(2);
        }
    }
}
