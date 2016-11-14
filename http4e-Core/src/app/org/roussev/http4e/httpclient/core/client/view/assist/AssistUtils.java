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

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreImages;
import org.roussev.http4e.httpclient.core.ExceptionHandler;
import org.roussev.http4e.httpclient.core.client.view.assist.Tracker.BlacklistStrategy;
import org.roussev.http4e.httpclient.core.misc.LazyObjects;
import org.roussev.http4e.httpclient.core.util.BaseUtils;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class AssistUtils {

   // "${header} = [${value}]"
   private final static String   CONTEXT_ID1   = "header-tmpl";
   private final static TemplateContextType   CTX_TYPE = new TemplateContextType(CONTEXT_ID1, "Header Template");
   private final static Template              TEMPLATE_HEADER   = new Template( "header",  "Insert new header", CONTEXT_ID1, "${header}=${value}",  true);
   private final static Template              TEMPLATE_PARAM    = new Template( "parameter",  "Insert new parameter", CONTEXT_ID1, "${param}=${value}",  true);
//   private final static Template              TEMPLATE_COMMENT  = new Template( "comment", "Insert comment",    CONTEXT_ID1, "# ${comment}", true);
   
   
   public static void doTemplateProposals( IDocument document, int offset, String qualifier, List proposalsList, boolean isHeaderProcessor){
      if (BaseUtils.isEmpty(qualifier)) {
         ICompletionProposal proposal;
         Region region = new Region(offset, 0);
         TemplateContext ctx1 = new DocumentTemplateContext(CTX_TYPE, document, offset, 0);
         Template topTmpl = TEMPLATE_PARAM;
         if(isHeaderProcessor){
            topTmpl = TEMPLATE_HEADER;
         }
         proposal = new TemplateProposal(topTmpl, ctx1, region, ResourceUtils.getImage(CoreConstants.PLUGIN_CORE, CoreImages.ASSIST_TEMPLATE));
         proposalsList.add(proposal);
         
//         TemplateContext ctx2 = new DocumentTemplateContext(CTX_TYPE, document, offset, 0);
//         proposal = new TemplateProposal(TEMPLATE_COMMENT, ctx2, region, ResourceUtils.getImage(CoreConstants.PLUGIN_CORE, CoreImages.ASSIST_TEMPLATE));
//         proposalsList.add(proposal);
      }
   }
   
   
   public static String getKeyWordToOffset(IDocument document, int offset){
      int currOffset = offset - 1;
      String currWord = "";
      try {
         char currChar;
         while (currOffset > 0 && !Character.isWhitespace(currChar = document.getChar(currOffset))) {
            currWord = currChar + currWord;
            currOffset--;
         }
      } catch (BadLocationException e) {
         ExceptionHandler.warn(e);
      }
      return currWord;
   }
   
   /**
    * Adding cached word tracks proposals
    */
   public static void doTrackProposals( Tracker wordTracker, String word, int offset, List proposalsList){

      List suggestions = wordTracker.suggest(word);
      if (suggestions.size() > 0) {
          buildWordTrackProposals(suggestions, word, offset - word.length(), proposalsList);
      }
    }
   
   
   /**
    * Adding http 1.1 headers proposals
    */
   public static void doHttpHeadersProposals( int offset, String qualifier, List proposalsList){
       List httpHeaders = LazyObjects.getHttpHeaders();
       int qlen = qualifier.length();
       
       for (Iterator iter = httpHeaders.iterator(); iter.hasNext();) {
         String text = (String) iter.next();
         if (text.toLowerCase().startsWith(qualifier.toLowerCase())) {
            int cursor = text.length();
            CompletionProposal cp = new CompletionProposal(text + AssistConstants.BRACKETS_COMPLETION, offset - qlen, qlen, cursor + 4, ResourceUtils.getImage(CoreConstants.PLUGIN_CORE, CoreImages.ASSIST_HEADER), text, null, LazyObjects.getInfoMap("Headers").getInfo(text));
            proposalsList.add(cp);
         }
       }
   }
   
   
   public static void doHttpH_ValuesProposals( String lineKey, int offset, String qualifier, List propList){

      ICompletionProposal proposal;
      int qlen = qualifier.length();
      int replacementOffset = offset - qlen;

      if(AssistConstants.HEADER_DATE.equalsIgnoreCase(lineKey) 
            || AssistConstants.HEADER_IF_MOD_SINCE.equalsIgnoreCase(lineKey)
            || AssistConstants.HEADER_RETRY_AFTER.equalsIgnoreCase(lineKey)
            || AssistConstants.HEADER_LAST_MODIFIED.equalsIgnoreCase(lineKey)
            || AssistConstants.HEADER_IF_UNMOD_SINCE.equalsIgnoreCase(lineKey)
            || AssistConstants.HEADER_EXPIRES.equalsIgnoreCase(lineKey)){
         Date today = new Date();
         String replaceStr = today.toString();
         proposal = new CompletionProposal(replaceStr, replacementOffset, qlen, replaceStr.length() + 1, ResourceUtils.getImage(CoreConstants.PLUGIN_CORE, CoreImages.ASSIST_HEADER), replaceStr, null, null);
         propList.add(proposal);
         
      } else {
         Collection valsForHeader = LazyObjects.getValuesForHeader(lineKey);
         for (Iterator iter = valsForHeader.iterator(); iter.hasNext();) {
            String val = (String) iter.next();
            if (val.toLowerCase().startsWith(qualifier.toLowerCase())) {
               int cursor = val.length();
               proposal = new CompletionProposal(val, offset - qlen, qlen, cursor + 1, ResourceUtils.getImage(CoreConstants.PLUGIN_CORE, CoreImages.ASSIST_HEADER), val, null, LazyObjects.getInfoMap(lineKey).getInfo(val));
               propList.add(proposal);
            }
         }
      }
   }

   
   
   private static void buildWordTrackProposals( List suggestions, String replacedWord, int offset, List proposalsList){
      int index = 0;
      for (Iterator i = suggestions.iterator(); i.hasNext();) {
         String currSuggestion = (String) i.next();
         proposalsList.add( new CompletionProposal(currSuggestion, offset, replacedWord.length(), currSuggestion.length(), ResourceUtils.getImage(CoreConstants.PLUGIN_CORE, CoreImages.ASSIST_HEADER_CACHED), currSuggestion, null, LazyObjects.getInfoMap("Headers").getInfo(currSuggestion)));
         index++;
      }
   }
   
   
   public static void addTrackWords(String text, IDocument doc, int offset, ModelTrackerListener modelListener){
      if ( DocumentUtils.isNL(text)) {
         String newWord = DocumentUtils.findMostRecentWord(offset, doc);
         newWord = newWord.trim();
         if(newWord.length() > 0 && !DocumentUtils.isComment(newWord)){
            final String key = DocumentUtils.getKeyFromLine(newWord);
            final String val = DocumentUtils.getValueFromLine(newWord);
            Tracker masterTracker = LazyObjects.getHeaderTracker();
            if(key.length() > 0) {
               if(modelListener!= null) modelListener.fireExecute(key, val);
               masterTracker.add(key);
               
               if(val.length() > 0) {
                  Tracker childTracker = masterTracker.getChildTracker(key);
                  childTracker.setBlacklistStrategy(new BlacklistStrategy(){
                     public boolean isBlacklisted( String word){
                        Collection blackList = LazyObjects.getValuesForHeader(key);
                        return blackList.contains(word);
                     }
                  });
                  childTracker.add(val);
               }
            }
         }
      }
   }
   
}


