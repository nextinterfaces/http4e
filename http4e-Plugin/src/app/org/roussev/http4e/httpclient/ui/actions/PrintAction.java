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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.ViewPart;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreImages;
import org.roussev.http4e.httpclient.core.ExceptionHandler;
import org.roussev.http4e.httpclient.core.client.model.ItemModel;
import org.roussev.http4e.httpclient.core.client.view.FolderView;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;
import org.roussev.http4e.httpclient.core.util.shared.PrinterFacade;
import org.roussev.http4e.httpclient.ui.HdViewPart;

/** 
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class PrintAction extends Action {

   private ViewPart view;

   public PrintAction( ViewPart view, String title) {
      super();
      this.view = view;

      setText(title);
      setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, CoreImages.HELP)));
      // setDisabledImageDescriptor(PDEPluginImages.DESC_PLUGIN_OBJ);

      setDescription("Print Http Packet");
      setToolTipText("Print Http Packet");
   }

   public void run(){
      print(view.getViewSite().getShell());
   }

   void print( Shell shell){
      PrintDialog printDialog = new PrintDialog(shell, SWT.NONE);
      printDialog.setText("Print Http Packets");
      PrinterData printerData = printDialog.open();
      if (!(printerData == null)) {
         final Printer printer = new Printer(printerData);
         
         Thread printingThread = new Thread("Printing") {
            public void run(){
               try {
                  new PrinterFacade(getTextToPrint(), printer).print();
               } catch (Exception e) {
                  ExceptionHandler.handle(e);
               } finally {
                  printer.dispose();
               }
            }
         };
         printingThread.start();         
      }
   }
   
   private String getTextToPrint(){
      StringBuilder sb = new StringBuilder();
      
      FolderView folderView = ((HdViewPart)view).getFolderView();
      ItemModel itemModel = folderView.getModel().getItemModel( new Integer(folderView.getSelectionItemHash()));
      sb.append("----------HTTP Request:---------\n");
      sb.append(itemModel.getRequest());
      sb.append("\n\n----------HTTP Response:--------\n");
      sb.append(itemModel.getResponse());
      
      return sb.toString();
   }

}
