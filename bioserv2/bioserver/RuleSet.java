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

/**
 * Object for a rule
 * rules are organzied in rulesets
 * each slot has one ruleset
 */
public class RuleSet {
    public class Rule {
        private String name;
        private byte attribute;     // 1 = changeable, 0 = fixed
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
    
    private int ff_flag, nm_flag, pa_flag, ts_flag;
    
    public Rule[] ruleset;
    public Rule[][] attributes;    
    
    public RuleSet(int area) {
        // different rulesets for the areas
        switch(area) {
            case 2:
                // four players, ten mins wait, very hard, nightmare on
                ruleset = new Rule[] {
                    new Rule("number of players", 1, 2),
                    new Rule("wait limit", 1, 2),
                    new Rule("difficulty level", 1, 3),
                    new Rule("nightmare",0, 1)
                };
                
                attributes = new Rule[][] {
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
                        new Rule("off", 0, 0),
                        new Rule("on", 0, 0),                        
                    }
                };
                nm_flag = 1;
                break;
                
            case 3:
                // four players, ten mins wait, very hard, friendly fire on
                ruleset = new Rule[] {
                    new Rule("number of players", 1, 2),
                    new Rule("wait limit", 1, 2),
                    new Rule("difficulty level", 1, 3),
                    new Rule("friendly fire",0, 1)
                };
                
                attributes = new Rule[][] {
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
                        new Rule("off", 0, 0),
                        new Rule("on", 0, 0),                        
                    }
                };
                ff_flag = 1;
                break;

            case 4:
                // four players, ten mins wait, very hard, friendly fire, nightmare
                ruleset = new Rule[] {
                    new Rule("number of players", 1, 2),
                    new Rule("wait limit", 1, 2),
                    new Rule("difficulty level", 1, 3),
                    new Rule("friendly fire",0, 1),
                    new Rule("nightmare",0, 1),
                };
                
                attributes = new Rule[][] {
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
                        new Rule("off", 0, 0),
                        new Rule("on", 0, 0),                        
                    }, {
                        new Rule("off", 0, 0),
                        new Rule("on", 0, 0),                                           
                    }
                };
                pa_flag = 1;
                break;
                
            case 5:
                // four players, ten mins wait, very hard, friendly fire on
                ruleset = new Rule[] {
                    new Rule("number of players", 1, 2),
                    new Rule("wait limit", 1, 2),
                    new Rule("difficulty level", 1, 3),
                    new Rule("friendly fire",1, 0),
                    new Rule("nightmare",1, 0),
                    new Rule("infinity",0, 1)
                };
                
                attributes = new Rule[][] {
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
                        new Rule("off", 0, 0),
                        new Rule("on", 0, 0),                        
                    }, {
                        new Rule("off", 0, 0),
                        new Rule("on", 0, 0),                        
                    }, {
                        new Rule("off", 0, 0),
                        new Rule("on", 0, 0),                        
                    }
                };
                ts_flag = 1;
                break;
                
                
            // all others
            default:
                // standard setting
                // four players, ten mins wait, very hard
                ruleset = new Rule[] {
                    new Rule("number of players", 1, 2),
                    new Rule("wait limit", 1, 2),
                    new Rule("difficulty level", 1, 3)
                };
                
                attributes = new Rule[][] {
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
                    }
                };
                
        }
    };
    
    // helper function to get a database field from the different rulesets of areas
    public static String getRuleField(int area, byte rulenr) {
        switch(area) {
            case 2:
                switch(rulenr) {
                    case 0:     return("maxplayers");
                    case 1:     return(null);   //return("waittime");
                    case 2:     return("difficulty");
                    case 3:     return("nightmare");
                    default:    return(null);
                }
            
            case 3:
                switch(rulenr) {
                    case 0:     return("maxplayers");
                    case 1:     return(null);   //return("waittime");
                    case 2:     return("difficulty");
                    case 3:     return("friendlyfire");
                    default:    return(null);
                }
            
            case 4:
                switch(rulenr) {
                    case 0:     return("maxplayers");
                    case 1:     return(null);   //return("waittime");
                    case 2:     return("difficulty");
                    case 3:     return("friendlyfire");
                    case 4:     return("nightmare");
                    default:    return(null);
                }
            
            case 5:
                switch(rulenr) {
                    case 0:     return("maxplayers");
                    case 1:     return(null);   //return("waittime");
                    case 2:     return("difficulty");
                    case 3:     return("friendlyfire");
                    case 4:     return("nightmare");
                    case 5:     return("infinity");
                    default:    return(null);
                }

            default:
                switch(rulenr) {
                    case 0:     return("maxplayers");
                    case 1:     return(null);   //return("waittime");
                    case 2:     return("difficulty");
                    default:    return(null);
                }
        }
    }
    
    public void reset() {
        // reset to standard
        ruleset[0].setValue((byte) 2);                  // players
        ruleset[1].setValue((byte) 2);                  // wait limit
        ruleset[2].setValue((byte) 3);                  // difficulty
        if(ff_flag == 1) ruleset[3].setValue((byte) 1); // friendly fire
        if(nm_flag == 1) ruleset[3].setValue((byte) 1); // nightmare
        if(pa_flag == 1) {                              // panic = ff+nightmare
            ruleset[3].setValue((byte)1);
            ruleset[4].setValue((byte)1);
        }
        if(ts_flag == 1) {
            ruleset[3].setValue((byte)0);
            ruleset[4].setValue((byte)0);
            ruleset[5].setValue((byte)1);
        }
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
    
    // 0 = friendly fire off, 1 = on
    public byte getFriendlyFire() {
        if(ff_flag == 1 || ts_flag == 1 || pa_flag == 1) return ruleset[3].getValue();
        return 0;
    }
/*
3rd value = FF (0 for off 1 for on)
4th value unknown(suspect its point multiplier and increases based on value)
5th value = gauge (1 for 30%, 2 for 50, 30 for 80)
6th value nightmare ( 0 off, 1 on)
7th value infinity ( 0 off, 1 on)
8th value special item glow (0 off, 1 on)
*/
    public byte getNightmare() {
        if(nm_flag == 1) return ruleset[3].getValue();
        if(ts_flag == 1 || pa_flag == 1) return ruleset[4].getValue();
        return 0;
    }
    public byte getInfinity() {
        if(ts_flag == 1) return ruleset[5].getValue();
        return 0;
    }

}
