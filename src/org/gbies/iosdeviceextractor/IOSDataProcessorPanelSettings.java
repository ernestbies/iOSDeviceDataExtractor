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


public class IOSDataProcessorPanelSettings {
    private boolean liveExtraction;
    private String extractDirectoryName;
    private String password;
    private boolean extractToZip;
    private boolean backupEncrypted;

    
    public boolean isExtractToZip() {
        return extractToZip;
    }

    public void setExtractToZip(boolean extractToZip) {
        this.extractToZip = extractToZip;
    }
        
    public boolean isLiveExtraction() {
        return liveExtraction;
    }

    public void setLiveExtraction(boolean isLiveExtraction) {
        this.liveExtraction = isLiveExtraction;
    }

    public String getExtractDirectoryName() {
        return extractDirectoryName;
    }

    public void setExtractDirectoryName(String extractDirectoryName) {
        this.extractDirectoryName = extractDirectoryName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBackupEncrypted() {
        return backupEncrypted;
    }

    public void setBackupEncrypted(boolean backupEncrypted) {
        this.backupEncrypted = backupEncrypted;
    }        
}
