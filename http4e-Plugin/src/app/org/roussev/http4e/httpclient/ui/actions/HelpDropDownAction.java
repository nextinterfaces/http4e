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
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;


public class HelpDropDownAction extends Action implements IMenuCreator {

   public static final int RESULTS_IN_DROP_DOWN = 10;

   private ViewPart        view;
   private Menu            fMenu;
   private HelpAboutAction aboutAction;
   private ConfigAction  configAction;


   public HelpDropDownAction( ViewPart view) {
      this.view = view;
      fMenu = null;
      setToolTipText("About, License");
      setMenuCreator(this);
   }


   public void dispose(){
      // action is reused, can be called several times.
      if (fMenu != null) {
         fMenu.dispose();
         fMenu = null;
      }
   }


   public Menu getMenu( Menu parent){
      return null;
   }


   public Menu getMenu( Control parent){
      if (fMenu != null) {
         fMenu.dispose();
      }
      fMenu = new Menu(parent);
      // addActionToMenu(fMenu, new HowtoAction(view, "How-to, How-to"));
      // addActionToMenu(fMenu, new ContributeAction(view, "Contribute to
      // Plugin"));
      // new MenuItem(fMenu, SWT.SEPARATOR);
      addActionToMenu(fMenu, getAboutAction());
      addActionToMenu(fMenu, getConfigAction());
      return fMenu;
   }


   protected void addActionToMenu( Menu parent, Action action){
      ActionContributionItem item = new ActionContributionItem(action);
      item.fill(parent, -1);
   }


   private HelpAboutAction getAboutAction(){
      if (aboutAction == null) {
         aboutAction = new HelpAboutAction(view, "About");
      }
      return aboutAction;
   }

   private ConfigAction getConfigAction(){
      if (configAction == null) {
         configAction = new ConfigAction(view, "Preferences");
      }
      return configAction;
   }


   public void run(){
      getAboutAction().run();
   }
}
