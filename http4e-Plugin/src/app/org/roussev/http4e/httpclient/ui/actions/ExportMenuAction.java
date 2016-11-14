package org.roussev.http4e.httpclient.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.part.ViewPart;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreImages;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;

public class ExportMenuAction extends Action implements IMenuCreator {

   private ViewPart    view;
   private Menu        menu;
   private Action      javaHttp3Action;
   private Action      javaHttp4Action;
   private Action      jmeterAction;
   private Action      prototypeAction;
   private Action      jqueryAction;
   private Action      jsAction;
   private Action      csAction;
   private Action      vbAction;
   private Action      flexAction;
   private Action      rubyAction;
   private Action      phpAction;
   private Action      ocAction;
   private Action      pythonAction;
   private Action      http4eAction;
   private Action      emptyAction;
   private PrintAction printAction;


   public ExportMenuAction( ViewPart view) {
      this.view = view;
      menu = null;
      setToolTipText("Export HTTP call to different languages");
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
      // addActionToMenu(fMenu, new HowtoAction(view, "How-to, How-to"));
      // addActionToMenu(fMenu, new ContributeAction(view, "Contribute to
      // Plugin"));
      // new MenuItem(fMenu, SWT.SEPARATOR);
      addActionToMenu(menu, getHTTP4eAction());
      addActionToMenu(menu, getJMeterAction());
      addActionToMenu(menu, getPrintAction());
      new MenuItem(menu, SWT.SEPARATOR);
      addActionToMenu(menu, getEmptyAction());
      addActionToMenu(menu, getJavaHttp4Action());
      addActionToMenu(menu, getJavaHttp3Action());
      addActionToMenu(menu, getJSAction());
      addActionToMenu(menu, getPrototypeAction());
      addActionToMenu(menu, getJqueryAction());
      addActionToMenu(menu, getFlexAction());
      addActionToMenu(menu, getCsAction());
      addActionToMenu(menu, getVbAction());
      addActionToMenu(menu, getRubyAction());
      addActionToMenu(menu, getPythonAction());
      addActionToMenu(menu, getPHPAction());
      addActionToMenu(menu, getOCAction());
      return menu;
   }


   protected void addActionToMenu( Menu parent, Action action){
      ActionContributionItem item = new ActionContributionItem(action);
      item.fill(parent, -1);
   }


   private Action getPrintAction(){
      printAction = new PrintAction(view, "Print /Save PDF");
      printAction.setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, CoreImages.PRINT)));

      return printAction;
   }


   private Action getJavaHttp3Action(){
      if (javaHttp3Action == null) {
         javaHttp3Action = new ExportJavaHttp3Action(view);
      }
      return javaHttp3Action;
   }


   private Action getJavaHttp4Action(){
      if (javaHttp4Action == null) {
         javaHttp4Action = new ExportJavaHttp4Action(view);
      }
      return javaHttp4Action;
   }


   private Action getJMeterAction(){
      if (jmeterAction == null) {
         jmeterAction = new ExportJMeterAction(view);
      }
      return jmeterAction;
   }


   private Action getPrototypeAction(){
      if (prototypeAction == null) {
         prototypeAction = new ExportAjaxPrototypeAction(view);
      }
      return prototypeAction;
   }


   private Action getJqueryAction(){
      if (jqueryAction == null) {
         jqueryAction = new ExportAjaxJQueryAction(view);
      }
      return jqueryAction;
   }


   private Action getCsAction(){
      if (csAction == null) {
         csAction = new ExportCsAction(view);
      }
      return csAction;
   }


   private Action getVbAction(){
      if (vbAction == null) {
         vbAction = new ExportVbAction(view);
      }
      return vbAction;
   }


   private Action getFlexAction(){
      if (flexAction == null) {
         flexAction = new ExportFlexAction(view);
      }
      return flexAction;
   }


   private Action getRubyAction(){
      if (rubyAction == null) {
         rubyAction = new ExportRubyAction(view);
      }
      return rubyAction;
   }


   private Action getPHPAction(){
      if (phpAction == null) {
         phpAction = new ExportPHPAction(view);
      }
      return phpAction;
   }


   private Action getOCAction(){
      if (ocAction == null) {
         ocAction = new ExportCocoaAction(view);
      }
      return ocAction;
   }


   private Action getHTTP4eAction(){
      if (http4eAction == null) {
         http4eAction = new ExportHTTP4eAction(view);
      }
      return http4eAction;
   }


   private Action getJSAction(){
      if (jsAction == null) {
         jsAction = new ExportAjaxJSAction(view);
      }
      return jsAction;
   }


   private Action getEmptyAction(){
      if (emptyAction == null) {
         emptyAction = new ExportEmptyAction(view);
      }
      return emptyAction;
   }


   private Action getPythonAction(){
      if (pythonAction == null) {
         pythonAction = new ExportPythonAction(view);
      }
      return pythonAction;
   }


   public void run(){
      getHTTP4eAction().run();
   }
}
