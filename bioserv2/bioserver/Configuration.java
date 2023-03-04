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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Configuration
 *  reads config.properties
 */
public class Configuration {
    public String gs_ip;
    public String db_user;
    public String db_password;

    // constructor
    public Configuration() {
        InputStream inputStream = null;
        Properties prop = new Properties();
        String propFileName = "config.properties";

        try {
            inputStream = new FileInputStream(propFileName);
            prop.load(inputStream);

            this.gs_ip       = prop.getProperty("gs_ip");
            this.db_user     = prop.getProperty("db_user");
            this.db_password = prop.getProperty("db_password");
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("Exception: " + e);
                }
            }
        }
    }
    
}
