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

import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.roussev.http4e.editor.xml.XMLConfiguration;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreImages;
import org.roussev.http4e.httpclient.core.client.model.ItemModel;
import org.roussev.http4e.httpclient.core.client.model.ModelEvent;
import org.roussev.http4e.httpclient.core.client.view.assist.DocumentUtils;
import org.roussev.http4e.httpclient.core.misc.ColorManagerAdaptor;
import org.roussev.http4e.httpclient.core.misc.Styles;
import org.roussev.http4e.httpclient.core.util.JunkUtils;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
class RequestView {

   private final StringBuilder[] textBuff    = { new StringBuilder("") };
   private final static int      ITEM_RAW    = 0;
   private final static int      ITEM_PRETTY = 1;
   private final static int      ITEM_JSON   = 2;
   private final static int      ITEM_HEX    = 3;
   private int                   currItem    = ITEM_RAW;

   private ViewForm              vForm;
   private StyledText            jsonText;
   private StyledText            reqText;
   private PayloadMenu           payloadMenu;
   private PayloadMenu           payloadMenuJson;
   private final JSONLineStyler  jsonStyler;


   RequestView( final ItemModel model, Composite parent) {

      jsonStyler = new JSONLineStyler();
      vForm = doToolbarControl(CoreConstants.TITLE_REQUEST, model, parent);
      reqText = buildEditorText(vForm);
      jsonText = buildJsonEditorText(vForm);

      // reqText = new StyledText(vForm, SWT.BORDER);
      doTextControl(model);
      // reqText.addLineStyleListener(jsonStyler);
      vForm.setContent(reqText);

      reqText.addKeyListener(new ExecuteKeyListener(new ExecuteCommand() {
         public void execute(){
            model.fireExecute(new ModelEvent(ModelEvent.REQUEST_START, model));
         }
      }));

      jsonText.addKeyListener(new ExecuteKeyListener(new ExecuteCommand() {
         public void execute(){
            model.fireExecute(new ModelEvent(ModelEvent.REQUEST_START, model));
         }
      }));
   }


   private StyledText buildEditorText( Composite parent){
      final SourceViewer sourceViewer = new SourceViewer(parent, null, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);

      final XMLConfiguration sourceConf = new XMLConfiguration(new ColorManagerAdaptor(ResourceUtils.getResourceCache()));
      sourceViewer.configure(sourceConf);
      sourceViewer.setDocument(DocumentUtils.createDocument2());

      return sourceViewer.getTextWidget();
   }


