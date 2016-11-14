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
package org.roussev.http4e.httpclient.core.client.view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreImages;
import org.roussev.http4e.httpclient.core.client.model.ItemModel;
import org.roussev.http4e.httpclient.core.client.model.Model;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
class ParamsAttachManager {

   // private final Menu menu;
   // private final ToolItem addBody;
   private final ToolBar  toolBar;
   private final ToolItem i_open;
   private boolean        isMultipart = false;


   // private final StyledText swtText;
   // private final MenuItem m_attachPart;

   ParamsAttachManager( final ItemModel model, final StyledText styledText, final ToolBar toolbar) {
      // this.swtText = swtText;
      this.toolBar = toolbar;

      ParamsOpen open = new ParamsOpen(this, model, styledText);

      i_open = new ToolItem(toolBar, SWT.PUSH);
      i_open.setImage(ResourceUtils.getImage(CoreConstants.PLUGIN_CORE, CoreImages.FILE_OPEN));
      i_open.setDisabledImage(ResourceUtils.getImage(CoreConstants.PLUGIN_CORE, CoreImages.FILE_OPEN_DIS));
      i_open.setToolTipText("Add File");
      i_open.addSelectionListener(open);
   }


   public void setEnabled( boolean enabled){
      i_open.setEnabled(enabled);
   }


   public boolean isMultipart(){
      return isMultipart;
   }


   public void setMultipart( boolean isMultipart){
      this.isMultipart = isMultipart;
   }

}

class ParamsOpen implements SelectionListener {

   private StyledText          st;
   private Model               model;
   private ParamsAttachManager manager;


   public ParamsOpen( ParamsAttachManager manager, ItemModel model, StyledText st) {
      this.st = st;
      this.model = model;
      this.manager = manager;
   }


   public void widgetSelected( SelectionEvent event){
      FileDialog fd = new FileDialog(st.getShell(), SWT.OPEN);
      fd.setText("Add File Parameter");
      fd.setFilterExtensions(CoreConstants.FILE_FILTER_EXT);
      String file = fd.open();

      if (file != null) {
         if (manager.isMultipart()) {
            st.setText(st.getText() + CoreConstants.FILE_PREFIX + file);
         } else {
            try {
               st.setText(readFileAsString(file));
            } catch (IOException e) {
               // ignore
            }
         }
         // model.fireExecute(new ModelEvent(ModelEvent.BODY_FOCUS_LOST,
         // model));
         // // force body to refresh itself
         // model.fireExecute(new ModelEvent(ModelEvent.PARAMS_FOCUS_LOST,
         // model));
      }
   }


   private static String readFileAsString( String filePath) throws java.io.IOException{
      StringBuilder fileData = new StringBuilder(1000);
      BufferedReader reader = new BufferedReader(new FileReader(filePath));
      char[] buf = new char[1024];
      int numRead = 0;
      while ((numRead = reader.read(buf)) != -1) {
         String readData = String.valueOf(buf, 0, numRead);
         fileData.append(readData);
         buf = new char[1024];
      }
      reader.close();
      return fileData.toString();
   }


   public void widgetDefaultSelected( SelectionEvent event){
   }

}
