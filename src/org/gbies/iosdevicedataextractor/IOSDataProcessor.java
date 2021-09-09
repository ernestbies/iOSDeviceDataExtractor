/*
 * Autopsy Forensic Browser
 *
 * Copyright 2011-2016 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * iOS Device Data Extractor (Autopsy module), version  1.0
 *
 */
package org.gbies.iosdevicedataextractor;

import javax.swing.JPanel;
import org.sleuthkit.autopsy.corecomponentinterfaces.DataSourceProcessor;
import org.sleuthkit.autopsy.corecomponentinterfaces.DataSourceProcessorCallback;
import org.sleuthkit.autopsy.corecomponentinterfaces.DataSourceProcessorProgressMonitor;
import org.openide.util.lookup.ServiceProvider;
import org.sleuthkit.datamodel.Host;

@ServiceProvider(service = DataSourceProcessor.class)
public class IOSDataProcessor implements DataSourceProcessor {

    private final String modulename = "Extraction data from iOS device or iTunes backup";
    private final IOSDataProcessorPanel processorPanel;

    public IOSDataProcessor() {
        processorPanel = new IOSDataProcessorPanel();
    }

    @Override
    public String getDataSourceType() {
        return modulename;
    }

    @Override
    public JPanel getPanel() {
        return processorPanel;
    }

    @Override
    public boolean isPanelValid() {
        return processorPanel.validatePanel();
    }

    @Override
    public void run(DataSourceProcessorProgressMonitor progressMonitor, DataSourceProcessorCallback callback) {
        new Thread(new addDeviceDataTask(processorPanel.getPanelSettings(), progressMonitor, callback)).start();
    }

    @Override
    public void run(Host host, DataSourceProcessorProgressMonitor progressMonitor, DataSourceProcessorCallback callback) {
        new Thread(new addDeviceDataTask(host, processorPanel.getPanelSettings(), progressMonitor, callback)).start();
    }

    @Override
    public void cancel() {

    }

    @Override
    public void reset() {
        processorPanel.resetPanel();
    }

}
