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
import java.util.logging.Level;
import javax.swing.JFileChooser;
import org.openide.util.NbBundle.Messages;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.autopsy.coreutils.MessageNotifyUtil;
import static org.gbies.iosdeviceextractor.addDeviceDataTask.logger;

/**
 * Allows examiner to supply iOS device or backup data source.
 */
@Messages({
    "DeviceDataProcessorPanel.moduleErrorMessage.title=Module Error",
    "DeviceDataProcessorPanel.moduleErrorMessage.body=A module caused an error listening to DeviceDataProcessorPanel updates.",})
public class IOSDataProcessorPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private IOSDataProcessorPanelSettings panelSettings;
    private String defaultFolderExtraction;
    private boolean panelValid;

    public IOSDataProcessorPanel() {
        initComponents();
        customInit();
        setComponents(true);
    }

    private void customInit() {
        this.panelSettings = new IOSDataProcessorPanelSettings();
        this.defaultFolderExtraction = Case.getCurrentCase().getModuleDirectory() + File.separator + "iOSBackup";
        jTextFieldDefaultFolderExtraction.setText(defaultFolderExtraction);
        jTextFieldDefaultFolderExtraction.setVisible(false);
        jTextFieldBackupFolder.setEditable(false);
        jButtonFolderExtraction.setVisible(false);
        panelValid = true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelLiveExtraction = new javax.swing.JPanel();
        jRadioButtonLiveExtraction = new javax.swing.JRadioButton();
        jCheckBoxChangeDefaultFolder = new javax.swing.JCheckBox();
        jTextFieldDefaultFolderExtraction = new javax.swing.JTextField();
        jButtonFolderExtraction = new javax.swing.JButton();
        jCheckBoxBackupEncrypted = new javax.swing.JCheckBox();
        jPanelBackupExtraction = new javax.swing.JPanel();
        jRadioButtonBackupExtraction = new javax.swing.JRadioButton();
        jTextFieldBackupFolder = new javax.swing.JTextField();
        jButtonBackupFolder = new javax.swing.JButton();
        jLabelPassword = new javax.swing.JLabel();
        jPasswordField = new javax.swing.JPasswordField();
        jLabelDirectory = new javax.swing.JLabel();
        jCheckBoxExtractToZip = new javax.swing.JCheckBox();

        jPanelLiveExtraction.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jRadioButtonLiveExtraction.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jRadioButtonLiveExtraction, org.openide.util.NbBundle.getMessage(IOSDataProcessorPanel.class, "IOSDataProcessorPanel.jRadioButtonLiveExtraction.text")); // NOI18N
        jRadioButtonLiveExtraction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonLiveExtractionActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxChangeDefaultFolder, org.openide.util.NbBundle.getMessage(IOSDataProcessorPanel.class, "IOSDataProcessorPanel.jCheckBoxChangeDefaultFolder.text")); // NOI18N
        jCheckBoxChangeDefaultFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxChangeDefaultFolderActionPerformed(evt);
            }
        });

        jTextFieldDefaultFolderExtraction.setEditable(false);
        jTextFieldDefaultFolderExtraction.setText(org.openide.util.NbBundle.getMessage(IOSDataProcessorPanel.class, "IOSDataProcessorPanel.jTextFieldDefaultFolderExtraction.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonFolderExtraction, org.openide.util.NbBundle.getMessage(IOSDataProcessorPanel.class, "IOSDataProcessorPanel.jButtonFolderExtraction.text")); // NOI18N
        jButtonFolderExtraction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFolderExtractionActionPerformed(evt);
            }
        });

        jCheckBoxBackupEncrypted.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxBackupEncrypted, org.openide.util.NbBundle.getMessage(IOSDataProcessorPanel.class, "IOSDataProcessorPanel.jCheckBoxBackupEncrypted.text")); // NOI18N

        javax.swing.GroupLayout jPanelLiveExtractionLayout = new javax.swing.GroupLayout(jPanelLiveExtraction);
        jPanelLiveExtraction.setLayout(jPanelLiveExtractionLayout);
        jPanelLiveExtractionLayout.setHorizontalGroup(
            jPanelLiveExtractionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLiveExtractionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelLiveExtractionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLiveExtractionLayout.createSequentialGroup()
                        .addComponent(jCheckBoxChangeDefaultFolder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldDefaultFolderExtraction, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelLiveExtractionLayout.createSequentialGroup()
                        .addComponent(jRadioButtonLiveExtraction)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBoxBackupEncrypted)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonFolderExtraction)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelLiveExtractionLayout.setVerticalGroup(
            jPanelLiveExtractionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLiveExtractionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelLiveExtractionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonLiveExtraction)
                    .addComponent(jCheckBoxBackupEncrypted))
                .addGap(4, 4, 4)
                .addGroup(jPanelLiveExtractionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldDefaultFolderExtraction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonFolderExtraction)
                    .addComponent(jCheckBoxChangeDefaultFolder))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelBackupExtraction.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        org.openide.awt.Mnemonics.setLocalizedText(jRadioButtonBackupExtraction, org.openide.util.NbBundle.getMessage(IOSDataProcessorPanel.class, "IOSDataProcessorPanel.jRadioButtonBackupExtraction.text")); // NOI18N
        jRadioButtonBackupExtraction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonBackupExtractionActionPerformed(evt);
            }
        });

        jTextFieldBackupFolder.setText(org.openide.util.NbBundle.getMessage(IOSDataProcessorPanel.class, "IOSDataProcessorPanel.jTextFieldBackupFolder.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButtonBackupFolder, org.openide.util.NbBundle.getMessage(IOSDataProcessorPanel.class, "IOSDataProcessorPanel.jButtonBackupFolder.text")); // NOI18N
        jButtonBackupFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackupFolderActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabelPassword, org.openide.util.NbBundle.getMessage(IOSDataProcessorPanel.class, "IOSDataProcessorPanel.jLabelPassword.text")); // NOI18N

        jPasswordField.setText(org.openide.util.NbBundle.getMessage(IOSDataProcessorPanel.class, "IOSDataProcessorPanel.jPasswordField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabelDirectory, org.openide.util.NbBundle.getMessage(IOSDataProcessorPanel.class, "IOSDataProcessorPanel.jLabelDirectory.text")); // NOI18N

        javax.swing.GroupLayout jPanelBackupExtractionLayout = new javax.swing.GroupLayout(jPanelBackupExtraction);
        jPanelBackupExtraction.setLayout(jPanelBackupExtractionLayout);
        jPanelBackupExtractionLayout.setHorizontalGroup(
            jPanelBackupExtractionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBackupExtractionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBackupExtractionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelBackupExtractionLayout.createSequentialGroup()
                        .addComponent(jRadioButtonBackupExtraction)
                        .addGap(54, 54, 54)
                        .addComponent(jLabelPassword)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelBackupExtractionLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabelDirectory)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldBackupFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonBackupFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelBackupExtractionLayout.setVerticalGroup(
            jPanelBackupExtractionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBackupExtractionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBackupExtractionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonBackupExtraction)
                    .addComponent(jLabelPassword)
                    .addComponent(jPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelBackupExtractionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldBackupFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonBackupFolder)
                    .addComponent(jLabelDirectory))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBoxExtractToZip, org.openide.util.NbBundle.getMessage(IOSDataProcessorPanel.class, "IOSDataProcessorPanel.jCheckBoxExtractToZip.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jCheckBoxExtractToZip)
                    .addComponent(jPanelBackupExtraction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelLiveExtraction, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelLiveExtraction, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelBackupExtraction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, Short.MAX_VALUE)
                .addComponent(jCheckBoxExtractToZip)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButtonLiveExtractionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonLiveExtractionActionPerformed
        setComponents(true);        
    }//GEN-LAST:event_jRadioButtonLiveExtractionActionPerformed

    private void jRadioButtonBackupExtractionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonBackupExtractionActionPerformed
        setComponents(false);
    }//GEN-LAST:event_jRadioButtonBackupExtractionActionPerformed

    private void jCheckBoxChangeDefaultFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxChangeDefaultFolderActionPerformed
        if (jCheckBoxChangeDefaultFolder.isSelected()) {
            jTextFieldDefaultFolderExtraction.setText(defaultFolderExtraction);
            jTextFieldDefaultFolderExtraction.setVisible(true);
            jButtonFolderExtraction.setVisible(true);
        } else {
            jTextFieldDefaultFolderExtraction.setVisible(false);
            jButtonFolderExtraction.setVisible(false);
        }
    }//GEN-LAST:event_jCheckBoxChangeDefaultFolderActionPerformed

    private void jButtonFolderExtractionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFolderExtractionActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String directory = chooser.getSelectedFile().getAbsolutePath();
            jTextFieldDefaultFolderExtraction.setText(directory);
        }
    }//GEN-LAST:event_jButtonFolderExtractionActionPerformed

    private void jButtonBackupFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackupFolderActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String directory = chooser.getSelectedFile().getAbsolutePath();
            jTextFieldBackupFolder.setText(directory);
            jPasswordField.setEnabled(true);
            panelValid = true;
            fireUpdateEvent();
        }
    }//GEN-LAST:event_jButtonBackupFolderActionPerformed

    private void setComponents(boolean isLiveExtraction) {
        if (isLiveExtraction) {
            jRadioButtonLiveExtraction.setSelected(true);
            jRadioButtonBackupExtraction.setSelected(false);

            jPanelLiveExtraction.setEnabled(true);
            jCheckBoxChangeDefaultFolder.setEnabled(true);
            jTextFieldDefaultFolderExtraction.setEnabled(true);
            jCheckBoxBackupEncrypted.setEnabled(true);
            jButtonFolderExtraction.setEnabled(true);
            jTextFieldBackupFolder.setEnabled(false);
            jPanelBackupExtraction.setEnabled(false);
            jButtonBackupFolder.setEnabled(false);
            jPasswordField.setEnabled(false);
            jLabelPassword.setEnabled(false);
            panelValid = true;
        } else {
            jRadioButtonLiveExtraction.setSelected(false);
            jRadioButtonBackupExtraction.setSelected(true);

            jPanelLiveExtraction.setEnabled(false);
            jCheckBoxChangeDefaultFolder.setEnabled(false);
            jTextFieldDefaultFolderExtraction.setEnabled(false);
            jCheckBoxBackupEncrypted.setEnabled(false);
            jButtonFolderExtraction.setEnabled(false);
            jTextFieldBackupFolder.setEnabled(true);
            jPanelBackupExtraction.setEnabled(true);
            jButtonBackupFolder.setEnabled(true);
            jLabelPassword.setEnabled(true);
            if ("".equals(jTextFieldBackupFolder.getText())) {
                panelValid = false;
                jPasswordField.setEnabled(false);
            } else {
                panelValid = true;
                jPasswordField.setEnabled(true);
            }
        }
        fireUpdateEvent();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBackupFolder;
    private javax.swing.JButton jButtonFolderExtraction;
    private javax.swing.JCheckBox jCheckBoxBackupEncrypted;
    private javax.swing.JCheckBox jCheckBoxChangeDefaultFolder;
    private javax.swing.JCheckBox jCheckBoxExtractToZip;
    private javax.swing.JLabel jLabelDirectory;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JPanel jPanelBackupExtraction;
    private javax.swing.JPanel jPanelLiveExtraction;
    private javax.swing.JPasswordField jPasswordField;
    private javax.swing.JRadioButton jRadioButtonBackupExtraction;
    private javax.swing.JRadioButton jRadioButtonLiveExtraction;
    private javax.swing.JTextField jTextFieldBackupFolder;
    private javax.swing.JTextField jTextFieldDefaultFolderExtraction;
    // End of variables declaration//GEN-END:variables

    public boolean validatePanel() {
        return panelValid;
    }

    public void resetPanel() {
        setComponents(true);
        jTextFieldBackupFolder.setText("");
        jTextFieldDefaultFolderExtraction.setText(defaultFolderExtraction);
    }

    public IOSDataProcessorPanelSettings getPanelSettings() {

        panelSettings.setLiveExtraction(jRadioButtonLiveExtraction.isSelected());
        panelSettings.setPassword(new String(jPasswordField.getPassword()));
        if (jRadioButtonLiveExtraction.isSelected()) {
            panelSettings.setExtractDirectoryName(jTextFieldDefaultFolderExtraction.getText());
        } else {
            panelSettings.setExtractDirectoryName(jTextFieldBackupFolder.getText());
        }
        panelSettings.setExtractToZip(jCheckBoxExtractToZip.isSelected());
        panelSettings.setBackupEncrypted(jCheckBoxBackupEncrypted.isSelected());
        return panelSettings;
    }

    private void fireUpdateEvent() {
        try {
            firePropertyChange(IOSDataProcessor.DSP_PANEL_EVENT.UPDATE_UI.toString(), false, true);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "DeviceDataProcessorPanel listener threw exception", e);
            MessageNotifyUtil.Notify.show(Bundle.DeviceDataProcessorPanel_moduleErrorMessage_title(),
                    Bundle.DeviceDataProcessorPanel_moduleErrorMessage_body(),
                    MessageNotifyUtil.MessageType.ERROR);
        }
    }

}
