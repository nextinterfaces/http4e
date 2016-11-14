package org.roussev.http4e.httpclient.core.client.view;

import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.roussev.http4e.editor.xml.XMLConfiguration;
import org.roussev.http4e.httpclient.core.client.view.assist.DocumentUtils;
import org.roussev.http4e.httpclient.core.misc.ColorManagerAdaptor;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;

public class SWTHelloWorld {

   private static StyledText buildEditorText( Composite parent){
      final SourceViewer sourceViewer = new SourceViewer(parent, null, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);

      final XMLConfiguration sourceConf = new XMLConfiguration(new ColorManagerAdaptor(ResourceUtils.getResourceCache()));
      sourceViewer.configure(sourceConf);
      sourceViewer.setDocument(DocumentUtils.createDocument2());

      return sourceViewer.getTextWidget();
   }


   public static void main( String[] args){

      Shell shell = new Shell();
      shell.setLayout(new FillLayout());
      shell.setSize(200, 100);
      Display display = shell.getDisplay();

      // JsonTextEditor editor = new JsonTextEditor();
      // editor.gete

      StyledText styledText = buildEditorText(shell);

      shell.open();
      
      while (!shell.isDisposed()) {
         if (!display.readAndDispatch()) {
            display.sleep();
         }
      }
   }
}