package org.roussev.http4e.httpclient.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

public class ImportMenuAction extends Action implements IMenuCreator {

   private ViewPart view;
   private Menu     menu;
   private Action   http4eAction;
   private Action   liveHeadersAction;
   private Action   packetAction;


   public ImportMenuAction( ViewPart view) {
      this.view = view;
      menu = null;
      setToolTipText("Import HTTP packets");
      setMenuCreator(this);
   }


   public void dispose(){
      // action is reused, can be called several times.
      if (menu != null) {
         menu.dispose();
         menu = null;
      }
   }


   public Menu getMenu( Menu parent){
      return null;
   }


   public Menu getMenu( Control parent){
      if (menu != null) {
         menu.dispose();
      }
      menu = new Menu(parent);
      addActionToMenu(menu, getPacketAction());
      addActionToMenu(menu, getHTTP4eAction());
      addActionToMenu(menu, getLiveHeadersAction());
      return menu;
   }


   protected void addActionToMenu( Menu parent, Action action){
      ActionContributionItem item = new ActionContributionItem(action);
      item.fill(parent, -1);
   }


   private Action getLiveHeadersAction(){
      if (liveHeadersAction == null) {
         liveHeadersAction = new ImportLiveHttpHeadersAction(view);
      }
      return liveHeadersAction;
   }


   private Action getPacketAction(){
      if (packetAction == null) {
         packetAction = new ImportPacketAction(view);
      }
      return packetAction;
   }


   private Action getHTTP4eAction(){
      if (http4eAction == null) {
         http4eAction = new ImportHTTP4eAction(view);
      }
      return http4eAction;
   }


   public void run(){
      getHTTP4eAction().run();
   }
}
