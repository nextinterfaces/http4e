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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.roussev.http4e.httpclient.core.CoreConstants;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class Tracker {  
   
   //-------------------
   public interface BlacklistStrategy {      
      boolean isBlacklisted(String word);      
   }
   //-------------------

   public final static String  MASTER_ID              = null;

   private int  maxQueueSize;
   private List         keyBuffer;
   private Collection   knownKeys    = new HashSet();
   private String id = null;
   // String(key), Tracker(values-for-key)
   private Map childTrackers = new HashMap();
   private BlacklistStrategy blacklistStrategy = new BlacklistStrategy(){
      public boolean isBlacklisted( String word){
         return false;
      }
   };


   public Tracker( String id, int queueSize) {
      this.id = id;
      this.maxQueueSize = queueSize;
      this.keyBuffer = new LinkedList();
   }
   
   
   public BlacklistStrategy getBlacklistStrategy(){
      return blacklistStrategy;
   }
   
   
   public void setBlacklistStrategy( BlacklistStrategy blacklistStrategy){
      this.blacklistStrategy = blacklistStrategy;
   }
   
   
   public Tracker getChildTracker(String key){
      if(key == null || "".equals(key.trim())){
         throw new IllegalArgumentException("Key is empty");
      }
      Tracker vals = (Tracker)childTrackers.get(key);
      if(vals == null){
         vals = new Tracker(key, CoreConstants.MAX_TRACKS_VALUES);
         childTrackers.put(key, vals);
      }
      return vals;
   }
   
   
   public Collection getChildTrackers(){
      List kids = new ArrayList();
      for (Iterator iter = childTrackers.keySet().iterator(); iter.hasNext();) {
         String key = (String) iter.next();
         kids.add(childTrackers.get(key));
      }
      return kids;
   }
   
   /**
    * 
    * @return true if this tracker is value tracker, otherwise is key tracker
    */
   public boolean isMasterTracker(){
      return (id == MASTER_ID);
   }
   
      

   public int getWordCount(){
      return keyBuffer.size();
   }
   

   public void add( String word){
      if (wordIsNotKnown(word) && !getBlacklistStrategy().isBlacklisted(word)) {
         flushOldestWord();
         insertNewWord(word);
      }
   }
   

   private void insertNewWord( String word){
      keyBuffer.add(0, word);
      knownKeys.add(word);
   }
   

   private void flushOldestWord(){
      if (keyBuffer.size() == maxQueueSize) {
         String removedWord = (String) keyBuffer.remove(maxQueueSize - 1);
         knownKeys.remove(removedWord);
      }
   }
   

   private boolean wordIsNotKnown( String word){
      return !knownKeys.contains(word);
   }
   

   public List suggest( String word){
      List suggestions = new LinkedList();
      String currWord;
      for (Iterator i = keyBuffer.iterator(); i.hasNext();) {
         currWord = (String) i.next();
         if (currWord.startsWith(word)) {
            suggestions.add(currWord);
         }
      }
      return suggestions;
   }


   public String toString(){
      return "{" + keyBuffer +
            "," + childTrackers +
            "}";
   }
   
   
//   public static void main( String[] args){
//      Collection exclList = new HashSet();
//      exclList.add("            q we");
//      exclList.add("asd");
//      System.out.println(exclList.contains("            q we"));
//      System.out.println(exclList.contains("q we"));
//      System.out.println(exclList.contains("ggggggggg"));
//   }
    
}