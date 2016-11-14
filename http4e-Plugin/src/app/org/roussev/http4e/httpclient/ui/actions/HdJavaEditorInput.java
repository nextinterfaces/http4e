/*
 *  Copyright 2017 Eclipse HttpClient (http4e) http://nextinterfaces.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.roussev.http4e.httpclient.ui.actions;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.internal.ide.AboutInfo;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;

/** 
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class HdJavaEditorInput implements IEditorInput {
   
    private AboutInfo aboutInfo;

    private final static String FACTORY_ID = "org.eclipse.ui.internal.dialogs.WelcomeEditorInputFactory";

    public final static String FEATURE_ID = "featureId";

    /**
     * WelcomeEditorInput constructor comment.
     */
    public HdJavaEditorInput(AboutInfo info) {
        super();
        if (info == null) {
            throw new IllegalArgumentException();
        }
        aboutInfo = info;
    }

    public boolean exists() {
        return false;
    }

    public Object getAdapter(Class adapter) {
        return null;
    }

    public ImageDescriptor getImageDescriptor() {
        return null;
    }

    public String getName() {
        return IDEWorkbenchMessages.WelcomeEditor_title;
    }

    public IPersistableElement getPersistable() {
        return new IPersistableElement() {
            public String getFactoryId() {
                return FACTORY_ID;
            }

            public void saveState(IMemento memento) {
                memento.putString(FEATURE_ID, aboutInfo.getFeatureId() + ':'
                        + aboutInfo.getVersionId());
            }
        };
    }

    public AboutInfo getAboutInfo() {
        return aboutInfo;
    }

    public boolean equals(Object o) {
        if ((o != null) && (o instanceof HdJavaEditorInput)) {
            if (((HdJavaEditorInput) o).aboutInfo.getFeatureId().equals(
                    aboutInfo.getFeatureId())) {
                return true;
            }
        }
        return false;
    }

    public String getToolTipText() {
        return NLS.bind(IDEWorkbenchMessages.WelcomeEditor_toolTip, aboutInfo.getFeatureLabel());
    }
}