   private StyledText buildJsonEditorText( Composite parent){
      final SourceViewer sourceViewer = new SourceViewer(parent, null, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
      StyledText st = sourceViewer.getTextWidget();
      JSONLineStyler jsonStyler = new JSONLineStyler();
      st.addLineStyleListener(jsonStyler);
      return st;
   }


   private void doTextControl( final ItemModel model){

      reqText.setFont(ResourceUtils.getFont(Styles.getInstance(reqText.getShell()).getFontMonospaced()));
      reqText.setForeground(ResourceUtils.getColor(Styles.GRAY_RGB_TEXT));
      reqText.setBackground(ResourceUtils.getColor(Styles.BACKGROUND_DISABLED));
      reqText.setEditable(false);
      reqText.addMouseListener(new MouseAdapter() {

         public void mouseDoubleClick( MouseEvent e){
            model.fireExecute(new ModelEvent(ModelEvent.REQUEST_RESIZED, model));
         }
      });

      jsonText.setFont(ResourceUtils.getFont(Styles.getInstance(jsonText.getShell()).getFontMonospaced()));
      jsonText.setForeground(ResourceUtils.getColor(Styles.GRAY_RGB_TEXT));
      jsonText.setBackground(ResourceUtils.getColor(Styles.BACKGROUND_DISABLED));
      jsonText.setEditable(false);
      jsonText.addMouseListener(new MouseAdapter() {

         public void mouseDoubleClick( MouseEvent e){
            model.fireExecute(new ModelEvent(ModelEvent.REQUEST_RESIZED, model));
         }
      });

      Menu popupMenu = new Menu(reqText);
      payloadMenu = new PayloadMenu(reqText, popupMenu);
      reqText.setMenu(popupMenu);

      Menu popupMenu2 = new Menu(jsonText);
      payloadMenuJson = new PayloadMenu(jsonText, popupMenu2);
      jsonText.setMenu(popupMenu2);
   }


   private ViewForm doToolbarControl( String title, final ItemModel model, final Composite parent){

      final ViewForm vForm = ViewUtils.buildViewForm(title, model, parent);
      final ToolBar bar = new ToolBar(vForm, SWT.FLAT);

      ToolItem i_raw = new ToolItem(bar, SWT.RADIO);
      i_raw.setImage(ResourceUtils.getImage(CoreConstants.PLUGIN_CORE, CoreImages.RAW));
      i_raw.setToolTipText("Raw View");

      ToolItem i_pretty = new ToolItem(bar, SWT.RADIO);
      i_pretty.setImage(ResourceUtils.getImage(CoreConstants.PLUGIN_CORE, CoreImages.PRETTY));
      i_pretty.setToolTipText("Pretty View");

      ToolItem i_json = new ToolItem(bar, SWT.RADIO);
      i_json.setImage(ResourceUtils.getImage(CoreConstants.PLUGIN_CORE, CoreImages.JSON));
      i_json.setToolTipText("JSON View");

      ToolItem i_hex = new ToolItem(bar, SWT.RADIO);
      i_hex.setImage(ResourceUtils.getImage(CoreConstants.PLUGIN_CORE, CoreImages.HEX));
      i_hex.setToolTipText("Hex View");

      vForm.setTopCenter(bar);

      i_raw.setSelection(true);

      i_raw.addSelectionListener(new SelectionAdapter() {

         public void widgetSelected( SelectionEvent e){
            reqText.setText(textBuff[0].toString());
            if (currItem == ITEM_JSON) {
               vForm.setContent(reqText);
               vForm.redraw();
            }
            currItem = ITEM_RAW;
         }
      });

      i_pretty.addSelectionListener(new SelectionAdapter() {

         public void widgetSelected( SelectionEvent e){
            reqText.setText(JunkUtils.prettyText(textBuff[0].toString()));
            if (currItem == ITEM_JSON) {
               vForm.setContent(reqText);
               vForm.redraw();
            }
            currItem = ITEM_PRETTY;
         }
      });

      i_json.addSelectionListener(new SelectionAdapter() {

         public void widgetSelected( SelectionEvent e){
            jsonText.setText(JunkUtils.jsonText(JunkUtils.prettyText(textBuff[0].toString()), false));
            if (currItem != ITEM_JSON) {
               vForm.setContent(jsonText);
               vForm.redraw();
            }
            currItem = ITEM_JSON;
         }
      });

      i_hex.addSelectionListener(new SelectionAdapter() {

         public void widgetSelected( SelectionEvent e){
            JunkUtils.hexText(reqText, textBuff[0].toString());
            if (currItem == ITEM_JSON) {
               vForm.setContent(reqText);
               vForm.redraw();
            }
            currItem = ITEM_HEX;
         }
      });

      return vForm;
   }


   void appendHttpText( String txt){
      textBuff[0].append(txt);

      if (currItem == ITEM_RAW) {
         reqText.setText(textBuff[0].toString());

      } else if (currItem == ITEM_PRETTY) {
         reqText.setText(JunkUtils.prettyText(textBuff[0].toString()));

      } else if (currItem == ITEM_JSON) {
         jsonText.setText(JunkUtils.jsonText(JunkUtils.prettyText(textBuff[0].toString()), false));

      } else {
         JunkUtils.hexText(reqText, textBuff[0].toString());
      }
   }


   public void setPayloadFilename( String filename){
      this.payloadMenu.setFilename(filename);
      this.payloadMenuJson.setFilename(filename);
   }


   void setHttpText( String txt){
      textBuff[0] = new StringBuilder(txt);

      if (currItem == ITEM_RAW) {
         reqText.setText(textBuff[0].toString());

      } else if (currItem == ITEM_PRETTY) {
         reqText.setText(JunkUtils.prettyText(textBuff[0].toString()));

      } else if (currItem == ITEM_JSON) {
         jsonText.setText(JunkUtils.prettyText(textBuff[0].toString()));
         // jsonText.setText(JunkUtils.jsonText(JunkUtils.prettyText(textBuff[0].toString()),
         // false));
         // jsonText.setText(JunkUtils.jsonText(textBuff[0].toString(), false));

      } else {
         JunkUtils.hexText(reqText, textBuff[0].toString());
      }

      // if (currItem == ITEM_RAW) {
      // reqText.setText(textBuff[0].toString());
      //         
      // } else if (currItem == ITEM_PRETTY) {
      // reqText.setText(textBuff[0].toString());
      //         
      // } else if (currItem == ITEM_JSON) {
      // reqText.setText(textBuff[0].toString());
      //         
      // } else {
      // JunkUtils.hexText(reqText, textBuff[0].toString());
      // }
   }

}
