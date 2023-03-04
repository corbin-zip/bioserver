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

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Date;

/*
 * a simple class to print the hexdump of a byte buffer
 */
public class Logging {
    // how many bytes should be shown in one line ?
    private static final int WIDTH = 16;

    public static void printBuffer(byte[] buffer, int length) {
        int index = 0;

        while(length>0) {
            // print a full line or rest of the buffer
            int j = Math.min(length, WIDTH);
            for(int i=0; i<j; i++) System.out.printf("%02x ", buffer[index+i]);
            for(int i=0; i<(WIDTH+1-j); i++) System.out.printf("   ");
            for(int i=0; i<j; i++) {
                char c = (char) buffer[index+i];
                System.out.printf("%c", Character.isLetterOrDigit(c)||Character.isSpaceChar(c) ? c:'.');
            }
            System.out.printf("\n");

            length -= WIDTH;
            index  += WIDTH;
        }
    }
	
    // print the complete buffer
    public static void printBuffer(byte[] buffer) {
            printBuffer(buffer, buffer.length);
    }
    
    // print a message to console
    public static void println(String msg) {
        Date date = new Date();
        System.out.println(date.toString()+" "+msg);
    }
 
    // check for deadlocks and log
    public static void checkDeadlocks() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long[] threadIds = bean.findDeadlockedThreads(); // Returns null if no threads are deadlocked.

        if (threadIds != null) {
            String exception = "DEADLOCK found\n";
            ThreadInfo[] infos = bean.getThreadInfo(threadIds);

            for (ThreadInfo info : infos) {
                StackTraceElement[] stack = info.getStackTrace();
                // Log or store stack trace information.
                for (StackTraceElement element : stack)
                    exception += element.toString() + "\n";
            }
            println(exception);
        }
    }
    
}
