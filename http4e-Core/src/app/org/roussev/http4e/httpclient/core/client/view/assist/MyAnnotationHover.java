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

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class MyAnnotationHover implements IAnnotationHover {
    
    public String getHoverInfo( ISourceViewer sourceViewer, int lineNumber){
        IDocument document = sourceViewer.getDocument();        
        try {
            IRegion info = document.getLineInformation(lineNumber);
            return document.get(info.getOffset(), info.getLength());
        } catch (BadLocationException x) {
        }
        return null;
    }
    
}
