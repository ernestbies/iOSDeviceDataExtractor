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

import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import org.gbies.iosbackupextractor.BackupReadException;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.autopsy.casemodule.services.FileManager;
import org.sleuthkit.autopsy.corecomponentinterfaces.DataSourceProcessorCallback;
import org.sleuthkit.autopsy.corecomponentinterfaces.DataSourceProcessorProgressMonitor;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.autopsy.ingest.IngestServices;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.Content;
import org.sleuthkit.datamodel.Host;
import org.sleuthkit.datamodel.LocalFilesDataSource;
import org.sleuthkit.datamodel.TskCoreException;
import org.sleuthkit.datamodel.TskDataException;


public class addDeviceDataTask implements Runnable {
    public static final IngestServices ingestServices = IngestServices.getInstance();
    public static final Logger logger = ingestServices.getLogger("iOSDeviceDataExtractor");    
    private final DataSourceProcessorProgressMonitor progressMonitor;
    private final DataSourceProcessorCallback callbackObj;
    private final Host host;
    private boolean hasCriticalError = false;
    private final List<String> errorList = new ArrayList<>();
    private final IOSDataProcessorPanelSettings panelSettings;
    private final List<String> localFilePaths = new ArrayList<>();
    private final List<Content> newDataSources = new ArrayList<>();
    
    public addDeviceDataTask(Host host, IOSDataProcessorPanelSettings panelSettings, DataSourceProcessorProgressMonitor aProgressMonitor, DataSourceProcessorCallback cbObj){
        this.host = host;
        this.progressMonitor = aProgressMonitor;
        this.callbackObj = cbObj;
        this.panelSettings = panelSettings;        
    }
    
    public addDeviceDataTask(IOSDataProcessorPanelSettings panelSettings, DataSourceProcessorProgressMonitor aProgressMonitor, DataSourceProcessorCallback cbObj){
        this.host = null;
        this.progressMonitor = aProgressMonitor;
        this.callbackObj = cbObj;
        this.panelSettings = panelSettings;        
    }
    
    @Messages({
    "addDeviceDataTask.creating.encrypted.backup=Creating encrypted backup...",
    "addDeviceDataTask.creating.unencrypted.backup=Creating unencrypted backup...",
    "addDeviceDataTask.reading.backup.information=Reading information from backup...",
    "addDeviceDataTask.extracting.backup=Extracting backup...",
    "addDeviceDataTask.connect.problem=iOS device connection problem, ",
    "addDeviceDataTask.manifest.file.not.created=Backup problem (Manifest.db file not found), ",
    "addDeviceDataTask.error.add.files.dataSources=Error add files to new DataSources",
    "addDeviceDataTask.error.extract.backup=Incorrect password, backup decryption problem or backup directory not found!"})
    @Override
    public void run() {
        errorList.clear();               
        IOSDataUnpacker iosDataUnpacker = new IOSDataUnpacker();        
        StringBuilder deviceInfo = new StringBuilder();
        
        if (panelSettings.isLiveExtraction()) {
            
            if (iosDataUnpacker.checkDevice()) {
                               
                logger.log(Level.INFO, "Connecting device... " + iosDataUnpacker.getDeviceInfo());
                
                try {
                    iosDataUnpacker.createBackup(panelSettings.getExtractDirectoryName(), panelSettings.isBackupEncrypted());
                } catch (UnpackDataException ex) {
                    Exceptions.printStackTrace(ex);
                }
                String backupType = Bundle.addDeviceDataTask_creating_encrypted_backup();
                if(!panelSettings.isBackupEncrypted()){
                    backupType = Bundle.addDeviceDataTask_creating_unencrypted_backup();
                } 
                
                deviceInfo.append(iosDataUnpacker.getDeviceInfo()).append(backupType);
                logger.log(Level.INFO, backupType);
                progressMonitor.setProgressText(deviceInfo.toString());
                progressMonitor.setIndeterminate(false);
                progressMonitor.setProgressMax(100);
                
                int percent;
                do {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }   
                    
                    percent = iosDataUnpacker.getBackupCreatePercent();                    
                    progressMonitor.setProgress(percent);                    
                } while (iosDataUnpacker.isProcessing());
                logger.log(Level.INFO, iosDataUnpacker.getOutputStream());
                
                File manifestDBFile = new File(iosDataUnpacker.getBackupDirectory(), "Manifest.db");
                File manifestPListFile = new File(iosDataUnpacker.getBackupDirectory(), "Manifest.plist");
                
                if(!manifestPListFile.exists() || !manifestDBFile.exists()){
                    errorList.add(Bundle.addDeviceDataTask_manifest_file_not_created() + iosDataUnpacker.getOutputStream());
                    logger.log(Level.SEVERE, Bundle.addDeviceDataTask_manifest_file_not_created());
                    hasCriticalError = true;
                }
             
            } else {
                errorList.add(Bundle.addDeviceDataTask_connect_problem());
                logger.log(Level.SEVERE, Bundle.addDeviceDataTask_connect_problem() + iosDataUnpacker.getDeviceInfo());
                hasCriticalError = true;
            }

        }

