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
package org.roussev.http4e.httpclient.core.misc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.roussev.http4e.httpclient.core.ExceptionHandler;
import org.roussev.http4e.httpclient.core.misc.Styles.FontStyle;


/**
 * This class retrieves http4e resources and caches them. The resources should be disposed when plugin is closed.
 * 
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class ResourceCache {

   private final Map imageMap    = new HashMap();
   private final Map fontMap     = new HashMap();
   private final Map colorMap    = new HashMap();
   private final Map cursorMap   = new HashMap();
   
   // ////////////////////////////
   // Images support
   // ////////////////////////////

   public Image getImage( ImageDescriptor imageDescriptor){
      if (imageDescriptor == null)
         return null;
      Image image = (Image) imageMap.get(imageDescriptor);
      if (image == null) {
         image = imageDescriptor.createImage();
         imageMap.put(imageDescriptor, image);
      }
      return image;
   }

   public Image getImage( String name){
      Image image = (Image) imageMap.get(name);
      if (image == null) {
         image = new Image(Display.getCurrent(), name);
         imageMap.put(name, image);
      }
      return image;
   }

   public void disposeImages(){
      for (Iterator iter = imageMap.values().iterator(); iter.hasNext();)
         ((Image)iter.next()).dispose();
      imageMap.clear();
   }
   
   
   // ////////////////////////////
   // Font support
   // ////////////////////////////
   /**
    * Returns a font based on its name, height and style
    * 
    * @param name String The name of the font
    * @param height int The height of the font
    * @param style int The style of the font
    * @return Font The font matching the name, height and style
    */
   public Font getFont( FontStyle fontStyle){
      return getFont(fontStyle.name, fontStyle.height, fontStyle.style, false, false);
   }   
   
   /**
    * Returns a font based on its name, height and style. Windows-specific
    * strikeout and underline flags are also supported.
    * 
    * @param name String The name of the font
    * @param size int The size of the font
    * @param style int The style of the font
    * @param strikeout boolean The strikeout flag (warning: Windows only)
    * @param underline boolean The underline flag (warning: Windows only)
    * @return Font The font matching the name, height, style, strikeout and
    *         underline
    */
   private Font getFont( String name, int size, int style, boolean strikeout, boolean underline){
      String fontName = name + '|' + size + '|' + style + '|' + strikeout + '|' + underline;
      Font font = (Font)fontMap.get(fontName);
      if (font == null) {
         FontData fontData = new FontData(name, size, style);
         if (strikeout || underline) {
            try {
               Class logFontClass = Class.forName("org.eclipse.swt.internal.win32.LOGFONT");
               Object logFont = FontData.class.getField("data").get(fontData);
               if (logFont != null && logFontClass != null) {
                  if (strikeout) {
                     logFontClass.getField("lfStrikeOut").set(logFont, new Byte((byte) 1));
                  }
                  if (underline) {
                     logFontClass.getField("lfUnderline").set(logFont, new Byte((byte) 1));
                  }
               }
            } catch (Throwable e) {
               ExceptionHandler.warn("Unable to set underline or strikeout" + " (probably on a non-Windows platform). " + e);
            }
         }
         font = new Font(Display.getCurrent(), fontData);
         fontMap.put(fontName, font);
      }
      return font;
   }

   /**
    * Dispose all of the cached fonts
    */
   public void disposeFonts(){
      for (Iterator iter = fontMap.values().iterator(); iter.hasNext();)
         ((Font)iter.next()).dispose();
      fontMap.clear();
   }

   
   // ////////////////////////////
   // Color support
   // ////////////////////////////

   public Color getColor(int red, int green, int blue){
      return getColor(new RGB(red, green, blue));
   }
   
   public Color getColor(RGB rgb){
      String key = rgb.blue + "|" + rgb.green + "|" + rgb.red;
      
      Color color = (Color)colorMap.get(key);
      if (color == null) {
         color = new Color(Display.getCurrent(), rgb);
         colorMap.put(key, color);
      }
      return color;
   }   
   
   public void disposeColors(){
      for (Iterator iter = colorMap.values().iterator(); iter.hasNext();)
         ((Color)iter.next()).dispose();
      colorMap.clear();
   }
   
   // ////////////////////////////
   // Cursor support
   // ////////////////////////////

   /**
    * Returns the system cursor matching the specific ID
    * 
    * @param id int The ID value for the cursor
    * @return Cursor The system cursor matching the specific ID
    */
   public Cursor getCursor( int id){
      Integer key = new Integer(id);
      Cursor cursor = (Cursor)cursorMap.get(key);
      if (cursor == null) {
         cursor = new Cursor(Display.getDefault(), id);
         cursorMap.put(key, cursor);
      }
      return cursor;
   }

   /**
    * Dispose all of the cached cursors
    */
   public void disposeCursors(){
      for (Iterator iter = cursorMap.values().iterator(); iter.hasNext();)
         ((Cursor)iter.next()).dispose();
      cursorMap.clear();
   }


   public void dispose(){
      disposeImages();
      disposeCursors();
      disposeFonts();
      disposeColors();
   }
   
}