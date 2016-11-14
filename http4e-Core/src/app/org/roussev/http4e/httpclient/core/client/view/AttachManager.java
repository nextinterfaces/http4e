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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
import org.roussev.http4e.httpclient.core.client.model.ModelEvent;
import org.roussev.http4e.httpclient.core.misc.CoreException;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
class AttachManager {

//   private final Menu         menu;
//   private final ToolItem     addBody;
   private final ToolBar      toolBar;
   private final ToolItem i_open;
//   private final StyledText   swtText;
//   private final MenuItem     m_attachPart;

   AttachManager( final ItemModel model, final StyledText swtText, final ToolBar toolbar) {
//      this.swtText = swtText;
      this.toolBar = toolbar;

      Open open = new Open(model, swtText);
      
      i_open = new ToolItem(toolBar, SWT.PUSH);
      i_open.setImage(ResourceUtils.getImage(CoreConstants.PLUGIN_CORE, CoreImages.FILE_OPEN));
      i_open.setDisabledImage(ResourceUtils.getImage(CoreConstants.PLUGIN_CORE, CoreImages.FILE_OPEN_DIS));
      i_open.setToolTipText("Add File");
      i_open.addSelectionListener(open);
//      i_open.addSelectionListener(new SelectionAdapter() {
//         public void widgetSelected( SelectionEvent e){
//         }
//      });
      
//      // Menu(bar)
//      menu = new Menu(toolBar.getShell(), SWT.POP_UP);
//
//      MenuItem m_addBody = new MenuItem(menu, SWT.PUSH);
//      m_addBody.setText("Add File");
//      m_addBody.addSelectionListener(open);
//      m_attachPart = new MenuItem(menu, SWT.PUSH);
//      m_attachPart.setText("Attach File");
//      m_attachPart.addSelectionListener(open);
//
//      addBody = new ToolItem(toolBar, SWT.DROP_DOWN);
//      addBody.setImage(ResourceUtils.getImage(CoreImages.ATTACH));
//      addBody.setToolTipText("Add Body");
//      addBody.addListener(SWT.Selection, new Listener() {
//         public void handleEvent( Event event){
//            if (event.detail == SWT.ARROW) {
//               Rectangle rect = addBody.getBounds();
//               menu.setLocation(toolBar.toDisplay(rect.x, rect.y + rect.height));
//               menu.setVisible(true);
//            } else {
//               FileDialog fd = new FileDialog(toolBar.getShell(), SWT.OPEN);
//               fd.setText("Open");
//               // fd.setFilterPath("C:/");
//               fd.setFilterExtensions(CoreConstants.FILE_FILTER_EXT);
//               String file = fd.open();
//               if (file != null) {
//                  fileReadAction(file);
//               }
//            }
//         }
//      });
   }

   public void setEnabled( boolean enabled){
      i_open.setEnabled(enabled);
   }

//   public void setMultipartEnabled( boolean enabled){
//      m_attachPart.setEnabled(enabled);
//   }

}




class Open implements SelectionListener {
   private StyledText parent;
   private Model model;
   public Open(ItemModel model, StyledText parent){
      this.parent = parent;
      this.model = model;
   }
   
   public void widgetSelected( SelectionEvent event){
      FileDialog fd = new FileDialog(parent.getShell(), SWT.OPEN);
      fd.setText("Add File Content to Body");
      // fd.setFilterPath("C:/");
      fd.setFilterExtensions(CoreConstants.FILE_FILTER_EXT);
      String file = fd.open();
      
      if (file != null) {
         parent.setText(CoreConstants.FILE_PREFIX + file);
         model.fireExecute(new ModelEvent(ModelEvent.BODY_FOCUS_LOST, model));
//       force body to refresh itself
         model.fireExecute(new ModelEvent(ModelEvent.PARAMS_FOCUS_LOST, model)); 
      }
   }

   public void widgetDefaultSelected( SelectionEvent event){
   }
   
}
