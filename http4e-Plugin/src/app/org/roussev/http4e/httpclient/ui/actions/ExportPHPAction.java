package org.roussev.http4e.httpclient.ui.actions;

import java.io.StringWriter;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreImages;
import org.roussev.http4e.httpclient.core.ExceptionHandler;
import org.roussev.http4e.httpclient.core.client.model.ItemModel;
import org.roussev.http4e.httpclient.core.client.model.ModelEvent;
import org.roussev.http4e.httpclient.core.client.view.FolderView;
import org.roussev.http4e.httpclient.core.util.BaseUtils;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;
import org.roussev.http4e.httpclient.ui.HdViewPart;

public class ExportPHPAction extends Action {

   private ViewPart view;
   private Menu     fMenu;


   public ExportPHPAction( ViewPart view) {
      this.view = view;
      fMenu = null;
      setToolTipText("Export call as PHP script");
      setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, CoreImages.PHP)));
      setText("     PHP");
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
         FolderView folderView = ((HdViewPart) view).getFolderView();
         ItemModel iModel = folderView.getModel().getItemModel(new Integer(folderView.getSelectionItemHash()));
         iModel.fireExecute(new ModelEvent(ModelEvent.EXPORT, iModel));

         StringWriter writer = new StringWriter();
         BaseUtils.writePHP(iModel, writer);
         
         ExportDialog dialog = new ExportDialog(view, "PHP Curl HTTP Rest client", 
               "Your call exported as PHP Curl HTTP Rest client call.",
               writer.toString());
         dialog.open();
      } catch (Exception e) {
         ExceptionHandler.handle(e);
      }
   }

}
