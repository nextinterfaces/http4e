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
package org.roussev.http4e.httpclient.core.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class SwtUtils {

   public static void centreWindow( Shell shell){
      Rectangle displayRect;
      try {
         displayRect = shell.getMonitor().getClientArea();
      } catch (NoSuchMethodError e) {
         displayRect = shell.getDisplay().getClientArea();
      }
      Rectangle shellRect = shell.getBounds();
      int x = (displayRect.width - shellRect.width) / 2;
      int y = (displayRect.height - shellRect.height) / 2;
      shell.setLocation(x, y);
   }


   public static boolean isMac(){
      String platform = SWT.getPlatform();
      if ("carbon".equals(platform) || "cocoa".equals(platform)) {
         return true;
      }

      // if ("win32".equals(platform) || "wpf".equals(platform)) {
      // // className = "org.eclipse.swt.browser.IE";
      // } else if ("motif".equals(platform)) {
      // // className = "org.eclipse.swt.browser.Mozilla";
      // } else if ("gtk".equals(platform)) {
      // // className = "org.eclipse.swt.browser.Mozilla";
      // } else if ("carbon".equals(platform) || "cocoa".equals(platform)) {
      // // className = "org.eclipse.swt.browser.Safari";
      // return true;
      // } else if ("photon".equals(platform)) {
      // // className = "org.eclipse.swt.browser.Voyager";
      // } else {
      // // dispose ();
      // // SWT.error (SWT.ERROR_NO_HANDLES);
      // }

      return false;
   }


   public static boolean isWindows(){
      String platform = SWT.getPlatform();
      if ("win32".equals(platform) || "wpf".equals(platform)) {
         return true;
      }
      return false;
   }

}
