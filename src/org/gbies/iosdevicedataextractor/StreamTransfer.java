/* 
 * Copyright (C) 2021 Grzegorz Bieś, Ernest Bieś
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * iOS Device Data Extractor (Autopsy module), version  1.0
 *
 */

package org.gbies.iosdevicedataextractor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamTransfer extends Thread {

    private InputStream in;
    private OutputStream out;

    public StreamTransfer(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[8192];

        try {
            for (;;) {
                if (Thread.interrupted()) {
                    break;
                }

                int count = in.read(buffer);

                if (count < 0) {
                    break;
                }

                if (count > 0 && out != null) {
                    out.write(buffer, 0, count);
                }
                
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
