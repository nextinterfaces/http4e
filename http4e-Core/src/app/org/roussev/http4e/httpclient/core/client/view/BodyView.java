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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreImages;
import org.roussev.http4e.httpclient.core.client.model.ItemModel;
import org.roussev.http4e.httpclient.core.client.model.ModelEvent;
import org.roussev.http4e.httpclient.core.misc.Styles;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
class BodyView {

   StyledText    bodyText;
   AttachManager attachManager;
   CLabel        titleLabel;


   BodyView( final ItemModel model, Composite parent) {

      ViewForm vForm = ViewUtils.buildViewForm(CoreConstants.TITLE_BODY, model, parent);
      titleLabel = (CLabel) vForm.getChildren()[0];
      bodyText = new StyledText(vForm, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
      vForm.setContent(bodyText);

      bodyText.setEditable(false);

      // Font font = Display.getCurrent().getSystemFont();
      // font.getFontData()[0].height = 12;
      // bodyText.setFont(font);
      bodyText.setFont(ResourceUtils.getFont(Styles.getInstance(parent.getShell()).getFontMonospaced()));
      bodyText.setForeground(ResourceUtils.getColor(Styles.GRAY_RGB_TEXT));

      // bodyText.setFont(ResourceUtils.getFont(Styles.FONT_COURIER));
      // bodyText.setForeground(ResourceUtils.getColor(Styles.GRAY_RGB_TEXT));
      bodyText.setBackground(ResourceUtils.getColor(Styles.PINK_DISABLED));

      bodyText.addMouseListener(new MouseAdapter() {

         public void mouseDoubleClick( MouseEvent e){
            model.fireExecute(new ModelEvent(ModelEvent.BODY_RESIZED, model));
         }
      });
      bodyText.addFocusListener(new FocusListener() {

         private String prevBody = model.getBody();


         public void focusGained( FocusEvent e){
            if (!prevBody.equals(bodyText.getText())) {
               prevBody = bodyText.getText();
               model.fireExecute(new ModelEvent(ModelEvent.BODY_FOCUS_GAINED, model));
            }
         }


         public void focusLost( FocusEvent e){
            if (!prevBody.equals(bodyText.getText())) {
               prevBody = bodyText.getText();
               model.fireExecute(new ModelEvent(ModelEvent.BODY_FOCUS_LOST, model));
            }
         }
      });
      
      bodyText.addKeyListener(new ExecuteKeyListener(new ExecuteCommand() {
         public void execute(){
            model.fireExecute(new ModelEvent(ModelEvent.REQUEST_START, model));
         }
      }));

      final ToolBar bar = new ToolBar(vForm, SWT.FLAT);

      ToolItem clearBtn = new ToolItem(bar, SWT.PUSH);
      clearBtn.setImage(ResourceUtils.getImage(CoreConstants.PLUGIN_CORE, CoreImages.DELETE));
      clearBtn.setToolTipText("Clear");
      clearBtn.addSelectionListener(new SelectionListener() {

         public void widgetDefaultSelected( SelectionEvent e){
         }


         public void widgetSelected( SelectionEvent e){
            BodyView.this.setText("");
         }
      });
      attachManager = new AttachManager(model, bodyText, bar);

      vForm.setTopCenter(bar);
   }


   String getText(){
      return bodyText.getText();
   }


   CLabel getTitleLabel(){
      return titleLabel;
   }


   void setText( String txt){
      bodyText.setText(txt);
   }


   void setBackground( Color color){
      StyledText st = (StyledText) bodyText;
      st.setBackground(color);
   }


   void setEditable( boolean editable, boolean isXwwwForm, boolean isPost){
      if (editable) {
         // bodyText.setFont(ResourceUtils.getFont(Styles.FONT_COURIER));
         bodyText.setForeground(ResourceUtils.getColor(Styles.GRAY_RGB_TEXT));
         bodyText.setBackground(ResourceUtils.getColor(Styles.BACKGROUND_ENABLED));
         bodyText.setEditable(true);
         // attachManager.setEnabled(true);
         if (isXwwwForm && isPost) {
            attachManager.setEnabled(false);
         } else {
            attachManager.setEnabled(true);
         }
         // if(isXwwForm && isPost){
         // attachManager.setEnabled(false);
         // } else {
         // attachManager.setEnabled(true);
         // }

      } else {
         // bodyText.setFont(ResourceUtils.getFont(Styles.FONT_COURIER));
         bodyText.setForeground(ResourceUtils.getColor(Styles.LIGHT_RGB_TEXT));
         bodyText.setBackground(ResourceUtils.getColor(Styles.PINK_DISABLED));
         bodyText.setEditable(false);
         attachManager.setEnabled(false);
      }
   }

}
