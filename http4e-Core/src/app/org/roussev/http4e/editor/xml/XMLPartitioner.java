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
package org.roussev.http4e.editor.xml;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

/**
 * Simple extension of DefaultPartitioner with printPartitions() method to
 * assist with printing out partition information
 *  
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class XMLPartitioner extends FastPartitioner {

   public XMLPartitioner( IPartitionTokenScanner scanner, String[] legalContentTypes) {
      super(scanner, legalContentTypes);
   }


   public ITypedRegion[] computePartitioning( int offset, int length, boolean includeZeroLengthPartitions){
      return super.computePartitioning(offset, length, includeZeroLengthPartitions);
   }


   public void connect( IDocument document, boolean delayInitialization){
      super.connect(document, delayInitialization);
//      printPartitions(document);
   }


   public void printPartitions( IDocument document){
      StringBuilder buffer = new StringBuilder();

      ITypedRegion[] partitions = computePartitioning(0, document.getLength());
      for (int i = 0; i < partitions.length; i++) {
         try {
            buffer.append("Partition type: " + partitions[i].getType() + ", offset: " + partitions[i].getOffset() + ", length: " + partitions[i].getLength());
            buffer.append("\n");
            buffer.append("Text:\n");
            buffer.append(document.get(partitions[i].getOffset(), partitions[i].getLength()));
            buffer.append("\n---------------------------\n\n\n");
         } catch (BadLocationException e) {
            e.printStackTrace();
         }
      }
      System.out.print(buffer);
   }
}