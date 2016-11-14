package org.roussev.http4e.httpclient.ui.actions;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.part.ViewPart;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreContext;
import org.roussev.http4e.httpclient.core.CoreImages;
import org.roussev.http4e.httpclient.core.CoreObjects;
import org.roussev.http4e.httpclient.core.ExceptionHandler;
import org.roussev.http4e.httpclient.core.client.model.ProxyItem;
import org.roussev.http4e.httpclient.core.client.view.FolderView;
import org.roussev.http4e.httpclient.core.util.BaseUtils;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;
import org.roussev.http4e.httpclient.ui.HdViewPart;

public class ProxyDialog extends TitleAreaDialog {

   private Button    enabledCheck;
   private Text      hostBox;
   private Text      portBox;
   private ProxyItem proxyItem;
   private ViewPart  viewPart;


   public ProxyDialog( ViewPart view) {
      super(view.getViewSite().getShell());
      setTitleImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, CoreImages.LOGO_DIALOG));
      this.viewPart = view;
   }


   protected Control createContents( Composite parent){
      Control contents = super.createContents(parent);
      setTitle("Proxy Configuration");
      setMessage("Proxy Configuration.");

      populate();

      return contents;
   }


   protected Control createDialogArea( Composite parent){
      Composite composite = new Composite(parent, SWT.NONE | SWT.BORDER);
      composite.setLayout(new GridLayout());
      composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      createControlWidgets(composite);

      return composite;
   }


   private void createControlWidgets( Composite parent){

      Composite composite0 = new Composite(parent, SWT.NONE);
      GridLayout layout0 = new GridLayout();
      layout0.numColumns = 1;
      composite0.setLayout(layout0);

      Composite composite = new Composite(composite0, SWT.NONE);
      GridLayout layout = new GridLayout();
      layout.numColumns = 2;
      layout.horizontalSpacing = 10;
      layout.verticalSpacing = 10;
      composite.setLayout(layout);

      GridData grData = new GridData(GridData.FILL_HORIZONTAL);
      grData.widthHint = 260;

      new Label(composite, SWT.NONE).setText("Host *");
      this.hostBox = new Text(composite, SWT.BORDER);
      hostBox.setLayoutData(grData);

      new Label(composite, SWT.NONE).setText("Port *");
      this.portBox = new Text(composite, SWT.BORDER);
      portBox.setLayoutData(grData);

      hostBox.addModifyListener(new ModifyListener() {

         public void modifyText( ModifyEvent e){
            ProxyDialog.this.proxyItem.setHost(hostBox.getText());
         }
      });

      portBox.addModifyListener(new ModifyListener() {

         public void modifyText( ModifyEvent e){
            try {
               int port = Integer.parseInt(portBox.getText());
               ProxyDialog.this.proxyItem.setPort(port);
            } catch (NumberFormatException nfe) {
               portBox.setText("");
            }
         }
      });

      Group group = new Group(parent, SWT.NONE);
      GridLayout layout1 = new GridLayout();
      layout1.numColumns = 1;
      layout1.marginHeight = 20;
      layout1.marginWidth = 20;
      group.setLayout(layout1);
      GridData d1 = new GridData(GridData.FILL_HORIZONTAL);
      group.setLayoutData(d1);
      this.enabledCheck = new Button(group, SWT.CHECK);
      enabledCheck.setText("Enable Proxy?");

      enabledCheck.addSelectionListener(new SelectionAdapter() {

         public void widgetSelected( SelectionEvent e){
            if (enabledCheck.getSelection()) {
               ProxyDialog.this.proxyItem.setProxy(true);
            } else {
               ProxyDialog.this.proxyItem.setProxy(false);
            }
         }
      });

   }


   public void populate(){
      CoreContext ctx = CoreContext.getContext();
      ProxyItem item = (ProxyItem) ctx.getObject(CoreObjects.PROXY_ITEM);
      try {
         if (item != null) {
            this.proxyItem = item;
            hostBox.setText(BaseUtils.noNull(item.getHost()));
            portBox.setText("" + item.getPort());
            enabledCheck.setSelection(item.isProxy());

         } else {
            this.proxyItem = new ProxyItem();
         }
      } catch (SWTException e) {
         // dispose exception
         ExceptionHandler.handle(e);
      }
   }


   protected void createButtonsForButtonBar( Composite parent){
      Button okBtn = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
      okBtn.addSelectionListener(new SelectionAdapter() {

         public void widgetSelected( SelectionEvent e){
            CoreContext.getContext().putObject(CoreObjects.PROXY_ITEM, ProxyDialog.this.proxyItem);
            ((HdViewPart)ProxyDialog.this.viewPart).fireProxyEnable(ProxyDialog.this.proxyItem.isProxy());
            fireExecuteFolderItems();
         }
      });
   }

   private void fireExecuteFolderItems(){
      IViewSite site = ProxyDialog.this.viewPart.getViewSite();
      HdViewPart part = (HdViewPart) site.getPart();
      FolderView folderView = part.getFolderView();
      folderView.enableProxy();
   }
   
}
