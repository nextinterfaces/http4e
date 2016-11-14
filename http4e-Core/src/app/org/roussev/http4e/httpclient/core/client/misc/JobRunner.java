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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class JobRunner implements Runnable {

   private boolean     done = false;
   private JobListener listener;
   private Composite   composite;

   public JobRunner( Composite composite, JobListener listener) {
      this.listener = listener;
      this.composite = composite;
   }

   public void run(){
      // main swt thread
      final Display display = Display.getDefault();

      // start separate thread
      Thread separateThread = new Thread(new Runnable() {
         public void run(){

            if (display.isDisposed()) return;
            final Object execObj = listener.execute();

            if (display.isDisposed()) return;
            display.syncExec(new Runnable() {
               public void run(){
                  // sync back to main swt thread
                  if (listener.isDisposed())
                     return;
                  listener.update(execObj);
               }
            });
            done = true;

            if (display.isDisposed()) return;
            display.wake();
         }
      });
      separateThread.start();

      while (!done && !composite.isDisposed()) {
         if (!display.readAndDispatch())
            display.sleep();
      }
   }

   public boolean isFinished(){
      return done;
   }

}
