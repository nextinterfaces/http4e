package org.roussev.http4e.httpclient.ui.actions;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreImages;
import org.roussev.http4e.httpclient.core.ExceptionHandler;
import org.roussev.http4e.httpclient.core.client.model.ItemModel;
import org.roussev.http4e.httpclient.core.util.BaseUtils;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;
import org.roussev.http4e.httpclient.ui.HdPlugin;
import org.roussev.http4e.httpclient.ui.HdViewPart;
import org.roussev.http4e.httpclient.ui.preferences.PreferenceConstants;

public class ImportHTTP4eAction extends Action {

   private ViewPart view;
   private Menu     fMenu;


   public ImportHTTP4eAction( ViewPart view) {
      this.view = view;
      fMenu = null;
      setToolTipText("Import HTTP4e script");
      setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, CoreImages.LOGO)));
      setText("Import HTTP4e script");
   }


   public void dispose(){
      // action is reused, can be called several times.
      if (fMenu != null) {
         fMenu.dispose();
         fMenu = null;
      }
   }


   // protected void addActionToMenu( Menu parent, Action action){
   // ActionContributionItem item = new ActionContributionItem(action);
   // item.fill(parent, -1);
   // }

   public void run(){

      try {

         FileDialog fileDialog = new FileDialog(view.getSite().getShell(), SWT.OPEN);

         fileDialog.setFileName("sessions.http4e");
         fileDialog.setFilterNames(new String[] { "HTTP4e File *.http4e" });
         fileDialog.setFilterExtensions(new String[] { "*.http4e" });
         fileDialog.setText("Open HTTP4e script");
         fileDialog.setFilterPath(getUserHomeDir());

         String path = fileDialog.open();
         if (path != null) {
            HdViewPart hdView = (HdViewPart) view;
            List<ItemModel> iteModels = BaseUtils.importHttp4eSessions(path, hdView.getFolderView().getModel());
            HdViewPart hdViewPart = (HdViewPart)view;
            for (ItemModel im : iteModels) {
               hdViewPart.getFolderView().buildTab(im);
            }
            
            updateUserHomeDir(path);
         }

      } catch (Exception e) {
         ExceptionHandler.handle(e);
      }
   }


   private String getUserHomeDir(){
      IPreferenceStore store = HdPlugin.getDefault().getPreferenceStore();
      String userDir = store.getString(PreferenceConstants.P_USER_HOME_DIR);
      if (BaseUtils.isEmpty(userDir)) {
         return System.getProperty("user.home");
      }
      return userDir;
   }


   private void updateUserHomeDir( String userDir){

      if (!BaseUtils.isEmpty(userDir)) {
         // Updating using Plugin directly persist the entry on each invokation
         // Plugin pl = (Plugin) CoreContext.getContext().getObject("p");
         // Preferences prefs = pl.getPluginPreferences();
         // prefs.setValue(PreferenceConstants.P_USER_HOME_DIR, userDir);
         // pl.savePluginPreferences();

         // Updating the preference "in memory" and persists "on Eclipse exit"
         IPreferenceStore store = HdPlugin.getDefault().getPreferenceStore();
         store.setValue(PreferenceConstants.P_USER_HOME_DIR, userDir);
      }
   }
}
