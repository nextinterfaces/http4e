package org.roussev.http4e.httpclient.ui.actions;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.ViewPart;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreImages;
import org.roussev.http4e.httpclient.core.client.model.ItemModel;
import org.roussev.http4e.httpclient.core.client.model.ModelEvent;
import org.roussev.http4e.httpclient.core.client.view.FolderView;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;
import org.roussev.http4e.httpclient.core.util.shared.ExportJavaViewer;
import org.roussev.http4e.httpclient.ui.HdViewPart;

public class ExportDialog extends TitleAreaDialog {

   private ViewPart view;
   private String   title;
   private String   titleMessage;
   private String   source;


   public ExportDialog( ViewPart view, String title, String titleMessage, String source) {
      super(view.getViewSite().getShell());
      setTitleImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, CoreImages.LOGO_DIALOG));
      this.view = view;
      this.title = title;
      this.titleMessage = titleMessage;
      this.source = source;
   }

//   public boolean close(){
//      return super.close();
//   }

   protected Control createContents( Composite parent){
      Control contents = super.createContents(parent);
      setTitle(title);
      setMessage(titleMessage);
      return contents;
   }


   protected Control createDialogArea( Composite parent){
      Composite composite = (Composite) super.createDialogArea(parent);

      try {
         FolderView folderView = ((HdViewPart) view).getFolderView();
         ItemModel itemModel = folderView.getModel().getItemModel(new Integer(folderView.getSelectionItemHash()));
         itemModel.fireExecute(new ModelEvent(ModelEvent.EXPORT, itemModel));

         ExportJavaViewer example = new ExportJavaViewer(composite);
         example.open(source);

      } catch (Exception e) {
         setErrorMessage(e.getLocalizedMessage());
      }

      return composite;
   }


   protected void createButtonsForButtonBar( Composite parent){
      createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
   }

}
