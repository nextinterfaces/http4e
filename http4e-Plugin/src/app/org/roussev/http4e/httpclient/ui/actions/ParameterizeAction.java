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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreContext;
import org.roussev.http4e.httpclient.core.CoreObjects;
import org.roussev.http4e.httpclient.core.ExceptionHandler;
import org.roussev.http4e.httpclient.core.client.model.ModelEvent;
import org.roussev.http4e.httpclient.core.client.view.FolderView;
import org.roussev.http4e.httpclient.ui.HdViewPart;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class ParameterizeAction extends Action {

   private HdViewPart         view;
   private ParameterizeDialog dialog;


   public ParameterizeAction( HdViewPart view) {
      super();
      this.view = view;
      setText("Parameterize your call");
      setDescription("Parameterize your call");
      setToolTipText("Parameterize your call");
   }


   // public void dispose(){
   // // action is reused, can be called several times.
   // if (fMenu != null) {
   // fMenu.dispose();
   // fMenu = null;
   // }
   // }

   public void run(){
      if (dialog == null) {
         dialog = new ParameterizeDialog(view);
      }
      try {
         MouseAdapter okListener = new MouseAdapter() {

            public void mouseUp( MouseEvent e){
               String dialogText = dialog.getText();
               Properties props = new Properties();

               try {
                  InputStream is = new ByteArrayInputStream(dialogText.getBytes("UTF-8"));
                  props.load(is);
               } catch (IOException e1) {
                  ExceptionHandler.handle(e1);
               }

               CoreContext ctx = CoreContext.getContext();
               ctx.putObject(CoreObjects.PARAMETERIZE_ARGS, props);
               FolderView folderView = ((HdViewPart) view).getFolderView();
               folderView.getModel().fireExecute(new ModelEvent(ModelEvent.PARAMETERIZE_CHANGE, CoreConstants.NULL_MODEL));
               // ItemModel iModel = new ItemModel(folderView.getModel(), item);
               // HdViewPart hdViewPart = (HdViewPart) view;
               // hdViewPart.getFolderView().buildTab(iModel);
            }
         };
         dialog.setOkListener(okListener);
         dialog.open();

      } catch (Exception e) {
         ExceptionHandler.handle(e);
      }
   }

}
