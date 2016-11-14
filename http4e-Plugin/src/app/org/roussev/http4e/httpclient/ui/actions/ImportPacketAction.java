package org.roussev.http4e.httpclient.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;
import org.roussev.http4e.httpclient.core.ExceptionHandler;
import org.roussev.http4e.httpclient.core.client.model.Item;
import org.roussev.http4e.httpclient.core.client.model.ItemModel;
import org.roussev.http4e.httpclient.core.client.view.FolderView;
import org.roussev.http4e.httpclient.core.util.Translator;
import org.roussev.http4e.httpclient.ui.HdViewPart;

public class ImportPacketAction extends Action {

   private ViewPart view;
   private Menu     fMenu;


   public ImportPacketAction( ViewPart view) {
      this.view = view;
      fMenu = null;
      setToolTipText("Import raw HTTP packet");
      setText("Import raw HTTP packet");
   }


   public void dispose(){
      // action is reused, can be called several times.
      if (fMenu != null) {
         fMenu.dispose();
         fMenu = null;
      }
   }

   public void run(){

      try {

         final ImportDialog dialog = new ImportDialog(view);
         MouseAdapter okListener = new MouseAdapter() {

            public void mouseUp( MouseEvent e){
               Item item = new Item();
               try {
                  item = Translator.httppacketToItem(dialog.getText());

               } catch (Exception exc) {
                  exc.printStackTrace();
               }

               FolderView folderView = ((HdViewPart) view).getFolderView();
               ItemModel iModel = new ItemModel(folderView.getModel(), item);
               HdViewPart hdViewPart = (HdViewPart) view;
               hdViewPart.getFolderView().buildTab(iModel);
            }
         };
         dialog.setOkListener(okListener);
         dialog.open();

      } catch (Exception e) {
         ExceptionHandler.handle(e);
      }
   }
}
