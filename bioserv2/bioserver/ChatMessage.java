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

public class ChatMessage {
    public int id;
    public byte[] handle;
    public byte[] nickname;
    public byte[] message;
    
    public int area;
    public int room;
    public int slot;
    public int game;

    public ChatMessage(int id, int area, int room, int slot, int game, String handle, String nickname, String message) {
        this.id = id;
        
        this.handle = handle.getBytes();
        this.nickname = nickname.getBytes();
        this.message = message.getBytes();
        
        this.area = area;
        this.room = room;
        this.slot = slot;
        this.game = game;
    }
}
