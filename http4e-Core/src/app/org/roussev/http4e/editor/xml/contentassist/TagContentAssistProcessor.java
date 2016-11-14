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
package org.roussev.http4e.editor.xml.contentassist;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.roussev.http4e.editor.xml.scanners.XMLTagScanner;
import org.roussev.http4e.editor.xml.xmlattribs.XMLElement;
import org.roussev.http4e.editor.xml.xmlattribs.XMLTree;
import org.roussev.http4e.httpclient.core.ExceptionHandler;


/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class TagContentAssistProcessor implements IContentAssistProcessor {
    
    XMLTree               dtdTree = null;
    
    private XMLTagScanner scanner;
    
    public TagContentAssistProcessor( XMLTagScanner scanner) {
        
        super();
        this.dtdTree = new XMLTree();
        this.scanner = scanner;
        
    }
    
    @SuppressWarnings("unchecked")
   public ICompletionProposal[] computeCompletionProposals(
            ITextViewer viewer, int offset){
        
        IDocument document = viewer.getDocument();
        boolean isAttribute = isAttribute(offset, document);
        
        TextInfo currentText = currentText(document, offset);
        
        if( !isAttribute) {
            
            List allElements = dtdTree.getAllElements();
            
            ICompletionProposal[] result = new ICompletionProposal[allElements
                    .size()];
            int i = 0;
            for (Iterator iter = allElements.iterator(); iter.hasNext();) {
                XMLElement element = (XMLElement) iter.next();
                String name = element.getName();
                
                String text = null;
                
                if( useContractedElementCompletion(offset, document)) {
                    text = name;
                } else {
                    text = "" + name + ">" + "</" + name + ">";
                }
                
                result[i++] = new CompletionProposal(text,
                        currentText.documentOffset, currentText.text.length(),
                        text.length());
                
            }
            return result;
            
        } else {
            
            List allAttributes = dtdTree.getAllAttributes();
            
            ICompletionProposal[] result = new ICompletionProposal[allAttributes
                    .size()];
            int i = 0;
            for (Iterator iter = allAttributes.iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                
                String text = null;
                
                if( currentText.isWhiteSpace) {
                    text = name + "= \"\" ";
                } else {
                    text = name;
                }
                
                result[i++] = new CompletionProposal(text,
                        currentText.documentOffset, currentText.text.length(),
                        text.length());
            }
            return result;
        }
        
    }
    
    private TextInfo currentText( IDocument document, int documentOffset){
        
        try {
            
            ITypedRegion region = document.getPartition(documentOffset);
            
            int partitionOffset = region.getOffset();
            int partitionLength = region.getLength();
            
            int index = documentOffset - partitionOffset;
            
            String partitionText = document.get(partitionOffset,
                    partitionLength);
            
            char c = partitionText.charAt(index);
            
            if( Character.isWhitespace(c) || Character.isWhitespace(partitionText.charAt(index - 1))) {
                return new TextInfo("", documentOffset, true);
            } else if( c == '<') {
                return new TextInfo("", documentOffset, true);
            } else {
                int start = index;
                c = partitionText.charAt(start);
                
                while (!Character.isWhitespace(c) && c != '<' && start >= 0) {
                    start--;
                    c = partitionText.charAt(start);
                }
                start++;
                
                int end = index;
                c = partitionText.charAt(end);
                
                while (!Character.isWhitespace(c) && c != '>' && end < partitionLength - 1) {
                    end++;
                    c = partitionText.charAt(end);
                }
                
                String substring = partitionText.substring(start, end);
                return new TextInfo(substring, partitionOffset + start, false);
                
            }
            
        } catch (BadLocationException e) {
           ExceptionHandler.handle(e);
        }
        return null;
    }
    
    /**
     * Used to determine whether the current offset is an attribute. Will return
     * true if it finds any [text][whitespace][text] pattern between the within
     * the current partition
     */
    private boolean isAttribute( int documentOffset, IDocument document){
        
        boolean isAttribute = false;
        
        try {
            ITypedRegion region = document.getPartition(documentOffset);
            
            int partitionOffset = region.getOffset();
            
            int readLength = documentOffset - partitionOffset;
            scanner.setRange(document, partitionOffset, readLength);
            
            boolean textReached = false;
            
            IToken token = null;
            while ((token = scanner.nextToken()) != Token.EOF) {
                if( token.getData() instanceof TextAttribute) {
                    textReached = true;
                    continue;
                }
                
                if( textReached && token.isWhitespace()) {
                    isAttribute = true;
                }
                
            }
            
        } catch (BadLocationException e) {
           ExceptionHandler.handle(e);
        }
        return isAttribute;
    }
    
    /**
     * Used to determine whether there is any text after the current offset
     * within the same partition, excluding the current word Also returns true
     * if there is no white
     */
    private boolean useContractedElementCompletion( int documentOffset,
            IDocument document){
        
        boolean textReached = false;
        boolean isRemainingWhiteSpace = true;
        
        try {
            ITypedRegion region = document.getPartition(documentOffset);
            
            int partitionOffset = region.getOffset();
            int partitionLength = region.getLength();
            
            int readLength = documentOffset - partitionOffset;
            int remainingLength = partitionLength - readLength;
            
            if( document.getLength() >= documentOffset + 1) {
                String firstTwo = document.get(partitionOffset, 2);
                if( firstTwo.equals("<<"))
                    return false;
            }
            
            scanner.setRange(document, documentOffset, remainingLength);
            
            IToken token = null;
            while ((token = scanner.nextToken()) != Token.WHITESPACE
                    && token != Token.EOF) {
                isRemainingWhiteSpace = false;
                continue;
            }
            
            while ((token = scanner.nextToken()) == Token.WHITESPACE
                    && token != Token.EOF) {
                isRemainingWhiteSpace = true;
                continue;
            }
            
            char c = (char) 0;
            
            while ((c == scanner.read())) {
                if( c == XMLTagScanner.EOF)
                    break;
                if( c == '<') {
                    break;
                }
                if( !Character.isWhitespace(c))
                    textReached = true;
                
            }
            
        } catch (BadLocationException e) {
           ExceptionHandler.handle(e);
        }
        
        if( textReached)
            return true;
        if( !isRemainingWhiteSpace && !textReached)
            return true;
        else
            return false;
        
    }
    
    public IContextInformation[] computeContextInformation( ITextViewer viewer,
            int offset){
        return null;
    }
    
    public char[] getCompletionProposalAutoActivationCharacters(){
        return null;
    }
    
    public char[] getContextInformationAutoActivationCharacters(){
        return null;
    }
    
    public String getErrorMessage(){
        return null;
    }
    
    public IContextInformationValidator getContextInformationValidator(){
        return null;
    }
    
    static class TextInfo {
        TextInfo( String text, int documentOffset, boolean isWhiteSpace) {
            this.text = text;
            this.isWhiteSpace = isWhiteSpace;
            this.documentOffset = documentOffset;
        }
        
        String  text;
        
        boolean isWhiteSpace;
        
        int     documentOffset;
    }
    
}