        if (!hasCriticalError) {
            progressMonitor.setProgressText(Bundle.addDeviceDataTask_reading_backup_information());
            progressMonitor.setIndeterminate(true);
            
            try {
                if (panelSettings.isLiveExtraction()) {
                    iosDataUnpacker.extractBackup(panelSettings.isExtractToZip());
                } else {
                    iosDataUnpacker.extractBackup(panelSettings.getExtractDirectoryName(), panelSettings.getPassword(), panelSettings.isExtractToZip());
                }
            } catch (FileNotFoundException | BackupReadException | InvalidKeyException ex) {
                errorList.add(Bundle.addDeviceDataTask_error_extract_backup());
                logger.log(Level.SEVERE, Bundle.addDeviceDataTask_error_extract_backup());
                hasCriticalError = true;
            }

            if (!hasCriticalError) {
                StringBuilder backupInfo = new StringBuilder();
                backupInfo.append(iosDataUnpacker.getBackupInfo()).append(Bundle.addDeviceDataTask_extracting_backup());
                logger.log(Level.INFO, backupInfo.toString());
                progressMonitor.setProgressText(backupInfo.toString());
                progressMonitor.setIndeterminate(false);
                progressMonitor.setProgressMax(100);

                int percent;
                do {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }

                    percent = iosDataUnpacker.getBackupExtractPercent();
                    progressMonitor.setProgress(percent);
                } while (iosDataUnpacker.isProcessing());
                
                logger.log(Level.INFO, "Files extraction from iOS backup complete.");

                localFilePaths.add(iosDataUnpacker.getExtractDirectory().getAbsolutePath());
                FileManager fileManager = Case.getCurrentCase().getServices().getFileManager();
                LocalFilesDataSource newDataSource;
                try {
                    String newDataSourceName = iosDataUnpacker.getDeviceName() + " " + iosDataUnpacker.getProductVersion() + " (" + iosDataUnpacker.getUniqueDeviceID() + ")";
                    newDataSource = fileManager.addLocalFilesDataSource(UUID.randomUUID().toString(), newDataSourceName, "", host, localFilePaths, new ProgressUpdater());
                    newDataSources.add(newDataSource);
                } catch (TskCoreException | TskDataException ex) {
                    errorList.add(Bundle.addDeviceDataTask_error_add_files_dataSources());
                    logger.log(Level.SEVERE, Bundle.addDeviceDataTask_error_add_files_dataSources());
                    hasCriticalError = true;                    
                }
            }
        }
                
        doCallBack();
    }

    private void doCallBack() {
        DataSourceProcessorCallback.DataSourceProcessorResult result;

        if (hasCriticalError) {
            result = DataSourceProcessorCallback.DataSourceProcessorResult.CRITICAL_ERRORS;            
        } else if (!errorList.isEmpty()) {
            result = DataSourceProcessorCallback.DataSourceProcessorResult.NONCRITICAL_ERRORS;
        } else {
            result = DataSourceProcessorCallback.DataSourceProcessorResult.NO_ERRORS;
        }
        callbackObj.done(result, errorList, newDataSources);
    }
            
    private class ProgressUpdater implements FileManager.FileAddProgressUpdater {
        private int count;
        
        @Override
        public void fileAdded(AbstractFile file) {
            count++;
            if(count % 10 == 0){               
                progressMonitor.setProgressText(NbBundle.getMessage(this.getClass(), "addDeviceDataTask.localFileAdd.progress.text", file.getParentPath(), file.getName()));           
            }
        }
    }
        
}
