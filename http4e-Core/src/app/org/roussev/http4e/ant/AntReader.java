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
package org.roussev.http4e.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.roussev.http4e.httpclient.core.util.FileCopy;


/**
 * This class moves bin artifacts during build. Used in ant.macros.xml.
 * 
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class AntReader {

   private final static String INCLUDES = "bin.includes";
   private final static List<String> BIN_EXCLUDE_LIST      = new ArrayList<String>(){
      {
         add(".");
      }
   };

   private final static List<String> FILECOPY_EXCLUDE_LIST = new ArrayList<String>() {
      {
         add(".svn");
         add(".SVN");
      }
   };
   
   
   public AntReader( String buildProp, String from, String to) throws Exception {

      Properties p = new Properties();
      InputStream inProp = new FileInputStream(buildProp);
      p.load(inProp);
      for (Object key : p.keySet()) {
         if (INCLUDES.equals(key)) {
            String csv = (String) p.get(key);
            StringTokenizer st = new StringTokenizer(csv, ",");
            String path;
            while (st.hasMoreTokens()) {
               path = st.nextToken();
               if (!BIN_EXCLUDE_LIST.contains(path)) {
                  System.out.println("path=" + path);
                  FileCopy.copy(
                        (from + File.separator + path), 
                        (to + File.separator + path), 
                        FILECOPY_EXCLUDE_LIST);
               }
            }
         }
      }
   }

   

   public static void main( String[] args) throws Exception{
      if (args.length < 3) {
         throw new IllegalArgumentException("'build.properties' path and 'copyToDir' not provided");
      }
      String buildProperties = args[0];
      String from = args[1];
      String to = args[2];

      new AntReader(buildProperties, from, to);
   }

}
