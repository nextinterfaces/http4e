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

import java.io.StringWriter;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreImages;
import org.roussev.http4e.httpclient.core.ExceptionHandler;
import org.roussev.http4e.httpclient.core.client.model.ItemModel;
import org.roussev.http4e.httpclient.core.client.model.ModelEvent;
import org.roussev.http4e.httpclient.core.client.view.FolderView;
import org.roussev.http4e.httpclient.core.util.BaseUtils;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;
import org.roussev.http4e.httpclient.ui.HdViewPart;

public class ExportCsAction extends Action {

   private ViewPart view;
   private Menu     fMenu;


   public ExportCsAction( ViewPart view) {
      this.view = view;
      fMenu = null;
      setToolTipText("Export call as C# script");
      setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, CoreImages.CSHARP)));
      setText("     C#");
   }


   public void dispose(){
      // action is reused, can be called several times.
      if (fMenu != null) {
         fMenu.dispose();
         fMenu = null;
      }
   }


   // protected void addActionToMenu( Menu parent, Action action){
   // ActionContributionItem item = new ActionContributionItem(action);
   // item.fill(parent, -1);
   // }

   public void run(){
      try {
         FolderView folderView = ((HdViewPart) view).getFolderView();
         ItemModel iModel = folderView.getModel().getItemModel(new Integer(folderView.getSelectionItemHash()));
         iModel.fireExecute(new ModelEvent(ModelEvent.EXPORT, iModel));

         StringWriter writer = new StringWriter();
         BaseUtils.writeCsharp(iModel, writer);
         
         ExportDialog dialog = new ExportDialog(view, "C# HTTP client", 
               "Your call exported as a C# HTTP client code.",
               writer.toString());
         dialog.open();
      } catch (Exception e) {
         ExceptionHandler.handle(e);
      }
   }

}
