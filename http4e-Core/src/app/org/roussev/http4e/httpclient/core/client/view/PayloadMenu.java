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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.roussev.http4e.httpclient.core.ExceptionHandler;
import org.roussev.http4e.httpclient.core.util.SwtUtils;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
class PayloadMenu {

   private String filename;


   public String getFilename(){
      return filename;
   }


   public void setFilename( String filename){
      this.filename = filename;
   }


   PayloadMenu( final StyledText styledText, Menu menu) {

      styledText.addKeyListener(new KeyAdapter() {

         public void keyPressed( KeyEvent e){
            if (e.stateMask == SWT.CTRL || e.stateMask == SWT.COMMAND) {
               if (e.character == 1) { // ^A
                  styledText.selectAll();
               } else if (e.character == 3) { // ^C
                  styledText.copy();
               } else if (e.character == 111) { // ^O
                  openFile(getFilename());
               }
            }
         }
      });

      MenuItem copyItem = new MenuItem(menu, SWT.PUSH);
      boolean isMac = SwtUtils.isMac();
      String ctrlName = isMac ? "CMD" : "CTRL";
      copyItem.setText("Copy   " + ctrlName + "+C");
      copyItem.addListener(SWT.Selection, new Listener() {

         public void handleEvent( Event event){
            styledText.copy();
         }
      });
      MenuItem payloadItem = new MenuItem(menu, SWT.PUSH);
      payloadItem.setText("View in Text editor   " + ctrlName + "+O");
      payloadItem.addListener(SWT.Selection, new Listener() {

         public void handleEvent( Event event){
            openFile(getFilename());
         }
      });
   }


   public static void openFile( String fileName){
      if (Desktop.isDesktopSupported()) {
         Desktop desktop = Desktop.getDesktop();
         try {
            fileName = fileName.replace('\\', '/');
            File f = new File(fileName);
            if (f.exists()) {
               desktop.open(f);
            }
         } catch (IOException e) {
            ExceptionHandler.handle(e);
         }
      }
   }

}
