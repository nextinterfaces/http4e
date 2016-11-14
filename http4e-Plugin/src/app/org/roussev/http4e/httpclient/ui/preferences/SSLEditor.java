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
package org.roussev.http4e.httpclient.ui.preferences;

import org.apache.commons.lang.text.StrTokenizer;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 */
class SSLEditor extends ListEditor {

   private static final String   DELIMITER    = "#";
   private static final String[] FILTER_NAMES = { "All Files (*.*)" };
   private static final String[] FILTER_EXTS  = { "*.*" };


   public SSLEditor( String name, String labelText, Composite parent) {
      super(name, labelText, parent);
   }


   /*
    * (non-Javadoc)
    * 
    * @see org.eclipse.jface.preference.ListEditor#createList(java.lang.String[])
    */
   protected String createList( String[] items){
      StringBuilder returnvalue = new StringBuilder();
      for (String item : items) {
         returnvalue.append(item).append(DELIMITER);
      }
      if (returnvalue.length() > 0) {
         // remove last delimiter
         returnvalue.setLength(returnvalue.length() - DELIMITER.length());
      }
      return returnvalue.toString();
   }


   /*
    * (non-Javadoc)
    * 
    * @see org.eclipse.jface.preference.ListEditor#getNewInputObject()
    */
   protected String getNewInputObject(){
      String returnvalue = null;
      SSLInputDialog inputDialog = new SSLInputDialog(getShell());
      if (inputDialog.open() == InputDialog.OK) {
         // check for valid Input
         try {
            returnvalue = inputDialog.getName();
         } catch (Exception e) {
            MessageDialog.openError(getShell(), "Wrong entry", "Wrong entry");
         }
      }
      return returnvalue;
   }


   /*
    * (non-Javadoc)
    * 
    * @see org.eclipse.jface.preference.ListEditor#parseString(java.lang.String)
    */
   protected String[] parseString( String stringList){
      StrTokenizer tokenizer = new StrTokenizer(stringList, DELIMITER);
      return tokenizer.getTokenArray();
   }

   private class SSLInputDialog extends Dialog {

      // private Text name;
      private String nameString;
      private Text   fileName;


      protected SSLInputDialog( Shell parentShell) {
         super(parentShell);
      }


      @Override
      protected Control createDialogArea( Composite parent){
         getShell().setText("SSL Parameters");
         final Composite returnvalue = (Composite) super.createDialogArea(parent);

         returnvalue.setLayout(new GridLayout(5, true));

         new Label(returnvalue, SWT.NONE).setText("Keystore File Name:");

         fileName = new Text(returnvalue, SWT.BORDER);
         GridData data = new GridData(GridData.FILL_HORIZONTAL);
         data.horizontalSpan = 4;
         fileName.setLayoutData(data);

         Button open = new Button(returnvalue, SWT.PUSH);
         open.setText("Open...");
         open.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected( SelectionEvent event){
               FileDialog dlg = new FileDialog(returnvalue.getShell(), SWT.OPEN);
               dlg.setFilterNames(FILTER_NAMES);
               dlg.setFilterExtensions(FILTER_EXTS);
               String fn = dlg.open();
               if (fn != null) {
                  fileName.setText(fn);
               }
            }
         });

         return returnvalue;
      }


      @Override
      protected void okPressed(){
         SSLEditor.super.doLoadDefault();
         nameString = fileName.getText();
         super.okPressed();
      }


      public String getName(){
         return nameString;
      }

   }

}
