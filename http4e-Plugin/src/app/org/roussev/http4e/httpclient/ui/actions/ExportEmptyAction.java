package org.roussev.http4e.httpclient.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

public class ExportEmptyAction extends Action {

   private Menu fMenu;

   public ExportEmptyAction( ViewPart view) {
      fMenu = null;
      setText("Export As ..");
      setEnabled(false);
   }

   public void dispose(){
      // action is reused, can be called several times.
      if (fMenu != null) {
         fMenu.dispose();
         fMenu = null;
      }
   }
}
