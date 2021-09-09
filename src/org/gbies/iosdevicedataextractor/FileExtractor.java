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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;
import org.gbies.iosbackupextractor.BackupFile;
import org.gbies.iosbackupextractor.BackupReadException;
import org.gbies.iosbackupextractor.NotUnlockedException;
import org.gbies.iosbackupextractor.UnsupportedCryptoException;

/**
 * Extracts file from iOS Backup.
 * 
 */
public class FileExtractor {

    private final BackupFile backupFile;
    private final int BUFFER_SIZE = 8192;
    private final byte[] buffer = new byte[BUFFER_SIZE];

    /**
     * Main constructor.
     * 
     * @param backupFile BackupFile to be extracted.
     */
    public FileExtractor(BackupFile backupFile) {
        this.backupFile = backupFile;        
    }
    
    
    /**
     * Extracts file from backup to target file on disk.
     * 
     * @param destinationFile The file to which the data is to be extracted.
     * @throws FileNotFoundException, BackupReadException, NotUnlockedException, UnsupportedCryptoException, IOException
     */
    public void extractToFile(File destinationFile) throws FileNotFoundException, BackupReadException, NotUnlockedException, UnsupportedCryptoException, IOException {

        try (FileOutputStream fileOutputStream = new FileOutputStream(destinationFile); 
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)) {
            
            BufferedInputStream bufferedInputStream = backupFile.getFileStream();
            bufferedOutputStream.flush();
            fileOutputStream.getChannel().truncate(backupFile.getSize());
            long realFileSize = (backupFile.getContentFile().get().length() > backupFile.getSize()) ? backupFile.getContentFile().get().length() : backupFile.getSize();
            fileOutputStream.getChannel().truncate(realFileSize);
            long padding = realFileSize - fileOutputStream.getChannel().size();
            
            int bytesRead;
            while (padding > 0 && (bytesRead = bufferedInputStream.read(buffer, 0, (int) Math.min(buffer.length, padding))) != -1) {
                bufferedOutputStream.write(buffer, 0, bytesRead);
                padding -= bytesRead;
            }          
        }        
    }
    
    /**
     * Add file form backup to zip archive.
     *
     * @param zipOutputStream Stream to which data is to be added.
     * @throws FileNotFoundException, BackupReadException, NotUnlockedException, UnsupportedCryptoException, IOException
     */
    public void addToArchive(ZipOutputStream zipOutputStream) throws BackupReadException, NotUnlockedException, UnsupportedCryptoException, FileNotFoundException, IOException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(backupFile.getFileStream())) {
            int readData;
            
            while ((readData = bufferedInputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, readData);
            }
        }
    }
    
}
