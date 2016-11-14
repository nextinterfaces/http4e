package org.json.me.test;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.roussev.http4e.httpclient.core.client.view.JSONLineStyler;

/**
 * This class implements syntax coloring using the StyledText API
 */
public class SyntaxTestExample {

   // Punctuation
   private static final String PUNCTUATION_OBJ = "{}";
   private static final String PUNCTUATION_ARR = "[]";
   private static final String PUNCTUATION_ALL = ",:";

   private Color               colorObj;
   private Color               colorArr;
   private Color               colorLeft;
   private Color               colorRight;


   private void createContents( Shell shell){
      shell.setLayout(new FillLayout());

      final StyledText styledText = new StyledText(shell, SWT.BORDER);
      styledText.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_DARK_MAGENTA));

//      // Add the syntax coloring handler
//      styledText.addExtendedModifyListener(new ExtendedModifyListener() {
//
//         public void modifyText( ExtendedModifyEvent event){
//            // Determine the ending offset
//            int end = event.start + event.length - 1;
//
//            // If they typed something, get it
//            if (event.start <= end) {
//               // Get the text
//               String text = styledText.getText(event.start, end);
//
//               // Create a collection to hold the StyleRanges
//               List ranges = new ArrayList();
//
//               // Turn any punctuation red
//               for (int i = 0, n = text.length(); i < n; i++) {
//                  char c = text.charAt(i);
//                  if (PUNCTUATION_ARR.indexOf(c) > -1) {
//                     ranges.add(new StyleRange(event.start + i, 1, colorArr, null, SWT.BOLD));
//
//                  } else if (PUNCTUATION_OBJ.indexOf(c) > -1) {
//                     ranges.add(new StyleRange(event.start + i, 1, colorObj, null, SWT.BOLD));
//
//                  } else if (PUNCTUATION_ALL.indexOf(c) > -1) {
//                     ranges.add(new StyleRange(event.start + i, 1, colorLeft, null, SWT.BOLD));
//                  }
//               }
//
//               // If we have any ranges to set, set them
//               if (!ranges.isEmpty()) {
//                  styledText.replaceStyleRanges(event.start, event.length, (StyleRange[]) ranges.toArray(new StyleRange[0]));
//               }
//            }
//         }
//      });

//      styledText.addBidiSegmentListener(new BidiSegmentListener() {
//
//         public void lineGetSegments( BidiSegmentEvent e){
//            String s = e.lineText;
//            System.out.println("line '" + s + "'");
//
//            // Create a collection to hold the StyleRanges
//            List ranges2 = new ArrayList();
//            ranges2.add(new StyleRange(e.lineOffset + 1, 1, colorRight, null, SWT.BOLD));
//
//            // If we have any ranges to set, set them
//            if (!ranges2.isEmpty()) {
//               styledText.replaceStyleRanges(e.lineOffset, e.lineOffset + 1, (StyleRange[]) ranges2.toArray(new StyleRange[0]));
//            }
//         }
//      });

      populateText(styledText);
      
      JSONLineStyler javaLineStyler = new JSONLineStyler();
      styledText.addLineStyleListener(javaLineStyler);
   }
   
//   public void lineGetStyle(LineStyleEvent event) {
//      Vector styles = new Vector();
//
//      // If the line is part of a block comment, create one style for the entire line.
//      if (inBlockComment(event.lineOffset, event.lineOffset + event.lineText.length())) {
//          styles.addElement(new StyleRange(event.lineOffset, event.lineText.length(), getColor(COMMENT), null));
//          event.styles = new StyleRange[styles.size()];
//          styles.copyInto(event.styles);
//          return;
//      }
//      
//      int token;
//      StyleRange lastStyle;
//      Color defaultFgColor = ((Control)event.widget).getForeground();
//      scanner.setRange(event.lineText);
//      token = scanner.nextToken();
//      while (token != EOF) {
//          if (token == OTHER) {
//              // no syntax highlighting necessary
//          } else if ((token != WHITE) {
//              // Only create a style if the token color is different than the widget's default foreground color and the 
//              // token’s fontStyle is not bold.
//              Color color = getColor(token);
//              if ((!color.equals(defaultFgColor)) || (token == KEY)) {
//                  StyleRange style = new StyleRange(scanner.getStartOffset() + event.lineOffset, scanner.getLength(), color, null);
//                  if (token == KEY) {
//                      style.fontStyle = SWT.BOLD;
//                  }
//                  if (styles.isEmpty()) {
//                      styles.addElement(style);
//                  } else {
//                      // Merge similar styles. 
//                      lastStyle = (StyleRange)styles.lastElement();
//                      if (lastStyle.similarTo(style) && (lastStyle.start + lastStyle.length == style.start)) {
//                          lastStyle.length += style.length;
//                      } else {
//                          styles.addElement(style); 
//                      }
//                  }
//              }
//          } else if (!styles.isEmpty()) && ((lastStyle=(StyleRange)styles.lastElement()).fontStyle == SWT.BOLD)) {
//              // Have the white space take on the bold style before it to minimize the number of style ranges.
//              int start = scanner.getStartOffset() + event.lineOffset;
//              if (lastStyle.start + lastStyle.length == start) {
//                  lastStyle.length += scanner.getLength();
//              }
//          }
//          token= scanner.nextToken();
//      }
//      event.styles = new StyleRange[styles.size()];
//      styles.copyInto(event.styles);
//  }


   private void populateText( StyledText styledText){

      try {
         styledText.setText(Uutt.readFileAsString("/Users/anna/_tools/tomcat-7.0.4/webapps/http4e/json4.html"));
      } catch (IOException e) {
         e.printStackTrace();
      }
   }


   public void run(){
      Display display = new Display();
      Shell shell = new Shell(display);

      colorObj = display.getSystemColor(SWT.COLOR_BLUE);
      colorArr = display.getSystemColor(SWT.COLOR_RED);
      colorLeft = display.getSystemColor(SWT.COLOR_DARK_BLUE);
      colorRight = display.getSystemColor(SWT.COLOR_GREEN);

      createContents(shell);
      shell.open();
      while (!shell.isDisposed()) {
         if (!display.readAndDispatch()) {
            display.sleep();
         }
      }

      // No need to dispose red

      display.dispose();
   }


   public static void main( String[] args){
      new SyntaxTestExample().run();
   }
}
