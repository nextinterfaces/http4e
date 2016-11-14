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

import java.util.Calendar;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreContext;
import org.roussev.http4e.httpclient.core.CoreImages;
import org.roussev.http4e.httpclient.core.CoreMessages;
import org.roussev.http4e.httpclient.core.util.BaseUtils;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class AboutDialog extends TitleAreaDialog {

   public AboutDialog( Shell shell) {
      super(shell);
      setTitleImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, CoreImages.LOGO_DIALOG));
      setHelpAvailable(false);
   }


   public boolean close(){
      return super.close();
   }


   protected Control createContents( Composite parent){
      Control contents = super.createContents(parent);
      setTitle(CoreMessages.PLUGIN_NAME);
      setMessage(CoreMessages.PLUGIN_ABOUT);
      return contents;
   }


   protected Control createDialogArea( Composite parent){
      Composite composite = (Composite) super.createDialogArea(parent);

      Browser browser = new Browser(composite, SWT.NONE);
      browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

      try {
         String html = new String(ResourceUtils.getBundleResourceBytes(CoreConstants.PLUGIN_UI, "resources/about.html"));
         html = html.replaceAll("currentYear", ""+Calendar.getInstance().get(Calendar.YEAR));
         browser.setText(html);

      } catch (Exception e) {
         setErrorMessage(e.getLocalizedMessage());
      }
      return composite;
   }

   /**
    * Creates the buttons for the button bar
    * 
    * @param parent
    *           the parent composite
    */
   protected void createButtonsForButtonBar( Composite parent){
      createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
   }

}
