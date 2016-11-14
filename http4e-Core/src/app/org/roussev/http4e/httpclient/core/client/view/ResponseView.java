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
import org.eclipse.swt.browser.Browser;
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
class ResponseView {

   private final String[]   text        = new String[] { "" };

   private final static int ITEM_RAW    = 0;
   private final static int ITEM_PRETTY = 1;
   private final static int ITEM_JSON   = 2;
   private final static int ITEM_HEX    = 3;
   private final static int ITEM_BRW    = 4;
   private int              currItem    = ITEM_RAW;

   private final StyledText responseText;
   private final StyledText jsonText;
   private final Browser    browser;
   private PayloadMenu      payloadMenu;
   private PayloadMenu      payloadMenuJson;


   ResponseView( final ItemModel model, Composite parent) {

      final ViewForm vForm = doToolbar(CoreConstants.TITLE_RESPONSE, model, parent);
      responseText = buildEditorText(vForm);
      jsonText = buildJsonEditorText(vForm);
      vForm.setContent(responseText);

      browser = new Browser(vForm, SWT.NONE);

      responseText.setFont(ResourceUtils.getFont(Styles.getInstance(parent.getShell()).getFontMonospaced()));
      responseText.setForeground(ResourceUtils.getColor(Styles.GRAY_RGB_TEXT));
      responseText.setBackground(ResourceUtils.getColor(Styles.BACKGROUND_DISABLED));
      responseText.setEditable(false);
      responseText.addMouseListener(new MouseAdapter() {
         public void mouseDoubleClick( MouseEvent e){
            model.fireExecute(new ModelEvent(ModelEvent.RESPONSE_RESIZED, model));
         }
      });
      responseText.addKeyListener(new ExecuteKeyListener(new ExecuteCommand() {
         public void execute(){
            model.fireExecute(new ModelEvent(ModelEvent.REQUEST_START, model));
         }
      }));
      
      jsonText.setFont(ResourceUtils.getFont(Styles.getInstance(parent.getShell()).getFontMonospaced()));
      jsonText.setForeground(ResourceUtils.getColor(Styles.GRAY_RGB_TEXT));
      jsonText.setBackground(ResourceUtils.getColor(Styles.BACKGROUND_DISABLED));
      jsonText.setEditable(false);
      jsonText.addMouseListener(new MouseAdapter() {
         public void mouseDoubleClick( MouseEvent e){
            model.fireExecute(new ModelEvent(ModelEvent.RESPONSE_RESIZED, model));
         }
      });
      jsonText.addKeyListener(new ExecuteKeyListener(new ExecuteCommand() {
         public void execute(){
            model.fireExecute(new ModelEvent(ModelEvent.REQUEST_START, model));
         }
      }));

      Menu popupMenu = new Menu(responseText);
      payloadMenu = new PayloadMenu(responseText, popupMenu);
      responseText.setMenu(popupMenu);

      Menu popupMenu2 = new Menu(jsonText);
      payloadMenuJson = new PayloadMenu(jsonText, popupMenu2);
      jsonText.setMenu(popupMenu2);

      ToolItem i_raw = Utils.getItem(ITEM_RAW, vForm);
      ToolItem i_pretty = Utils.getItem(ITEM_PRETTY, vForm);
      ToolItem i_hex = Utils.getItem(ITEM_HEX, vForm);
      ToolItem i_brw = Utils.getItem(ITEM_BRW, vForm);
      ToolItem i_json = Utils.getItem(ITEM_JSON, vForm);
      i_raw.setSelection(true);

      i_raw.addSelectionListener(new SelectionAdapter() {

         public void widgetSelected( SelectionEvent e){
            responseText.setText(text[0]);
            if (currItem == ITEM_BRW || currItem == ITEM_JSON) {
               vForm.setContent(responseText);
               vForm.redraw();
            }
            currItem = ITEM_RAW;
         }
      });

      i_pretty.addSelectionListener(new SelectionAdapter() {

         public void widgetSelected( SelectionEvent e){
            responseText.setText(JunkUtils.prettyText(text[0]));
            if (currItem == ITEM_BRW || currItem == ITEM_JSON) {
               vForm.setContent(responseText);
               vForm.redraw();
            }
            currItem = ITEM_PRETTY;
         }
      });

      i_hex.addSelectionListener(new SelectionAdapter() {

         public void widgetSelected( SelectionEvent e){
            JunkUtils.hexText(responseText, text[0]);
            if (currItem == ITEM_BRW || currItem == ITEM_JSON) {
               vForm.setContent(responseText);
               vForm.redraw();
            }
            currItem = ITEM_HEX;
         }
      });

      i_brw.addSelectionListener(new SelectionAdapter() {

         public void widgetSelected( SelectionEvent e){
            browser.setText(JunkUtils.prettyText(text[0]));
            if (currItem != ITEM_BRW) {
               vForm.setContent(browser);
               vForm.redraw();
            }
            currItem = ITEM_BRW;
         }
      });

      i_json.addSelectionListener(new SelectionAdapter() {

         public void widgetSelected( SelectionEvent e){
            jsonText.setText(JunkUtils.jsonText(JunkUtils.prettyText(text[0]), false));
            if (currItem != ITEM_JSON) {
               vForm.setContent(jsonText);
               vForm.redraw();
            }
            currItem = ITEM_JSON;
         }
      });
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


   private ViewForm doToolbar( String title, final ItemModel model, final Composite parent){

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

      ToolItem i_br = new ToolItem(bar, SWT.RADIO);
      i_br.setImage(ResourceUtils.getImage(CoreConstants.PLUGIN_CORE, CoreImages.BROWSER));
      i_br.setToolTipText("View in Browser");

      vForm.setTopCenter(bar);

      return vForm;
   }


   public void setHttpText( String txt){
      text[0] = txt;
      if (currItem == ITEM_RAW) {
         responseText.setText(text[0]);

      } else if (currItem == ITEM_PRETTY) {
         responseText.setText(JunkUtils.prettyText(text[0]));

      } else if (currItem == ITEM_HEX) {
         JunkUtils.hexText(responseText, text[0]);

      } else if (currItem == ITEM_BRW) {
         browser.setText(JunkUtils.prettyText(text[0]));

      } else {
         jsonText.setText(JunkUtils.jsonText(JunkUtils.prettyText(text[0]), false));
      }
   }


   public void setPayloadFilename( String filename){
      this.payloadMenu.setFilename(filename);
      this.payloadMenuJson.setFilename(filename);
   }

}
