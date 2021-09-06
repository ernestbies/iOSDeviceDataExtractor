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
 * iOS Device Data Extractor Autopsy Module, version  1.0
 *
 */

package org.gbies.iosdeviceextractor;

/**
 * Exception for unpack data.
 * 
 */
public class UnpackDataException extends Exception {

    public UnpackDataException(Exception ex) {
        super(ex);
    }

    public UnpackDataException(String msg) {
        super(msg);
    }

    public UnpackDataException(Exception ex, String msg) {
        super(msg, ex);
    }
}
