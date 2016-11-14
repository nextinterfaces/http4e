package org.json.me;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.ContextInformationValidator;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class Ch5CompletionEditor extends Composite {

   private TextViewer       textViewer;

   private WordTracker      wordTracker;

   private static final int MAX_QUEUE_SIZE = 200;


   public Ch5CompletionEditor( Composite parent) {
      super(parent, SWT.NULL);
      wordTracker = new WordTracker(MAX_QUEUE_SIZE);
      buildControls();
   }


   private void buildControls(){
      setLayout(new FillLayout());
      textViewer = new TextViewer(this, SWT.MULTI | SWT.V_SCROLL);

      textViewer.setDocument(new Document());

      final ContentAssistant assistant = new ContentAssistant();
      assistant.setContentAssistProcessor(new RecentWordContentAssistProcessor(wordTracker), IDocument.DEFAULT_CONTENT_TYPE);

      assistant.install(textViewer);

      textViewer.getControl().addKeyListener(new KeyAdapter() {

         public void keyPressed( KeyEvent e){
            switch (e.keyCode) {
               case SWT.F1:
                  assistant.showPossibleCompletions();
                  break;
               default:
                  // ignore everything else
            }
         }
      });

      textViewer.addTextListener(new ITextListener() {

         public void textChanged( TextEvent e){
            if (isWhitespaceString(e.getText())) {
               wordTracker.add(findMostRecentWord(e.getOffset() - 1));
            }
         }
      });
   }


   protected String findMostRecentWord( int startSearchOffset){
      int currOffset = startSearchOffset;
      char currChar;
      String word = "";
      try {
         while (currOffset > 0 && !Character.isWhitespace(currChar = textViewer.getDocument().getChar(currOffset))) {
            word = currChar + word;
            currOffset--;
         }
         return word;
      } catch (BadLocationException e) {
         e.printStackTrace();
         return null;
      }
   }


   protected boolean isWhitespaceString( String string){
      StringTokenizer tokenizer = new StringTokenizer(string);
      // if there is at least 1 token, this string is not whitespace
      return !tokenizer.hasMoreTokens();
   }

}

class WordTracker {

   private int  maxQueueSize;

   private List wordBuffer;

   private Map  knownWords = new HashMap();


   public WordTracker( int queueSize) {
      maxQueueSize = queueSize;
      wordBuffer = new LinkedList();
   }


   public int getWordCount(){
      return wordBuffer.size();
   }


   public void add( String word){
      if (wordIsNotKnown(word)) {
         flushOldestWord();
         insertNewWord(word);
      }
   }


   private void insertNewWord( String word){
      wordBuffer.add(0, word);
      knownWords.put(word, word);
   }


   private void flushOldestWord(){
      if (wordBuffer.size() == maxQueueSize) {
         String removedWord = (String) wordBuffer.remove(maxQueueSize - 1);
         knownWords.remove(removedWord);
      }
   }


   private boolean wordIsNotKnown( String word){
      return knownWords.get(word) == null;
   }


   public List suggest( String word){
      List suggestions = new LinkedList();
      for (Iterator i = wordBuffer.iterator(); i.hasNext();) {
         String currWord = (String) i.next();
         if (currWord.startsWith(word)) {
            suggestions.add(currWord);
         }
      }
      return suggestions;
   }

}

class RecentWordContentAssistProcessor implements IContentAssistProcessor {

   private String                       lastError = null;

   private IContextInformationValidator contextInfoValidator;

   private WordTracker                  wordTracker;


   public RecentWordContentAssistProcessor( WordTracker tracker) {
      super();
      contextInfoValidator = new ContextInformationValidator(this);
      wordTracker = tracker;
   }


   public ICompletionProposal[] computeCompletionProposals( ITextViewer textViewer, int documentOffset){
      IDocument document = textViewer.getDocument();
      int currOffset = documentOffset - 1;

      try {
         String currWord = "";
         char currChar;
         while (currOffset > 0 && !Character.isWhitespace(currChar = document.getChar(currOffset))) {
            currWord = currChar + currWord;
            currOffset--;
         }

         List suggestions = wordTracker.suggest(currWord);
         ICompletionProposal[] proposals = null;
         if (suggestions.size() > 0) {
            proposals = buildProposals(suggestions, currWord, documentOffset - currWord.length());
            lastError = null;
         }
         return proposals;
      } catch (BadLocationException e) {
         e.printStackTrace();
         lastError = e.getMessage();
         return null;
      }
   }


   private ICompletionProposal[] buildProposals( List suggestions, String replacedWord, int offset){
      ICompletionProposal[] proposals = new ICompletionProposal[suggestions.size()];
      int index = 0;
      for (Iterator i = suggestions.iterator(); i.hasNext();) {
         String currSuggestion = (String) i.next();
         proposals[index] = new CompletionProposal(currSuggestion, offset, replacedWord.length(), currSuggestion.length());
         index++;
      }
      return proposals;
   }


   public IContextInformation[] computeContextInformation( ITextViewer textViewer, int documentOffset){
      lastError = "No Context Information available";
      return null;
   }


   public char[] getCompletionProposalAutoActivationCharacters(){
      // we always wait for the user to explicitly trigger completion
      return null;
   }


   public char[] getContextInformationAutoActivationCharacters(){
      // we have no context information
      return null;
   }


   public String getErrorMessage(){
      return lastError;
   }


   public IContextInformationValidator getContextInformationValidator(){
      return contextInfoValidator;
   }
}
