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
package org.roussev.http4e.httpclient.ui.dialog.example;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.roussev.http4e.httpclient.ui.actions.AboutDialog;


/**
 * This class demonstrates JFace's TitleAreaDialog class
 * 
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class ShowMyTitleAreaDialog extends ApplicationWindow {
   /**
    * ShowCustomDialog constructor
    */
   public ShowMyTitleAreaDialog() {
      super(null);
   }

   /**
    * Runs the application
    */
   public void run(){
      // Don't return from open() until window closes
      setBlockOnOpen(true);

      // Open the main window
      open();

      // Dispose the display
      Display.getCurrent().dispose();
   }

   /**
    * Creates the main window's contents
    * 
    * @param parent
    *           the main window
    * @return Control
    */
   protected Control createContents( Composite parent){
      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(new GridLayout(10, true));

      // Create the button
      Button show = new Button(composite, SWT.NONE);
      show.setText("Show");

      final Shell shell = parent.getShell();

      // Display the TitleAreaDialog
      show.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected( SelectionEvent event){
            // Create and show the dialog
            AboutDialog dlg = new AboutDialog(shell);
            dlg.open();
         }
      });

      parent.pack();
      return composite;
   }

   /**
    * The application entry point
    * 
    * @param args
    *           the command line arguments
    */
   public static void main( String[] args){
      new ShowMyTitleAreaDialog().run();
   }
}