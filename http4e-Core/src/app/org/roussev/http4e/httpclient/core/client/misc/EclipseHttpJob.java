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

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.progress.IProgressConstants;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class EclipseHttpJob extends Job {
    
   /**
    * A family identifier for all grouped jobs
    */
   public static final Object FAMILY_JOB = new Object();

   /**
    * Total duration that the test job should sleep, in milliseconds.
    */
   private long               duration;

   /**
    * Whether the test job should fail.
    */
   private boolean            failure;

   /**
    * Whether the job should report unknown progress.
    */
   private boolean            unknown;

   public EclipseHttpJob( long duration, boolean lock, /*boolean failure,*/ boolean indeterminate/*, boolean reschedule, long rescheduleWait*/) {
      super("http4e loading..");
      this.duration = duration;
//      this.failure = failure;
      this.unknown = indeterminate;
//      this.reschedule = reschedule;
//      this.rescheduleWait = rescheduleWait;
      if (lock)
         setRule(ResourcesPlugin.getWorkspace().getRoot());
      
      setProperty(IProgressConstants.KEEP_PROPERTY, Boolean.valueOf("true"));
      setProperty(IProgressConstants.KEEPONE_PROPERTY, Boolean.valueOf("true"));
      schedule(10);
   }

   public boolean belongsTo( Object family){
//      if (family instanceof TestJob) {
//         return true;
//      }
      return family == FAMILY_JOB;
   }

   protected IStatus run( IProgressMonitor monitor){
      if (failure) {
         MultiStatus result = new MultiStatus("org.eclipse.ui.examples.jobs", 1, "http4e MultiStatus message", new RuntimeException("http4e MultiStatus exception"));
         result.add(new Status(IStatus.ERROR, "org.eclipse.ui.examples.jobs", 1, "http4e child status message", new RuntimeException("http4e child exception")));
         return result;
      }
      final long sleep = 10;
      int ticks = (int) (duration / sleep);
      if (this.unknown)
         monitor.beginTask(toString(), IProgressMonitor.UNKNOWN);
      else
         monitor.beginTask(toString(), ticks);
      try {
         for (int i = 0; i < ticks; i++) {
            if (monitor.isCanceled()) {
               return Status.CANCEL_STATUS;
            }
            monitor.subTask("Processing tick #" + i);
            try {
               Thread.sleep(sleep);
            } catch (InterruptedException e) {
               return Status.CANCEL_STATUS;
            }
            monitor.worked(10);
         }
      } finally {
//         if (reschedule)
//            schedule(rescheduleWait);
         monitor.done();
      }
      return Status.OK_STATUS;
   }

}
