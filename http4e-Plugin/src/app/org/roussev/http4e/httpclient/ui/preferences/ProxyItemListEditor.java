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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.roussev.http4e.httpclient.core.client.model.ProxyItem;


/**
 * @author andreaszbschmidt
 *
 */
class ProxyItemListEditor extends ListEditor {

   private static final String DELIMITER = "#";


   public ProxyItemListEditor( String name, String labelText, Composite parent) {
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
      ProxyInputDialog inputDialog = new ProxyInputDialog(getShell());
      if (inputDialog.open() == InputDialog.OK) {
         // check for valid Input
         try {
            String name = inputDialog.getName();
            String host = inputDialog.getHost();
            String port = inputDialog.getPort();

            String inputText = name + "," + host + "," + port;

            // parse String for empty fields
            ProxyItem.createFromString(inputText);

            returnvalue = inputText;
         } catch (Exception e) {
            MessageDialog.openError(getShell(), "Wrong entry", "None of the fields must be left blank");
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

   private class ProxyInputDialog extends Dialog {

      private Text   host;
      private Text   port;
      private Text   name;
      private String hostString;
      private String portString;
      private String nameString;


      protected ProxyInputDialog( Shell parentShell) {
         super(parentShell);
      }


      @Override
      protected Control createDialogArea( Composite parent){
         getShell().setText("Proxy Parameters");
         Composite returnvalue = (Composite) super.createDialogArea(parent);
         // Name
         Label nameLabel = new Label(returnvalue, 64);
         nameLabel.setText("Name");
         GridData nameGridData = new GridData(1796);
         nameGridData.widthHint = convertHorizontalDLUsToPixels(300);
         nameLabel.setLayoutData(nameGridData);
         nameLabel.setFont(parent.getFont());
         name = new Text(returnvalue, SWT.BORDER);
         name.setLayoutData(new GridData(768));

         // Host
         Label hostLabel = new Label(returnvalue, 64);
         hostLabel.setText("Host");
         GridData hostGridData = new GridData(1796);
         hostGridData.widthHint = convertHorizontalDLUsToPixels(300);
         hostLabel.setLayoutData(hostGridData);
         hostLabel.setFont(parent.getFont());
         host = new Text(returnvalue, SWT.BORDER);
         host.setLayoutData(new GridData(768));
         // Port
         Label portLabel = new Label(returnvalue, 64);
         portLabel.setText("Port");
         GridData portGridData = new GridData(1796);
         portGridData.widthHint = convertHorizontalDLUsToPixels(300);
         portLabel.setLayoutData(portGridData);
         portLabel.setFont(parent.getFont());
         port = new Text(returnvalue, SWT.BORDER);
         port.setLayoutData(new GridData(768));

         return returnvalue;
      }


      @Override
      protected void okPressed(){
         // save values
         nameString = name.getText();
         hostString = host.getText();
         portString = port.getText();

         super.okPressed();
      }


      public String getName(){
         return nameString;
      }


      public String getHost(){
         return hostString;
      }


      public String getPort(){
         return portString;
      }

   }

}
