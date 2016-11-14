package org.roussev.http4e.httpclient.ui;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreContext;
import org.roussev.http4e.httpclient.core.CoreImages;
import org.roussev.http4e.httpclient.core.CoreObjects;
import org.roussev.http4e.httpclient.core.client.model.AuthItem;
import org.roussev.http4e.httpclient.core.client.model.ProxyItem;
import org.roussev.http4e.httpclient.core.client.view.FolderView;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;
import org.roussev.http4e.httpclient.ui.actions.AddTabAction;
import org.roussev.http4e.httpclient.ui.actions.AuthenticationAction;
import org.roussev.http4e.httpclient.ui.actions.DuplicateTabAction;
import org.roussev.http4e.httpclient.ui.actions.ExportMenuAction;
import org.roussev.http4e.httpclient.ui.actions.HelpDropDownAction;
import org.roussev.http4e.httpclient.ui.actions.ImportMenuAction;
import org.roussev.http4e.httpclient.ui.actions.ParameterizeAction;
import org.roussev.http4e.httpclient.ui.actions.ProxyAction;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class HdViewPart extends ViewPart {

   private HdContentViewer viewer;


   public HdViewPart() {
   }


   public FolderView getFolderView(){
      return viewer.getFolderView();
   }


   public void createPartControl( Composite parent){

      IActionBars actionBars = getViewSite().getActionBars();
      IToolBarManager toolbar = actionBars.getToolBarManager();

      ControlContribution space = new ControlContribution("Space") {

         public Control createControl( Composite parent){
            final Label space = new Label(parent, SWT.NONE);
            space.setSize(10, 0);
            return space;
         }


         protected int computeWidth( Control control){
            return 10;
         }
      };

      // ---- TODO add a maginfier control
      // tbmanager.add(new ControlContribution("magn"){
      // public Control createControl( Composite parent){
      // final Label space = new Label(parent, SWT.NONE);
      // space.setSize(20, 0);
      // space.setImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI,
      // CoreImages.MAGN));
      // return space;
      // }
      // protected int computeWidth( Control control){
      // return 20;
      // }
      // });
      //

      // ---- TODO add a Search|Find search control
      // tbmanager.add(new ControlContribution("Find") {
      // public Control createControl( Composite parent){
      // final Text find = new Text(parent, SWT.NONE);
      // find.setSize(100, 0);
      // find.setToolTipText("Find");
      // find.setForeground(ResourceUtils.getColor(Styles.PROC_INSTR));
      // find.setBackground(ResourceUtils.getColor(Styles.BACKGROUND_DISABLED));
      // return find;
      // }
      // protected int computeWidth( Control control){
      // return 100;
      // }
      // });

      toolbar.add(space);

      AddTabAction tabAction = new AddTabAction(this);
      tabAction.setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, CoreImages.TAB_OPEN)));
      toolbar.add(tabAction);

      DuplicateTabAction tabDuplicate = new DuplicateTabAction(this);
      tabDuplicate.setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, CoreImages.TAB_DUPLICATE)));
      toolbar.add(tabDuplicate);

      ParameterizeAction parameterizeAction = new ParameterizeAction(this);
      parameterizeAction.setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, CoreImages.PARAMETERIZE)));
      toolbar.add(parameterizeAction);

      // TabCloseAction tabCloseAction = new TabCloseAction(this);
      // tabCloseAction.setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI,
      // CoreImages.TAB_DEL)));
      // toolbar.add(tabCloseAction);

      ImportMenuAction importAction = new ImportMenuAction(this);
      importAction.setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, CoreImages.IMPORT)));
      toolbar.add(importAction);

      // ExportJavaHttp3Action exportAction = new ExportJavaHttp3Action(this);
      // exportAction.setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI,
      // CoreImages.EXPORT)));
      // toolbar.add(exportAction);

      // ToolsAction toolsAction = new ToolsAction(this);
      // toolsAction.setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI,
      // CoreImages.TOOLS)));
      // toolbar.add(toolsAction);

      ExportMenuAction exportMenuAction = new ExportMenuAction(this);
      exportMenuAction.setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, CoreImages.EXPORT)));
      toolbar.add(exportMenuAction);

      AuthenticationAction authAction = new AuthenticationAction(this);
      authAction.setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, CoreImages.AUTH)));
      toolbar.add(authAction);

      ProxyAction proxyAction = new ProxyAction(this);
      proxyAction.setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, CoreImages.PROXY)));
      toolbar.add(proxyAction);

      HelpDropDownAction helpAction = new HelpDropDownAction(this);
      helpAction.setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, CoreImages.HELP)));
      helpAction.setEnabled(true);
      toolbar.add(helpAction);

      viewer = new HdContentViewer(parent);

      CoreContext ctx = CoreContext.getContext();
      ProxyItem proxyItem = (ProxyItem) ctx.getObject(CoreObjects.PROXY_ITEM);
      AuthItem authItem = (AuthItem) ctx.getObject(CoreObjects.AUTH_ITEM);
      fireAuthEnable(authItem != null && (authItem.isBasic() || authItem.isDigest()));
      fireProxyEnable(proxyItem != null && proxyItem.isProxy());

      // folderView=new FolderView(frame, new FolderModel());
      parent.addDisposeListener(new DisposeListener() {

         public void widgetDisposed( DisposeEvent e){
            viewer.doDispose();
         }
      });

      // ToolBarManager tbar = new ToolBarManager(typeViewerToolBar);
      // fTypeViewerViewForm.setTopLeft(typeViewerToolBar);
      // set the filter menu items
      // IActionBars actionBars= getViewSite().getActionBars();
      // IMenuManager viewMenu= actionBars.getMenuManager();
      // for (int i= 0; i < fViewActions.length; i++) {
      // ToggleViewAction action= fViewActions[i];
      // viewMenu.add(action);
      // action.setEnabled(false);
      // }
      // viewMenu.add(new Separator());

      getSite().getShell().addShellListener(new ShellListener() {

         public void shellActivated( ShellEvent e){
            // folderView.setFocus(true);
         }


         public void shellDeactivated( ShellEvent e){
            // folderView.setFocus(false);
         }


         public void shellClosed( ShellEvent e){
         }


         public void shellDeiconified( ShellEvent e){
         }


         public void shellIconified( ShellEvent e){
         }
      });
   }


   /**
    * Called when we must grab focus.
    * 
    * @see org.eclipse.ui.part.ViewPart#setFocus
    */
   public void setFocus(){
      // folderView.focus();
      viewer.getControl().setFocus();
   }


   /**
    * Called when the View is to be disposed
    */
   public void dispose(){
      super.dispose();
   }


   public void fireAuthEnable( boolean enabled){
      IToolBarManager toolbar = this.getViewSite().getActionBars().getToolBarManager();
      IContributionItem[] items = toolbar.getItems();

      for (IContributionItem it : items) {
         if (it instanceof ActionContributionItem) {
            ActionContributionItem aci = (ActionContributionItem) it;
            if (aci.getAction() instanceof AuthenticationAction) {
               AuthenticationAction aa = (AuthenticationAction) aci.getAction();
               String img = null;
               if (enabled) {
                  img = CoreImages.AUTH_ENABLED;
               } else {
                  img = CoreImages.AUTH;
               }
               aa.setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, img)));
            }
         }
      }
   }


   public void fireProxyEnable( boolean enabled){
      IToolBarManager toolbar = this.getViewSite().getActionBars().getToolBarManager();
      IContributionItem[] items = toolbar.getItems();
      for (IContributionItem it : items) {
         if (it instanceof ActionContributionItem) {
            ActionContributionItem aci = (ActionContributionItem) it;
            if (aci.getAction() instanceof ProxyAction) {
               ProxyAction aa = (ProxyAction) aci.getAction();
               String img = null;
               if (enabled) {
                  img = CoreImages.PROXY_ENABLED;
               } else {
                  img = CoreImages.PROXY;
               }
               aa.setImageDescriptor(ImageDescriptor.createFromImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, img)));
            }
         }
      }
   }

}