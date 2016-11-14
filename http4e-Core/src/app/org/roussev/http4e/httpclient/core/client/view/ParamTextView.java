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

import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.snippets.Snippet25;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.roussev.http4e.httpclient.core.client.model.ItemModel;
import org.roussev.http4e.httpclient.core.client.model.ModelEvent;
import org.roussev.http4e.httpclient.core.client.view.assist.AssistUtils;
import org.roussev.http4e.httpclient.core.client.view.assist.DocumentUtils;
import org.roussev.http4e.httpclient.core.client.view.assist.HConfiguration;
import org.roussev.http4e.httpclient.core.client.view.assist.HContentAssistProcessor;
import org.roussev.http4e.httpclient.core.client.view.assist.ModelTrackerListener;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
class ParamTextView implements IControlView {

   private StyledText styledText;
   final ItemModel    model;


   ParamTextView( final ItemModel iModel, Composite parent) {
      this.model = iModel;
      styledText = buildEditorText(parent);

      Menu popupMenu = new Menu(styledText);
      new ClipboardMenu(styledText, popupMenu);
      styledText.setMenu(popupMenu);

      styledText.addMouseListener(new MouseAdapter() {

         public void mouseDoubleClick( MouseEvent e){
            model.fireExecute(new ModelEvent(ModelEvent.PARAMS_RESIZED, model));
         }
      });
      styledText.addFocusListener(new FocusListener() {

         public void focusGained( FocusEvent e){
            model.fireExecute(new ModelEvent(ModelEvent.PARAMS_FOCUS_GAINED, model));
         }


         public void focusLost( FocusEvent e){
            model.fireExecute(new ModelEvent(ModelEvent.PARAMS_FOCUS_LOST, model));
         }
      });

      styledText.addKeyListener(new ExecuteKeyListener(new ExecuteCommand() {
         public void execute(){
            model.fireExecute(new ModelEvent(ModelEvent.REQUEST_START, model));
         }
      }));

   }


   private StyledText buildEditorText( Composite parent){
      final SourceViewer sourceViewer = new SourceViewer(parent, null, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
      final HConfiguration sourceConf = new HConfiguration(HContentAssistProcessor.PARAM_PROCESSOR);
      sourceViewer.configure(sourceConf);
      sourceViewer.setDocument(DocumentUtils.createDocument1());

      sourceViewer.getControl().addKeyListener(new KeyAdapter() {

         public void keyPressed( KeyEvent e){
            // if ((e.character == ' ') && ((e.stateMask & SWT.CTRL) != 0)) {
            if (Utils.isAutoAssistInvoked(e)) {
               IContentAssistant ca = sourceConf.getContentAssistant(sourceViewer);
               ca.showPossibleCompletions();
            }
         }
      });

      sourceViewer.addTextListener(new ITextListener() {

         public void textChanged( TextEvent e){
            ModelTrackerListener trackerListener = new ModelTrackerListener() {

               public void fireExecute( String key, String value){
                  model.fireExecute(new ModelEvent(ModelEvent.PARAMS_FOCUS_LOST, model));
               }
            };
            AssistUtils.addTrackWords(e.getText(), sourceViewer.getDocument(), e.getOffset() - 1, trackerListener);
         }
      });

      return sourceViewer.getTextWidget();
   }


   public Control getControl(){
      return styledText;
   }
}
