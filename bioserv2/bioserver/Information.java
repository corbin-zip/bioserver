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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *  Organize the urls in lobby
 * 
 *  preliminary class, we need url to data mapping
 *  currently always the same page is delivered
 */
public class Information {
    
    // beware of the quotes, they need to be backslashed !
    String contents_s =
            "<HTML>"
            + "<HEAD>"
            + "<!--"
            + "	<GAME-STYLE>"
            + "		\"MOUSE=OFF\","
            + "		\"SCROLL=OFF\","
            + "		\"TITLE=OFF\","
            + "		\"BACK=ON:mmbb://BUTTON_NG\","
            + "		\"FORWARD=OFF\","
            + "		\"CANCEL=OFF\","
            + "		\"RELOAD=OFF\","
            + "		\"CHOICE_MV=OFF\","
            + "		\"X_SHOW=OFF\","
            + "		\"FRONT_LABEL=ON:6\","
            + "	</GAME-STYLE>"
            + "-->"
            + "<TITLE>database</TITLE><meta http-equiv=\"Content-Type\" content=\"text/html; charset=Shift_JIS\"></HEAD>"
            + ""
            + "<BODY bgcolor=\"#000033\" text=#FFFFFF>"
            + "<!-- Choices -->"
            + "<br>"
            + "<IMG SRC=\"\" width=0 height=0 USEMAP=#CENTER_MAP BORDER=0>"
            + "<MAP NAME=CENTER_MAP>"
            + "<!--CHG-IMG-BUTTON-2--><AREA SHAPE=RECT COORDS=\"164, 30,416, 60\" HREF=lbs://lbs/05/INFOR/INFOR00.HTM>"
            + "<!--CHG-IMG-BUTTON-2--><AREA SHAPE=RECT COORDS=\"164, 92,416,118\" HREF=lbs://lbs/05/RANKING.HTM>"
            + "<!--CHG-IMG-BUTTON-2--><AREA SHAPE=RECT COORDS=\"164,154,416,219\" HREF=afs://02/2>"
            + "<!--CHG-IMG-BUTTON-2--><AREA SHAPE=RECT COORDS=\"164,216,416,266\" HREF=afs://02/4>"
            + "</MAP> "
            + ""
            + ""
            + "<table width=584 cellspacing=30 cellpadding=0>"
            + "  <tr> "
            + "    <td align=center>&nbsp;</td>"
            + "    <td width=256 height=32 align=center background=afs://02/123.PNG>INFORMATION</td>"
            + "    <td align=center>&nbsp;</td>"
            + "  </tr>"
            + "  <tr> "
            + "    <td align=center>&nbsp;</td>"
            + "    <td width=256 height=32 align=center background=afs://02/123.PNG>RANKING</td>"
            + "    <td align=center>&nbsp;</td>"
            + "  </tr>"
            + "  <tr> "
            + "    <td align=center>&nbsp;</td>"
            + "    <td width=256 height=32 align=center background=afs://02/123.PNG>TERMS OF USE</td>"
            + "    <td align=center>&nbsp;</td>"
            + "  </tr>"
            + "  <tr> "
            + "    <td align=center>&nbsp;</td>"
            + "    <td width=256 height=32 align=center background=afs://02/123.PNG>REGISTER / CHANGE</td>"
            + "    <td align=center>&nbsp;</td>"
            + "  </tr>"
            + "</table>"
            + "</BODY>"
            + "</HTML>";
    
    public Information() {
        
    }
    
    // retrieve the desired URL
    public byte[] getData(String url) {
        byte[] content_b = null;
        url = ("htm/"+url).replace("..", "X");
//        Logging.println("requested url: "+url);
        try {
            content_b = Files.readAllBytes(Paths.get(url));
        } catch (IOException ex) {
            Logging.println("Error reading file: "+url);
            content_b = contents_s.getBytes();
        }
        return content_b;
    }    
}
