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
package org.roussev.http4e.httpclient.core.client.misc;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Logs all data written to the wire LOG.
 *
 * @author <a href="mailto:oleg@ural.ru">Oleg Kalnichevski</a>
 * 
 * @since 2.0beta1
 */
public class ApacheOutputStream extends FilterOutputStream {

    /** Original input stream. */
    private OutputStream out;
    
    private ApacheHttpListener apacheHttpListener;

    /**
     * Create an instance that wraps the specified output stream.
     * @param out The output stream.
     * @param wire The Wire log to use.
     */
    public ApacheOutputStream(ApacheHttpListener apacheHttpListener, OutputStream out/*, Wire wire*/) {
        super(out);
        this.out = out;
        this.apacheHttpListener = apacheHttpListener;
    }
    
    /**
     * 
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    public void write(byte[] b, int off, int len) throws IOException {
        this.out.write(b,  off,  len);
        apacheHttpListener.write(b);
    }

    /**
     * 
     * @see java.io.OutputStream#write(byte[])
     */
    public void write(byte[] b) throws IOException {
        this.out.write(b);
        apacheHttpListener.write(b);
    }
}
