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
package org.roussev.http4e.httpclient.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.part.ViewPart;

/**
 * Action used for the open historical input again in DependenciesView
 * 
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class HelpAboutAction extends Action {

   private ViewPart view;

   public HelpAboutAction( ViewPart view, String title) {
      super();
      this.view = view;
      setText(title);
      // setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreImages.HELP)));
      // setDisabledImageDescriptor(PDEPluginImages.DESC_PLUGIN_OBJ);

      setDescription(title);
      setToolTipText(title);
   }

   public void run(){
      AboutDialog dialog = new AboutDialog(view.getViewSite().getShell());
      dialog.open();
   }

}
