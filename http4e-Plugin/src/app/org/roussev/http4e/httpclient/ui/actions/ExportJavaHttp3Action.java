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

public class ExportJavaHttp3Action extends Action {


   private ViewPart view;
   private Menu     fMenu;


   public ExportJavaHttp3Action( ViewPart view) {
      this.view = view;
      fMenu = null;
      setToolTipText("Export call as Apache HttpClient 3 script");
      setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, CoreImages.JAVA)));
      setText("     Apache HttpClient 3");
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
         BaseUtils.writeJavaHttpClient3(iModel, writer);
         
         ExportDialog dialog = new ExportDialog(view, "Apache HTTP Client 3.x call", 
               "Your call exported as Java Apache HTTP Client 3.x call.",
               writer.toString());
         dialog.open();
      } catch (Exception e) {
         ExceptionHandler.handle(e);
      }
   }

   // private static final String EDITOR_ID =
   // "org.eclipse.ui.internal.ide.dialogs.WelcomeEditor";
   // private IWorkbenchWindow workbenchWindow =
   // PlatformUI.getWorkbench().getActiveWorkbenchWindow();
   //
   //
   // /**
   // * Opens the welcome page for a feature.
   // *
   // * @param feature
   // * the about info for the feature
   // * @return <code>true</code> if successful, <code>false</code> otherwise
   // */
   // private boolean openWelcomePage( AboutInfo feature){
   // IWorkbenchPage page = null;
   // // See if the feature wants a specific perspective
   // String perspectiveId = feature.getWelcomePerspectiveId();
   //
   // if (perspectiveId == null) {
   // // Just use the current perspective unless one is not open
   // // in which case use the default
   // page = workbenchWindow.getActivePage();
   //
   // if (page == null || page.getPerspective() == null) {
   // perspectiveId =
   // PlatformUI.getWorkbench().getPerspectiveRegistry().getDefaultPerspective();
   // }
   // }
   //
   // if (perspectiveId != null) {
   // try {
   // page = PlatformUI.getWorkbench().showPerspective(perspectiveId,
   // workbenchWindow);
   // } catch (WorkbenchException e) {
   // IDEWorkbenchPlugin.log("Error opening perspective: " + perspectiveId,
   // e.getStatus());
   // return false;
   // }
   // }
   //
   // if (page == null) {
   // return false;
   // }
   //
   // page.setEditorAreaVisible(true);
   //
   // // create input
   // WelcomeEditorInput input = new WelcomeEditorInput(feature);
   //
   // // see if we already have a welcome editorz
   // IEditorPart editor = page.findEditor(input);
   // if (editor != null) {
   // page.activate(editor);
   // return true;
   // }
   //
   // try {
   // WorkbenchPage page2;
   // page.openEditor(input, EDITOR_ID);
   // } catch (PartInitException e) {
   // IDEWorkbenchPlugin.log("Error opening welcome editor for feature: " +
   // feature.getFeatureId(), e);
   // IStatus status = new Status(IStatus.ERROR,
   // IDEWorkbenchPlugin.IDE_WORKBENCH, 1,
   // IDEWorkbenchMessages.QuickStartAction_openEditorException, e);
   // ErrorDialog.openError(workbenchWindow.getShell(),
   // IDEWorkbenchMessages.Workbench_openEditorErrorDialogTitle,
   // IDEWorkbenchMessages.Workbench_openEditorErrorDialogMessage, status);
   // return false;
   // }
   // return true;
   // }
   //
   //
   // /**
   // * Prompts the user for a feature that has a welcome page.
   // *
   // * @return the chosen feature, or <code>null</code> if none was chosen
   // */
   // private AboutInfo promptForFeature() throws WorkbenchException{
   // // Ask the user to select a feature
   // ArrayList welcomeFeatures = new ArrayList();
   //
   // URL productUrl = null;
   // IProduct product = Platform.getProduct();
   // if (product != null) {
   // productUrl = ProductProperties.getWelcomePageUrl(product);
   // welcomeFeatures.add(new AboutInfo(product));
   // }
   // // try {
   // // productUrl = new URL("http://qwe.com");
   // // } catch (MalformedURLException e) {
   // // e.printStackTrace();
   // // }
   // // System.err.println("ExportJavaHttp3Action.promptForFeature: productUrl:
   // "
   // // + productUrl);
   //
   // AboutInfo[] features = IDEWorkbenchPlugin.getDefault().getFeatureInfos();
   // for (int i = 0; i < features.length; i++) {
   // URL url = features[i].getWelcomePageURL();
   // try {
   // url = new URL("http://asd.com");
   // } catch (MalformedURLException e) {
   // e.printStackTrace();
   // }
   // System.err.println("ExportJavaHttp3Action.promptForFeature: url: " + url);
   // if (url != null && !url.equals(productUrl)) {
   // welcomeFeatures.add(features[i]);
   // }
   // }
   //
   // Shell shell = workbenchWindow.getShell();
   //
   // if (welcomeFeatures.size() == 0) {
   // MessageDialog.openInformation(shell,
   // IDEWorkbenchMessages.QuickStartMessageDialog_title,
   // IDEWorkbenchMessages.QuickStartMessageDialog_message);
   // return null;
   // }
   //
   // features = new AboutInfo[welcomeFeatures.size()];
   // welcomeFeatures.toArray(features);
   //
   // FeatureSelectionDialog d = new FeatureSelectionDialog(shell, features,
   // product == null ? null : product.getId(),
   // IDEWorkbenchMessages.WelcomePageSelectionDialog_title,
   // IDEWorkbenchMessages.WelcomePageSelectionDialog_message,
   // IIDEHelpContextIds.WELCOME_PAGE_SELECTION_DIALOG);
   // if (d.open() != Window.OK || d.getResult().length != 1) {
   // return null;
   // }
   // return (AboutInfo) d.getResult()[0];
   // }

}
