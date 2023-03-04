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

// Definitions for the protocol
public class Commands {
    
    final static byte SERVER         = (byte) 0x18;    // a packet from the server
    final static byte CLIENT         = (byte) 0x81;    // a packet from the client
    final static byte GAMESERVER     = (byte) 0x28;    // a packet from the gameserver
    final static byte GAMECLIENT     = (byte) 0x82;    // a packet from the gameclient

    final static byte QUERY          = 0x01;    // ask a question
    final static byte TELL           = 0x02;    // answer a question
    final static byte BROADCAST      = 0x10;    // send a packet to all clients

    final static int LOGIN           = 0x6101;
    final static int UNKN61A0        = 0x61A0;  // TIMEOUTS ?
    final static int CHECKVERSION    = 0x6103;  // check the clients version
    final static int CHECKRND        = 0x600E;  // check random numbers
    final static int UNKN61A1        = 0x61A1;
    final static int IDHNPAIRS       = 0x6131;  // send available ID/HN pairs

    final static int HNSELECT        = 0x6132;  // which pair shall be used
    final static int UNKN6104        = 0x6104;
    final static int MOTHEDAY        = 0x614C;  // message of the day, kind of html/xml used

    final static int CHARSELECT      = 0x6190;  // selected char and its statistics
    final static int UNKN6881        = 0x6881;
    final static int UNKN6882        = 0x6882;
    final static int RANKINGS        = 0x6145;  // playerranking one can see in the area lobby
    final static int UNKN6141        = 0x6141;
    final static int AREACOUNT       = 0x6203;  // how many areas has this server
    final static int AREAPLAYERCNT   = 0x6205;  // number of players in the area
    final static int AREASTATUS      = 0x6206;  // area available (0) or locked (3)
    final static int AREANAME        = 0x6204;  // name of the area
    final static int AREADESCRIPT    = 0x620A;  // descripton of the area
    final static int HEARTBEAT       = 0x6202;  // send every 30 secs to the clients

    final static int AREASELECT      = 0x6207;  // choose area
    final static int EXITAREA        = 0x6209;  // leave the roomlist (back to arealist)
    final static int ROOMSCOUNT      = 0x6301;  // rooms in area
    final static int ROOMPLAYERCNT   = 0x6303;
    final static int ROOMSTATUS      = 0x6304;  // status of a room
    final static int ROOMNAME        = 0x6302;  // Name of a room
    final static int UNKN6308	     = 0x6308;

    final static int ENTERROOM       = 0x6305;
    final static int SLOTCOUNT       = 0x6401;  // How many gameslots are in the room ?
    final static int SLOTPLRSTATUS   = 0x6403;  // how many players are in slot / available in slot ?
    final static int SLOTSTATUS      = 0x6404;  // is slot available, used or full ?
    final static int SLOTTITLE       = 0x6402;  // title of the gameslot
    final static int SLOTATTRIB2     = 0x640B;
    final static int SLOTPWDPROT     = 0x6405;  // flag for password protection
    final static int SLOTSCENTYPE    = 0x650A;  // scenario and type (DVD/HDD) for this slot

    final static int RULESCOUNT      = 0x6603;  // how many rules are there for slot ?
    final static int RULEATTCOUNT    = 0x6607;  // how many attributes has a rule ?
    final static int UNKN6601        = 0x6601;
    final static int UNKN6602        = 0x6602;
    final static int RULEDESCRIPT    = 0x6604;  // name of the rule
    final static int RULEVALUE       = 0x6606;  // get value of rule
    final static int RULEATTRIB      = 0x6605;  // additional attribute 2 of rule
    final static int ATTRDESCRIPT    = 0x6608;  // name of the choice
    final static int ATTRATTRIB      = 0x660E;  // attribute of the choice (always 0?)
    final static int PLAYERSTATS     = 0x640A;  // statistics of players in room
    final static int EXITSLOTLIST    = 0x6408;  // leave the slotlist (back to roomlist)

    final static int CREATESLOT      = 0x6407;  // create a new slot
    final static int SCENESELECT     = 0x6509;  // select scenario for slot
    final static int SLOTNAME        = 0x6609;  // set name of the slot
    final static int SETRULE         = 0x660B;  // set rule for gameslot
    final static int UNKN660C        = 0x660C;
    final static int SLOTTIMER       = 0x6409;  // wait time for a gameslot
    final static int UNKN6412        = 0x6412;
    final static int UNKN6504        = 0x6504;
    final static int CANCELSLOT      = 0x6501;  // cancel game in slot
    final static int LEAVESLOT       = 0x6502;  // leave slot
    final static int PLAYERSTATBC    = 0x6503;  // broadcasting statistics of a joining player
    final static int CANCELSLOTBC    = 0x6505;  // broadcast when host cancels slot
    final static int PLAYEROK        = 0x6506;  // broadcast when player is "unlocked"
    final static int STARTGAME       = 0x6508;  // broadcast by host when game will be started

    final static int CHATIN          = 0x6701;  // chat message from a client
    final static int CHATOUT         = 0x6702;  // chat mesage from server

    final static int GETREADY        = 0x6910;  // broadcasted by server, clients request game details then
    final static int PLAYERCOUNT     = 0x6911;  // total number of players for the gamesession
    final static int PLAYERNUMBER    = 0x6912;  // number of player
    final static int PLAYERSTAT      = 0x6913;  // statistic of a player in slot
    final static int PLAYERSCORE     = 0x6917;  // scoring from the ranklist for a player
    final static int GAMESESSION     = 0x6915;  // the session number for this game
    final static int GAMEDIFF        = 0x6914;  // difficulty of the game
    final static int GSINFO          = 0x6916;  // gameserver info (192.168.2.1:8590)
    final static int UNKN6002        = 0x6002;

    final static int ENTERAGL        = 0x6210;  // entering the aftergame
    final static int AGLSTATS        = 0x6213;  // stats after the game
    final static int AGLPLAYERCNT    = 0x6212;  // number of players in aftergame lobby
    final static int LEAVEAGL        = 0x6211;  // leave after game lobby ?
    final static int JOINGAME        = 0x6406;  // joining a slot not as host
    final static int AGLLEAVE        = 0x6214;  // broadcasting that a player left
    final static int AGLJOIN         = 0x6215;  // broadcast stats of client to aftergame lobby
    final static int GETINFO         = 0x6801;  // request the information

    final static int EVENTDAT        = 0x670D;
    final static int EVENTDATBC      = 0x670E;

    final static int BUDDYLIST       = 0x6707;  // check status of a buddy
    final static int CHECKBUDDY      = 0x6703;  // online status checker
    final static int PRIVATEMSG      = 0x6704;  // private messaging
    final static int PRIVATEMSGBC    = 0x6705;  // broadcast of the private msg

    final static int UNKN6181        = 0x6181;  // unknown, deep in the database functions ...
                                                // something along registration ?
    
    final static int CONNCHECK       = 0x6001;  // send every 60 secs to client
    
    final static int LOGOUT          = 0x6006;
    final static int SLOTPASSWD      = 0x660A;  // set passowrd for slot

    final static int POSTGAMEINFO    = 0x6138;  // statistics for the played game, used for rankings

    // packets for gameserver
    final static int GSLOGIN         = 0x1031;  // first login packet for gameserver
}
