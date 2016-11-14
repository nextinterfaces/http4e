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
package org.roussev.http4e.httpclient.core.util.shared;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.printing.Printer;
import org.roussev.http4e.httpclient.core.misc.Styles;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class PrinterFacade {

   private Font          font;
   // private Color foregroundColor, backgroundColor;
   private GC            gc;
   private Font          printerFont;
   // private Color printerForegroundColor, printerBackgroundColor;
   private int           lineHeight = 0;
   private int           tabWidth   = 0;
   private int           leftMargin, rightMargin, topMargin, bottomMargin;
   private int           x, y;
   private int           index, end;
   private String        textToPrint;
   private String        tabs;
   private StringBuilder wordBuffer;
   private Printer       printer;


   public PrinterFacade( String textToPrint, Printer printer) {
      this.textToPrint = textToPrint;
      this.printer = printer;
   }


   public void print(){
      if (printer.startJob("Text")) { // the string is the job name - shows up

         try {// in the printer's job list
            Rectangle clientArea = printer.getClientArea();
            Rectangle trim = printer.computeTrim(0, 0, 0, 0);
            Point dpi = printer.getDPI();
            leftMargin = dpi.x + trim.x; // one inch from left side of paper
            rightMargin = clientArea.width - dpi.x + trim.x + trim.width; // one
            // inch
            // from
            // right
            // side
            // of
            // paper
            topMargin = dpi.y + trim.y; // one inch from top edge of paper
            bottomMargin = clientArea.height - dpi.y + trim.y + trim.height; // one
            // inch
            // from
            // bottom
            // edge
            // of
            // paper

            /* Create a buffer for computing tab width. */
            int tabSize = 4; // is tab width a user setting in your UI?
            StringBuilder tabBuffer = new StringBuilder(tabSize);
            for (int i = 0; i < tabSize; i++)
               tabBuffer.append(' ');
            tabs = tabBuffer.toString();

            /*
             * Create printer GC, and create and set the printer font &
             * foreground color.
             */
            gc = new GC(printer);

            font = printer.getSystemFont(); // ResourceUtils.getFont(Styles.getInstance().FONT_COURIER);
            // foregroundColor = ResourceUtils.getColor(Styles.DARK_RGB_TEXT);
            // backgroundColor =
            // ResourceUtils.getColor(Styles.BACKGROUND_ENABLED);

            FontData fontData = font.getFontData()[0];
            printerFont = new Font(printer, fontData.getName(), fontData.getHeight(), fontData.getStyle());
            gc.setFont(printerFont);
            tabWidth = gc.stringExtent(tabs).x;
            lineHeight = gc.getFontMetrics().getHeight();

            // RGB rgb = foregroundColor.getRGB();
            // printerForegroundColor = new Color(printer, rgb);
            // gc.setForeground(printerForegroundColor);

            // rgb = backgroundColor.getRGB();
            // printerBackgroundColor = new Color(printer, rgb);
            // gc.setBackground(printerBackgroundColor);

            /* Print text to current gc using word wrap */
            printText(printer);
            printer.endJob();

         } catch (Exception e) {
            throw new RuntimeException(e);

         } finally {
            /* Cleanup graphics resources used in printing */
            if (printerFont != null)
               printerFont.dispose();
            // if(printerForegroundColor != null)
            // printerForegroundColor.dispose();
            // if(printerBackgroundColor != null)
            // printerBackgroundColor.dispose();
            if (gc != null)
               gc.dispose();
         }
      }
   }


   private void printText( Printer printer){
      printer.startPage();
      wordBuffer = new StringBuilder();
      x = leftMargin;
      y = topMargin;
      index = 0;
      end = textToPrint.length();
      while (index < end) {
         char c = textToPrint.charAt(index);
         index++;
         if (c != 0) {
            if (c == 0x0a || c == 0x0d) {
               if (c == 0x0d && index < end && textToPrint.charAt(index) == 0x0a) {
                  index++; // if this is cr-lf, skip the lf
               }
               printWordBuffer(printer);
               newline(printer);
            } else {
               if (c != '\t') {
                  wordBuffer.append(c);
               }
               if (Character.isWhitespace(c)) {
                  printWordBuffer(printer);
                  if (c == '\t') {
                     x += tabWidth;
                  }
               }
            }
         }
      }
      if (y + lineHeight <= bottomMargin) {
         printer.endPage();
      }
   }


   private void printWordBuffer( Printer printer){
      if (wordBuffer.length() > 0) {
         String word = wordBuffer.toString();
         int wordWidth = gc.stringExtent(word).x;
         if (x + wordWidth > rightMargin) {
            /* word doesn't fit on current line, so wrap */
            newline(printer);
         }
         gc.drawString(word, x, y, false);
         x += wordWidth;
         wordBuffer = new StringBuilder();
      }
   }


   private void newline( Printer printer){
      x = leftMargin;
      y += lineHeight;
      if (y + lineHeight > bottomMargin) {
         printer.endPage();
         if (index + 1 < end) {
            y = topMargin;
            printer.startPage();
         }
      }
   }

}
