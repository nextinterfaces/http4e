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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreImages;
import org.roussev.http4e.httpclient.core.CoreMessages;
import org.roussev.http4e.httpclient.core.client.model.ItemModel;
import org.roussev.http4e.httpclient.core.misc.Styles;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
class ParamView {

   private IControlView textView;
   private CLabel       titleLabel;
   private ParamsAttachManager attachManager;
   private boolean        isMultipart = false;


   ParamView( final ItemModel model, Composite parent) {
      ViewForm vForm = ViewUtils.buildViewForm(CoreConstants.TITLE_PARAMETERS, model, parent);
      textView = new ParamTextView(model, vForm);
      titleLabel = (CLabel) vForm.getChildren()[0];
      final ToolBar bar = new ToolBar(vForm, SWT.FLAT);
      vForm.setContent(textView.getControl());
      ToolItem clearBtn = new ToolItem(bar, SWT.PUSH);
      clearBtn.setImage(ResourceUtils.getImage(CoreConstants.PLUGIN_CORE, CoreImages.DELETE));
      clearBtn.setToolTipText("Clear");
      clearBtn.addSelectionListener(new SelectionListener() {

         public void widgetDefaultSelected( SelectionEvent e){
         }


         public void widgetSelected( SelectionEvent e){
            ParamView.this.setParamText("");
         }
      });
      attachManager = new ParamsAttachManager(model, (StyledText) textView.getControl(), bar);
      vForm.setContent(textView.getControl());
      vForm.setTopCenter(bar);
   }


   String getParamText(){
      StyledText st = (StyledText) textView.getControl();
      if (CoreMessages.PARAM_DEFAULTS.equals(st.getText())) {
         return CoreConstants.EMPTY_TEXT;
      }
      String txt = st.getText();
      
      return txt;
   }


   void setParamText( String txt){
      StyledText st = (StyledText) textView.getControl();
      if (CoreConstants.EMPTY_TEXT.equals(txt)) {
         st.setText(CoreMessages.PARAM_DEFAULTS);
      } else {
         st.setText(txt);
      }
      if (CoreConstants.EMPTY_TEXT.equals(getParamText())) {
         st.setForeground(ResourceUtils.getColor(Styles.LIGHT_RGB_TEXT));
      } else {
         st.setForeground(ResourceUtils.getColor(Styles.DARK_RGB_TEXT));
      }
   }


   void setFocus( boolean focusGained){
      StyledText st = (StyledText) textView.getControl();
      if (focusGained) {
         if (CoreConstants.EMPTY_TEXT.equals(getParamText())) {
            st.setText(CoreConstants.EMPTY_TEXT);
         }
         st.setForeground(ResourceUtils.getColor(Styles.DARK_RGB_TEXT));
      }
   }


   void setEditable( boolean editable){
      StyledText st = (StyledText) textView.getControl();
      if (editable) {
         // bodyText.setFont(ResourceUtils.getFont(Styles.FONT_COURIER));
         st.setForeground(ResourceUtils.getColor(Styles.GRAY_RGB_TEXT));
         st.setBackground(ResourceUtils.getColor(Styles.BACKGROUND_ENABLED));
         st.setEditable(true);
         attachManager.setEnabled(true);

      } else {
         // bodyText.setFont(ResourceUtils.getFont(Styles.FONT_COURIER));
         st.setForeground(ResourceUtils.getColor(Styles.LIGHT_RGB_TEXT));
         st.setBackground(ResourceUtils.getColor(Styles.PINK_DISABLED));
         st.setEditable(false);
         attachManager.setEnabled(false);
      }
   }


   CLabel getTitleLabel(){
      return titleLabel;
   }


   boolean isEditable(){
      StyledText st = (StyledText) textView.getControl();
      return st.getEditable();
   }

   
   public void setMultipart( boolean isMultipart){
      this.attachManager.setMultipart(isMultipart);
   }


   void setBackground( Color color){
      StyledText st = (StyledText) textView.getControl();
      st.setBackground(color);
   }

}
