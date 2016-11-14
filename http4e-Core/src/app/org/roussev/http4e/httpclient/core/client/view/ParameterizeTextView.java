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

import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.roussev.http4e.httpclient.core.client.view.assist.DocumentUtils;
import org.roussev.http4e.httpclient.core.client.view.assist.HConfiguration;
import org.roussev.http4e.httpclient.core.client.view.assist.HContentAssistProcessor;

public class ParameterizeTextView implements IControlView {

   private StyledText styledText;


   public ParameterizeTextView( Composite parent) {
      styledText = buildEditorText(parent);

      Menu popupMenu = new Menu(styledText);
      new ClipboardMenu(styledText, popupMenu);
      styledText.setMenu(popupMenu);
   }


   private StyledText buildEditorText( Composite parent){
      final SourceViewer sourceViewer = new SourceViewer(parent, null, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
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

      return sourceViewer.getTextWidget();
   }


   public Control getControl(){
      return styledText;
   }
}
