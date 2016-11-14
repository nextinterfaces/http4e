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
package org.roussev.http4e.httpclient.ui;

import java.io.File;
import java.io.FilenameFilter;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.roussev.http4e.httpclient.core.CoreContext;
import org.roussev.http4e.httpclient.core.CoreObjects;
import org.roussev.http4e.httpclient.ui.preferences.PreferenceConstants;

/**
 * Main plugin class.
 * 
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class HdPlugin extends AbstractUIPlugin implements UIConstants {

   // The shared instance
   private static HdPlugin plugin;
   private ResourceBundle  resourceBundle;


   public static String getResourceString( String key){
      ResourceBundle bundle = HdPlugin.getDefault().getResourceBundle();
      try {
         return (bundle != null) ? bundle.getString(key) : key;
      } catch (MissingResourceException e) {
         return key;
      }
   }


   public ResourceBundle getResourceBundle(){
      return resourceBundle;
   }


   /**
    * The constructor
    */
   public HdPlugin() {
      plugin = this;
      // init images
      // getImageRegistry();
   }


   public void start( BundleContext context) throws Exception{
      super.start(context);
      try {
         resourceBundle = ResourceBundle.getBundle(HdPlugin.class.getName());
      } catch (MissingResourceException x) {
         resourceBundle = null;
      }
      CoreContext.getContext().putObject("p", HdPlugin.getDefault());

      IPreferenceStore store = HdPlugin.getDefault().getPreferenceStore();
      int size = store.getInt(PreferenceConstants.P_PAYLOAD_VIEW_SIZE);
      if(size < 10){
         size = 50;
         store.setValue(PreferenceConstants.P_PAYLOAD_VIEW_SIZE, size);
      }
      CoreContext.getContext().putObject(CoreObjects.RESPONSE_VIEW_SIZE, size);
   }


   public void stop( BundleContext context) throws Exception{
      cleanupUserDirectoryPackets(".REQ.txt");
      cleanupUserDirectoryPackets(".RESP.txt");
      plugin = null;
      super.stop(context);
   }


   private static void cleanupUserDirectoryPackets( final String ext){

      FilenameFilter filter = new FilenameFilter() {

         public boolean accept( File dir, String name){
            return name.endsWith(ext);
         }
      };
      File dir = new File(CoreContext.PRODUCT_USER_DIR);
      String[] files = dir.list(filter);

      int len = files.length;
      for (int i = 0; i < len; i++) {
         File f = new File(dir, files[i]);
         f.delete();
      }
   }


   /**
    * Returns the shared instance
    * 
    * @return the shared instance
    */
   public static HdPlugin getDefault(){
      return plugin;
   }


   /**
    * Returns an image descriptor for the image file at the given plug-in
    * relative path
    * 
    * @param path
    *           the path
    * @return the image descriptor
    */
   public static ImageDescriptor getImageDescriptor( String path){
      return imageDescriptorFromPlugin(PLUGIN_ID, path);
   }

}
