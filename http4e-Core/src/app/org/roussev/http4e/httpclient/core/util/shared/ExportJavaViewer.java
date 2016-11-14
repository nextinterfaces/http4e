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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/** 
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class ExportJavaViewer {

   private StyledText     text;
   private JavaLineStyler lineStyler = new JavaLineStyler();
   private static int HEIGHT = 650;
   private static int WEIGHT = 540;
   
   
   public static void main( String[] args){
      Display display = new Display();
      Shell shell = new Shell(display);      
      shell.setBounds(100, 100, 430, 575);
      shell.setLayout(new FillLayout());
      
      ExportJavaViewer javaViewer = new ExportJavaViewer(shell);
      InputStream in;
      byte[] data = null;
      try {
         in = new FileInputStream("C:/_dev/workspace/Java5/src/enums/HttpStatusException.java");
         data = new byte[in.available()];
         in.read(data);
         String javaSrc = new String(data, "UTF8");
         System.out.println("javaSrc=" + javaSrc);
         javaViewer.open(javaSrc);
      } catch (IOException e) {
         e.printStackTrace();
      }
      
      shell.open();
      
      while (!shell.isDisposed()) {
          if (!display.readAndDispatch()) {
              display.sleep();
          }
      }
      display.dispose();
   }
   
   
//   void createShell( Shell shell/*Display display*/){
////      shell = new Shell(display);
//      shell.setText("Window");
//      GridLayout layout = new GridLayout();
//      layout.numColumns = 1;
//      shell.setLayout(layout);
//      shell.addShellListener(new ShellAdapter() {
//         public void shellClosed( ShellEvent e){
//            lineStyler.disposeColors();
//            text.removeLineStyleListener(lineStyler);
//         }
//      });
//   }
 
   
   public ExportJavaViewer( Composite parent){
      createStyledText(parent);
   }
   
   
   void createStyledText(Composite parent){
      text = new StyledText(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
//      text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
      GridData spec = new GridData();
      spec.heightHint = HEIGHT;
      spec.widthHint = WEIGHT;
      spec.horizontalAlignment = GridData.FILL;
      spec.grabExcessHorizontalSpace = true;
      spec.verticalAlignment = GridData.FILL;
      spec.grabExcessVerticalSpace = true;
      text.setLayoutData(spec);
      text.addLineStyleListener(lineStyler);
      text.setEditable(false);
      Color bg = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
      text.setBackground(bg);
   }
   
   
//   void displayError( String msg, Shell shell){
//      MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
//      box.setMessage(msg);
//      box.open();
//   }


   public void open( final String sourceString){
      // Guard against superfluous mouse move events -- defer action until
      // later
      Display display = text.getDisplay();
      display.asyncExec(new Runnable() {
         public void run(){
            text.setText(sourceString);
         }
      });

      // parse the block comments up front since block comments can go across
      // lines - inefficient way of doing this
      lineStyler.parseBlockComments(sourceString);
   }
}

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * IBM Corporation - initial API and implementation
 ******************************************************************************/

class JavaLineStyler implements LineStyleListener {

   JavaScanner             scanner       = new JavaScanner();

   int[]                   tokenColors;

   Color[]                 colors;

   Vector                  blockComments = new Vector();

   public static final int EOF           = -1;

   public static final int EOL           = 10;

   public static final int WORD          = 0;

   public static final int WHITE         = 1;

   public static final int KEY_WORD      = 2;

   public static final int COMMENT       = 3;

   public static final int STRING        = 5;

   public static final int OTHER         = 6;

   public static final int NUMBER        = 7;

   public static final int MAXIMUM_TOKEN = 8;


   public JavaLineStyler() {
      initializeColors();
      scanner = new JavaScanner();
   }


   Color getColor( int type){
      if (type < 0 || type >= tokenColors.length) {
         return null;
      }
      return colors[tokenColors[type]];
   }


   boolean inBlockComment( int start, int end){
      for (int i = 0; i < blockComments.size(); i++) {
         int[] offsets = (int[]) blockComments.elementAt(i);
         // start of comment in the line
         if ((offsets[0] >= start) && (offsets[0] <= end))
            return true;
         // end of comment in the line
         if ((offsets[1] >= start) && (offsets[1] <= end))
            return true;
         if ((offsets[0] <= start) && (offsets[1] >= end))
            return true;
      }
      return false;
   }


   void initializeColors(){
      Display display = Display.getDefault();
      colors = new Color[] { new Color(display, new RGB(0, 0, 0)), // black
            new Color(display, new RGB(0, 153, 0)), // comment
            new Color(display, new RGB(153, 0, 0)), // string
            new Color(display, new RGB(0, 51, 255)) // reserved words
      };
      tokenColors = new int[MAXIMUM_TOKEN];
      tokenColors[WORD] = 0;
      tokenColors[WHITE] = 0;
      tokenColors[KEY_WORD] = 3;
      tokenColors[COMMENT] = 1;
      tokenColors[STRING] = 2;
      tokenColors[OTHER] = 0;
      tokenColors[NUMBER] = 0;
   }


   void disposeColors(){
      for (int i = 0; i < colors.length; i++) {
         colors[i].dispose();
      }
   }


   /**
    * Event.detail line start offset (input) Event.text line text (input)
    * LineStyleEvent.styles Enumeration of StyleRanges, need to be in order.
    * (output) LineStyleEvent.background line background color (output)
    */
   public void lineGetStyle( LineStyleEvent event){
      Vector styles = new Vector();
      int token;
      StyleRange lastStyle;
      // If the line is part of a block comment, create one style for the
      // entire line.
      if (inBlockComment(event.lineOffset, event.lineOffset + event.lineText.length())) {
         styles.addElement(new StyleRange(event.lineOffset, event.lineText.length(), getColor(COMMENT), null));
         event.styles = new StyleRange[styles.size()];
         styles.copyInto(event.styles);
         return;
      }
      Color defaultFgColor = ((Control) event.widget).getForeground();
      scanner.setRange(event.lineText);
      token = scanner.nextToken();
      while (token != EOF) {
         if (token == OTHER) {
            // do nothing for non-colored tokens
         } else if (token != WHITE) {
            Color color = getColor(token);
            // Only create a style if the token color is different than the
            // widget's default foreground color and the token's style is
            // not
            // bold. Keywords are bolded.
            if ((!color.equals(defaultFgColor)) || (token == KEY_WORD)) {
               StyleRange style = new StyleRange(scanner.getStartOffset() + event.lineOffset, scanner.getLength(), color, null);
               if (token == KEY_WORD || token == STRING) {
                  style.fontStyle = SWT.BOLD;
               }
               if (styles.isEmpty()) {
                  styles.addElement(style);
               } else {
                  // Merge similar styles. Doing so will improve
                  // performance.
                  lastStyle = (StyleRange) styles.lastElement();
                  if (lastStyle.similarTo(style) && (lastStyle.start + lastStyle.length == style.start)) {
                     lastStyle.length += style.length;
                  } else {
                     styles.addElement(style);
                  }
               }
            }
         } else if ((!styles.isEmpty()) && ((lastStyle = (StyleRange) styles.lastElement()).fontStyle == SWT.BOLD)) {
            int start = scanner.getStartOffset() + event.lineOffset;
            lastStyle = (StyleRange) styles.lastElement();
            // A font style of SWT.BOLD implies that the last style
            // represents a java keyword.
            if (lastStyle.start + lastStyle.length == start) {
               // Have the white space take on the style before it to
               // minimize the number of style ranges created and the
               // number of font style changes during rendering.
               lastStyle.length += scanner.getLength();
            }
         }
         token = scanner.nextToken();
      }
      event.styles = new StyleRange[styles.size()];
      styles.copyInto(event.styles);
   }


   public void parseBlockComments( String text){
      blockComments = new Vector();
      StringReader buffer = new StringReader(text);
      int ch;
      boolean blkComment = false;
      int cnt = 0;
      int[] offsets = new int[2];
      boolean done = false;

      try {
         while (!done) {
            switch (ch = buffer.read()) {
               case -1: {
                  if (blkComment) {
                     offsets[1] = cnt;
                     blockComments.addElement(offsets);
                  }
                  done = true;
                  break;
               }
               case '/': {
                  ch = buffer.read();
                  if ((ch == '*') && (!blkComment)) {
                     offsets = new int[2];
                     offsets[0] = cnt;
                     blkComment = true;
                     cnt++;
                  } else {
                     cnt++;
                  }
                  cnt++;
                  break;
               }
               case '*': {
                  if (blkComment) {
                     ch = buffer.read();
                     cnt++;
                     if (ch == '/') {
                        blkComment = false;
                        offsets[1] = cnt;
                        blockComments.addElement(offsets);
                     }
                  }
                  cnt++;
                  break;
               }
               case '#': {
                  // TODO ruby comments
                  break;
               }
               default: {
                  cnt++;
                  break;
               }
            }
         }
      } catch (IOException e) {
         // ignore errors
      }
   }

   /**
    * A simple fuzzy scanner for Java
    */
   public class JavaScanner {

      protected Hashtable    fgKeys     = null;

      protected StringBuilder fBuffer    = new StringBuilder();

      protected String       fDoc;

      protected int          fPos;

      protected int          fEnd;

      protected int          fStartToken;

      protected boolean      fEofSeen   = false;
      private String[]       fgKeywords = { "assert", "enum", "abstract", "boolean", "break", "byte", "case", "catch", "char", "class", "continue", "default", "do", "double", "else", "extends", "false", "final", "finally", "float", "for", "if", "implements", "import", "instanceof", "int", "interface", "long",
                                              "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while", 
                                              "var", "script", "function", "src", "type", "language", "foreach", "using",
                                              "mx", "rest",
                                              "Imports", "Public", "Private", "Class", "Interface", "Shared", "End", "Sub", "Dim", "If", "Then", "Using", "Try", "Catch", "IsNot", "AndAlso", "Not", "Implements",
                                              "begin", "end", "puts", "require", "require_once", "echo", "php", "def", "print"};


      public JavaScanner() {
         initialize();
      }


      /**
       * Returns the ending location of the current token in the document.
       */
      public final int getLength(){
         return fPos - fStartToken;
      }


      /**
       * Initialize the lookup table.
       */
      void initialize(){
         fgKeys = new Hashtable();
         Integer k = new Integer(KEY_WORD);
         for (int i = 0; i < fgKeywords.length; i++)
            fgKeys.put(fgKeywords[i], k);
      }


      /**
       * Returns the starting location of the current token in the document.
       */
      public final int getStartOffset(){
         return fStartToken;
      }


      /**
       * Returns the next lexical token in the document.
       */
      public int nextToken(){
         int c;
         fStartToken = fPos;
         while (true) {
            switch (c = read()) {
               case EOF:
                  return EOF;
               case '/': // comment
                  c = read();
                  if (c == '/') {
                     while (true) {
                        c = read();
                        if ((c == EOF) || (c == EOL)) {
                           unread(c);
                           return COMMENT;
                        }
                     }
                  } else {
                     unread(c);
                  }
                  return OTHER;
               case '\'': // char const
                  character: for (;;) {
                     c = read();
                     switch (c) {
                        case '\'':
                           return STRING;
                        case EOF:
                           unread(c);
                           return STRING;
                        case '\\':
                           c = read();
                           break;
                     }
                  }

               case '"': // string
                  string: for (;;) {
                     c = read();
                     switch (c) {
                        case '"':
                           return STRING;
                        case EOF:
                           unread(c);
                           return STRING;
                        case '\\':
                           c = read();
                           break;
                     }
                  }

               case '0':
               case '1':
               case '2':
               case '3':
               case '4':
               case '5':
               case '6':
               case '7':
               case '8':
               case '9':
                  do {
                     c = read();
                  } while (Character.isDigit((char) c));
                  unread(c);
                  return NUMBER;
               default:
                  if (Character.isWhitespace((char) c)) {
                     do {
                        c = read();
                     } while (Character.isWhitespace((char) c));
                     unread(c);
                     return WHITE;
                  }
                  if (Character.isJavaIdentifierStart((char) c)) {
                     fBuffer.setLength(0);
                     do {
                        fBuffer.append((char) c);
                        c = read();
                     } while (Character.isJavaIdentifierPart((char) c));
                     unread(c);
                     Integer i = (Integer) fgKeys.get(fBuffer.toString());
                     if (i != null)
                        return i.intValue();
                     return WORD;
                  }
                  return OTHER;
            }
         }
      }


      /**
       * Returns next character.
       */
      protected int read(){
         if (fPos <= fEnd) {
            return fDoc.charAt(fPos++);
         }
         return EOF;
      }


      public void setRange( String text){
         fDoc = text;
         fPos = 0;
         fEnd = fDoc.length() - 1;
      }


      protected void unread( int c){
         if (c != EOF)
            fPos--;
      }
   }

}