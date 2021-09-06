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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.gbies.iosbackupextractor.BackupFile;
import org.gbies.iosbackupextractor.BackupReadException;
import org.gbies.iosbackupextractor.ITunesBackup;
import org.gbies.iosbackupextractor.NotUnlockedException;
import org.gbies.iosbackupextractor.UnsupportedCryptoException;
import static org.gbies.iosdeviceextractor.addDeviceDataTask.logger;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Exceptions;

/**
 * Reads and extracts data from iOS device or iTunes backup.
 */
public class IOSDataUnpacker {

    private final File filesCommandPath;
    private final ByteArrayOutputStream outputStream;    
    private boolean processing;
    private String backupInfo;
    private String deviceInfo;
    private String deviceName;
    private String deviceProductName;
    private String deviceProductVersion;
    private String uniqueDeviceID;
    private File backupDirectory;
    private File extractDirectory;
    private int backupExtractPercent;
    private final Pattern pattern;    

    /**
     * Main constructor.
     *
     */
    public IOSDataUnpacker() {        
        this.filesCommandPath = getFilesCommandPath();        
        this.outputStream = new ByteArrayOutputStream();
        this.pattern = Pattern.compile("(\\d+)% Finished");
    }

    /**
     * Checks iOS device
     *
     * @return True if the device is connected and false if not. 
     * Reads parameters like DeviceName, ProductName, ProductVersion, etc.
     */
    public boolean checkDevice() {
        outputStream.reset();        
        executeCommand(new String[]{"ideviceinfo","-s"}, outputStream);
        
        StringBuilder deviceParameters = getDeviceParameters(outputStream.toString());
        
        if (uniqueDeviceID == null || "".equals(uniqueDeviceID)) {
            deviceInfo = outputStream.toString();
            return false;
        } else {
            deviceInfo = deviceParameters.toString();
            return true;
        }
    }            

