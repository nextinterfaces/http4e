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
package org.roussev.http4e.httpclient.core.client.view.assist;

import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.roussev.http4e.httpclient.core.CoreContext;
import org.roussev.http4e.httpclient.core.CoreObjects;
import org.roussev.http4e.httpclient.core.misc.Styles;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class AstandaloneHEditor {

   private SourceViewer sourceViewer;

   public AstandaloneHEditor( Composite parent) {
      buildControls(parent);
   }

   public void dispose(){
      ResourceUtils.disposeResources();
   }

   private void buildControls( Composite parent){
      parent.setLayout(new FillLayout());
      
      sourceViewer = new SourceViewer(parent, null, SWT.MULTI | SWT.V_SCROLL);
      
      final HConfiguration sourceConf = new HConfiguration(HContentAssistProcessor.HEADER_PROCESSOR);
      sourceViewer.configure(sourceConf);
      sourceViewer.setDocument( DocumentUtils.createDocument1());

      // final IContentAssistant assistant = getContentAssistant(null);
      // assistant.install(textViewer);

      final StyledText st = sourceViewer.getTextWidget();
      
      sourceViewer.getControl().addKeyListener(new KeyAdapter() {
         public void keyPressed( KeyEvent e){
            if ((e.character == ' ') && ((e.stateMask & SWT.CTRL) != 0)) {
               IContentAssistant ca = sourceConf.getContentAssistant(sourceViewer);
               ca.showPossibleCompletions();

               st.setBackground(ResourceUtils.getColor(Styles.SSL));
            }
            st.setBackground(ResourceUtils.getColor(Styles.BACKGROUND_ENABLED));
         }
      });
      
      
      sourceViewer.addTextListener(new ITextListener() {
         public void textChanged( TextEvent e){
            AssistUtils.addTrackWords(e.getText(), sourceViewer.getDocument(), e.getOffset()-1, null);            
         }
      });
      
   }

   public static void main( String[] args){
      
      String ROOT_CORE  = "C:/.work/http4e/org.roussev.http4e/Core";     
      String ROOT_UI    = "C:/.work/http4e/org.roussev.http4e/Plugin";        
       
      CoreContext.getContext().putObject(CoreObjects.ROOT_PATH_CORE, ROOT_CORE);       
      CoreContext.getContext().putObject(CoreObjects.ROOT_PATH_UI, ROOT_UI);
      CoreContext.getContext().putObject(CoreObjects.IS_STANDALONE, "nonull");
       
      Display display = new Display();
      Shell shell = new Shell(display);
      shell.setBounds(700, 350, 500, 350);
      // shell.setLayout(new FillLayout());
      AstandaloneHEditor view = new AstandaloneHEditor(shell);

      shell.open();

      while (!shell.isDisposed()) {
         if (!display.readAndDispatch()) {
            display.sleep();
         }
      }
      view.dispose();
      display.dispose();
   }
}
