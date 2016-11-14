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

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.ViewPart;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreImages;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class ImportDialog extends TitleAreaDialog {

   private StyledText    text;
   private static int    HEIGHT = 400;
   private static int    WEIGHT = 300;
   private MouseListener okListener;
   private static final RGB       GRAY_NORMAL_TEXT       = new RGB(15, 15, 15);
   private static final RGB       GRAY_RGB_TEXT       = new RGB(180, 180, 180);


   public ImportDialog( ViewPart view) {
      super(view.getViewSite().getShell());
      setTitleImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, CoreImages.LOGO_DIALOG));
   }

   public void setOkListener( MouseListener okListener){
      this.okListener = okListener;
   }


   public boolean close(){
      return super.close();
   }


   protected Control createContents( Composite parent){
      Control contents = super.createContents(parent);
      setTitle("Import HTTP packet");
      setMessage("Import a raw HTTP packet to Client.");
      return contents;
   }


   protected Control createDialogArea( Composite parent){
      Composite composite = (Composite) super.createDialogArea(parent);
      text = new StyledText(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
      GridData spec = new GridData();
      spec.heightHint = HEIGHT;
      spec.widthHint = WEIGHT;
      spec.horizontalAlignment = GridData.FILL;
      spec.grabExcessHorizontalSpace = true;
      spec.verticalAlignment = GridData.FILL;
      spec.grabExcessVerticalSpace = true;
      text.setLayoutData(spec);
      text.setForeground(ResourceUtils.getColor(GRAY_RGB_TEXT));
      text.setText("POST /user/some HTTP/1.1\nContent-Type: application/xml\nUser-Agent: http4e/3.1.5\nHost: www.nextinterfaces.com\nContent-Length: 11\n\nsample body long data..");

      final boolean[] isClicked = {false};
      text.addMouseListener(new MouseListener(){

         public void mouseDoubleClick( MouseEvent e){
            // TODO Auto-generated method stub
            
         }

         public void mouseDown( MouseEvent e){
            if(!isClicked[0]){
               text.setText("");
               text.setForeground(ResourceUtils.getColor(GRAY_NORMAL_TEXT));
            }
            isClicked[0] = true;
         }

         public void mouseUp( MouseEvent e){
         }         
      });
      return composite;
   }


   protected void createButtonsForButtonBar( Composite parent){
      Button ok = createButton(parent, IDialogConstants.OK_ID, "Import Packet", true);
      createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
      ok.addMouseListener(okListener);
      ok.addTraverseListener(new TraverseListener() {

         public void keyTraversed( TraverseEvent e){
            if (SWT.TRAVERSE_RETURN == e.detail) {
            }
         }
      });
   }
   
   public String getText(){
      return text.getText();
   }

}