    /**
     * Create iOS backup.
     *
     * @param backupDirectoryPath The folder where the backup will be created
     * @param encrypted True if backup is to be encrypted, false if not.
     * @throws UnpackDataException
     */
    public void createBackup(final String backupDirectoryPath, final boolean encrypted) throws UnpackDataException {
        outputStream.reset();
        
        if ("".equals(uniqueDeviceID) || uniqueDeviceID == null) {
            throw new UnpackDataException("UniqueDeviceID is null or empty!");
        }
        
        File directory = new File(backupDirectoryPath);
        if (!directory.exists()) {
            directory.mkdir();
            logger.log(Level.INFO, "Created backup directory " + directory.getAbsolutePath());
        }
        
        processing = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                                
                String[] cmdArray = new String[]{"idevicebackup2", "backup", "--full", backupDirectoryPath};
                if (encrypted) {
                    executeCommand(new String[]{"idevicebackup2", "backup", "encryption", "on", "1234"}, outputStream);
                    executeCommand(cmdArray, outputStream);
                    executeCommand(new String[]{"idevicebackup2", "backup", "encryption", "off", "1234"}, outputStream);
                } else {
                    executeCommand(cmdArray, outputStream);
                }
                processing = false;
                backupDirectory = new File(backupDirectoryPath, uniqueDeviceID);
            }
        });

        thread.start();        
    }

    /**
     * Extracts files from iOS backup.
     *
     * @param backupDirectoryPath The folder containing the ios backup files.
     * @param password Backup password.
     * @param extractToZIP If true then the backup will be extracted to a zip
     * file if not, to a folder.
     * @throws FileNotFoundException, BackupReadException, InvalidKeyException.
     */
    public void extractBackup(String backupDirectoryPath, String password, boolean extractToZIP) throws BackupReadException, InvalidKeyException, FileNotFoundException {        
        ITunesBackup backup = new ITunesBackup(new File(backupDirectoryPath));
    
        if (backup.manifest.encrypted) {
            backup.manifest.getKeyBag().get().unlock(password);
            backup.decryptDatabase();
        }

        deviceName = backup.manifest.deviceName;
        deviceProductVersion = backup.manifest.productVersion;
        uniqueDeviceID = backup.manifest.uniqueDeviceID;

        StringBuilder information = new StringBuilder();
        information.append("DeviceName: ").append(deviceName).append("\n");
        information.append("ProductVersion: ").append(deviceProductVersion).append("\n");
        information.append("UniqueDeviceID: ").append(uniqueDeviceID).append("\n");
        information.append("Backup encrypted: ").append(backup.manifest.encrypted).append("\n");
        backupInfo = information.toString();

        extractDirectory = new File(backup.directory.getParent(), uniqueDeviceID + "_extract_" + System.currentTimeMillis());

        if (!extractDirectory.exists()) {
            extractDirectory.mkdir();
        }
        
        backupExtractPercent = 0;
        processing = true;
        List<BackupFile> backupFiles = backup.getAllFiles();
        if (!backupFiles.isEmpty()) {
            if (extractToZIP) {
                extractFilesToZipFile(backupFiles, extractDirectory);
            } else {
                extractFilesToFolder(backupFiles, extractDirectory);
            }
        } else {
            logger.log(Level.INFO, "No files in backup");
        }

    }

    public void extractBackup(boolean extractToZIP) throws FileNotFoundException, BackupReadException, InvalidKeyException {        
        extractBackup(backupDirectory.getAbsolutePath(), "1234", extractToZIP);
    }

    /**
     * Extracts files from iOS backup
     * and add to ZIP archive file.
     *
     * @param backupFiles List BackupFiles that will be extracted.
     * @param destinationDirectory The folder where ZIP file will be created.
     */
    private void extractFilesToZipFile(final List<BackupFile> backupFiles, final File destinationDirectory) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {                
                int count = backupFiles.size();
                File zipFile = new File(destinationDirectory.getAbsolutePath(), uniqueDeviceID + ".zip");                

                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(zipFile);
                } catch (FileNotFoundException ex) {
                    Exceptions.printStackTrace(ex);
                }
                
                ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
                int i = 0;
                for (BackupFile backupFile : backupFiles) {
                    try {
                        String relative = Paths.get(backupFile.domain, backupFile.relativePath).toString();

                        ZipEntry zipEntry;
                        switch (backupFile.getFileType()) {
                            case SYMBOLIC_LINK:
                                zipEntry = new ZipEntry(relative);
                                zipOutputStream.putNextEntry(zipEntry);
                                break;
                            case DIRECTORY:
                                zipEntry = new ZipEntry(relative + "/");
                                zipOutputStream.putNextEntry(zipEntry);
                                break;
                            case FILE:
                                zipEntry = new ZipEntry(relative);
                                zipOutputStream.putNextEntry(zipEntry);

                                FileExtractor fileExtractor = new FileExtractor(backupFile);

                                try {
                                    fileExtractor.addToArchive(zipOutputStream);
                                } catch (BackupReadException | NotUnlockedException | UnsupportedCryptoException | IOException ex) {
                                    logger.log(Level.SEVERE, "Can't add backup file " + backupFile.relativePath + " to archive :" + ex);
                                }
                                                                
                                break;
                        }

                        zipOutputStream.closeEntry();

                        backupExtractPercent = (i * 100) / count;
                        i++;
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, "Add entry to Zip archive problem: " + ex);
                    }
                }

                processing = false;
            }
        });

        thread.start();

    }

    /**
     * Extracts files from iOS backup
     * and save in folder on the disk.
     *
     * @param backupFiles List BackupFiles that will be extracted.
     * @param destinationDirectory The folder where the BackupFile will be unpacked.
     */        
    private void extractFilesToFolder(final List<BackupFile> backupFiles, final File destinationDirectory) {
        
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int count = backupFiles.size();

                int i = 0;
                for (BackupFile backupFile : backupFiles) {

                    String relative = Paths.get(backupFile.domain, backupFile.relativePath).toString();
                    File destination = new File(destinationDirectory.getAbsolutePath(), relative);                    

                    try {
                        Files.createDirectories(destination.getParentFile().toPath());
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }

                    switch (backupFile.getFileType()) {
                        case SYMBOLIC_LINK:
                            logger.log(Level.WARNING, "Symbolic link " + backupFile.relativePath + " not save");                            
                            break;
                        case DIRECTORY:
                            if (!destination.exists()) {
                                try {
                                    Files.createDirectory(destination.toPath());
                                } catch (IOException ex) {
                                    Exceptions.printStackTrace(ex);
                                }
                            }
                            break;
                        case FILE:
                            if (destination.exists()) {
                                destination = new File(destination.getAbsolutePath() + "_Id." + System.currentTimeMillis());
                                logger.log(Level.WARNING, "Duplicate name: " + relative + ", file renamed to " + destination.getName());
                            }
                            
                            FileExtractor fileExtractor = new FileExtractor(backupFile);

                            try {
                                fileExtractor.extractToFile(destination);
                            } catch (BackupReadException | NotUnlockedException | UnsupportedCryptoException | IOException ex) {
                                logger.log(Level.SEVERE, "Can't extract backup file " + backupFile.relativePath +" :" + ex);
                            }
                            break;
                    }

                    backupExtractPercent = (i * 100 / count);
                    i++;
                }
                processing = false;
            }
        });

        thread.start();
    }
             
    /**
     * @return Backup creation percentage.
     */
    public int getBackupCreatePercent() {
        int percent;
                
        String textLog = outputStream.toString();
        Matcher matcher = pattern.matcher(textLog);
        
        String lastFinishedLine = null;
        while (matcher.find()) {
            lastFinishedLine = matcher.group(1);
        }
        
        try {
            percent = Integer.valueOf(lastFinishedLine);
        } catch (NumberFormatException e) {
            percent = 0;
        }

        return percent;
    }
    
    /**
     * @return iOS Device parameters string eg. DeviceName, ProductName, 
     * ProductVersion, UniqueDeviceID.
     */
    private StringBuilder getDeviceParameters(String text) {
        StringBuilder deviceParameters = new StringBuilder();
       
        deviceName = getParameterValue("DeviceName: ", text);
        deviceParameters.append("DeviceName: ").append(deviceName).append("\n");
        deviceProductName = getParameterValue("ProductName: ", text);
        deviceParameters.append("ProductName: ").append(deviceProductName).append("\n");
        deviceProductVersion = getParameterValue("ProductVersion: ", text);
        deviceParameters.append("ProductVersion: ").append(deviceProductVersion).append("\n");
        uniqueDeviceID = getParameterValue("UniqueDeviceID: ", text);
        deviceParameters.append("UniqueDeviceID: ").append(uniqueDeviceID).append("\n");

        return deviceParameters;
    }
       
    /**
     * @return iOS device parameter form text result
     * of the command operation.
     */    
    private String getParameterValue(String parameterName, String text){         
        String newText = text.replace("\r", "");
        int beginIndex = newText.indexOf(parameterName);
        if(beginIndex != -1){
            beginIndex += parameterName.length();
            return newText.substring(beginIndex, newText.indexOf("\n", beginIndex));
        }                            
        
        return null;        
    }
    
    /**
     * Executes shell commands.
     *
     * @param cmdArray The command array string to be executed.
     * @param out Result of the command operation.
     */
    private void executeCommand(String[] cmdArray, OutputStream out) {
        if(filesCommandPath != null){
            cmdArray[0] = filesCommandPath.getAbsolutePath() + File.separator + cmdArray[0];
        }       
        
        Process process;
        try {
            process = Runtime.getRuntime().exec(cmdArray);
        } catch (IOException ex) {
            processing = false;
            ex.printStackTrace();
            return;
        }

        StreamTransfer outReader = new StreamTransfer(process.getInputStream(), out);
        outReader.start();
        
        try {
            if (out != null) {
                process.waitFor();
                outReader.join();
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * @return True if extraction is in progress, false if completed.
     */    
    public boolean isProcessing() {
        return processing;
    }
    
    /**
     * @return Backup extract percent.
     */
    public int getBackupExtractPercent() {
        return backupExtractPercent;
    }

    public String getOutputStream() {
        return outputStream.toString();
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getProductName() {
        return deviceProductName;
    }

    public String getProductVersion() {
        return deviceProductVersion;
    }

    public String getUniqueDeviceID() {
        return uniqueDeviceID;
    }

    public String getBackupInfo() {
        return backupInfo;
    }

    public File getBackupDirectory() {
        return backupDirectory;
    }

    public File getExtractDirectory() {
        return extractDirectory;
    }

    private File getFilesCommandPath() {
        String versionOS = System.getProperty("os.name").toLowerCase();
        File filePath = null;
        if (versionOS.contains("windows")) {
            filePath = InstalledFileLocator.getDefault().locate("win", IOSDataUnpacker.class.getPackage().getName(), false);
        } 
        return filePath;                
    }
}