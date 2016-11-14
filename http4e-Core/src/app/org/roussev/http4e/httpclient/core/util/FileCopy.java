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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class FileCopy {
   

   public static void copy( String from, String to, List excludeList) throws IOException{
      File src = new File(from);

      if (excludeList != null && excludeList.contains(src.getName())) {
         return;
      }
      //System.out.println("-- Copying: " + from + " to: " + to);

      if (!src.exists()) {
         throw new RuntimeException("File doesn't exist '" + from + "'");
      }
      if (src.isDirectory()) {
         File dirCreate = new File(to);
         if (!dirCreate.exists()) {
            dirCreate.mkdirs();
         }
         File[] srcFiles = src.listFiles();
         for (int i = 0; i < srcFiles.length; i++) {
            File srcF = srcFiles[i];
            if (srcF.isDirectory()) {
               FileCopy.copy(srcF.getPath(), to, excludeList);
            } else {
               File fileCreate = new File(to + File.separator + srcF.getName());
               FileCopy.copyFile(srcF, fileCreate);
            }
         }

      } else {
         File fileCreate = new File(to);
         fileCreate.getParentFile().mkdirs();
         FileCopy.copyFile(src, fileCreate);
      }
   }

   public static void copyFile( File src, File dist) throws IOException{

      InputStream in = null;
      OutputStream out = null;
      try {
         dist.createNewFile();
         in = new FileInputStream(src);
         out = new FileOutputStream(dist);

         // Transfer bytes from in to out
         byte[] buf = new byte[1024];
         int len;
         while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
         }

      } finally {
         try {
            if (in != null)
               in.close();
            if (out != null)
               out.close();
         } catch (IOException ignore) {
         }
      }
   }

}
