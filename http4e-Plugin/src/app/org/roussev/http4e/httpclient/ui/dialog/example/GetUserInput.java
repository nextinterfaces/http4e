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

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * @deprecated
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class GetUserInput extends ApplicationWindow {

   public GetUserInput() {
      super(null);
   }

   public void run(){
      // Don't return from open() until window closes
      setBlockOnOpen(true);

      // Open the main window
      open();

      // Dispose the display
      Display.getCurrent().dispose();
   }

   protected void configureShell( Shell shell){
      super.configureShell(shell);

      // Set the title bar text
      shell.setText("Get Input");
   }

   protected Control createContents( Composite parent){
      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(new GridLayout(1, false));

      // Create a label to display what the user typed in
      final Label label = new Label(composite, SWT.NONE);
      label.setText("This will display the user input from InputDialog");

      // Create the button to launch the error dialog
      Button show = new Button(composite, SWT.PUSH);
      show.setText("Get Input");
      show.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected( SelectionEvent event){
            InputDialog dlg = new InputDialog(
                  Display.getCurrent().getActiveShell(), 
                  "Enter Input", "Enter Input", 
                  label.getText(), new LengthValidator());
            if (dlg.open() == Window.OK) {
               // User clicked OK; update the label with the input
               label.setText(dlg.getValue());
            }
         }
      });

      parent.pack();
      return composite;
   }

   public static void main( String[] args){
      new GetUserInput().run();
   }
}

class LengthValidator implements IInputValidator {

   public String isValid( String newText){
      int len = newText.length();

      if (newText.equals("hellohttp4e"))
         return "Thank you. Enjoy your product.";
      if (len > 8)
         return "Http4e couldn't identify your password";

      // Input must be OK
      return null;
   }
}