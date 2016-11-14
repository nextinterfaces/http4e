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
import org.roussev.http4e.httpclient.core.client.model.AuthItem;
import org.roussev.http4e.httpclient.core.client.view.FolderView;
import org.roussev.http4e.httpclient.core.util.BaseUtils;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;
import org.roussev.http4e.httpclient.ui.HdViewPart;

public class AuthDialog extends TitleAreaDialog {

   private Button   basicCheck;
   private Button   digestCheck;
   private Button   preemptiveCheck;
   private Text     usernameBox;
   private Text     passBox;
   private Text     hostBox;
   private Text     portBox;
   private Text     realmBox;
   private Button   okBtn;
   // private Button boxAuth;

   private AuthItem authItem;

   private ViewPart viewPart;


   public AuthDialog( ViewPart view) {
      super(view.getViewSite().getShell());
      setTitleImage(ResourceUtils.getImage(CoreConstants.PLUGIN_UI, CoreImages.LOGO_DIALOG));
      this.viewPart = view;
   }


   protected Control createContents( Composite parent){
      Control contents = super.createContents(parent);
      setTitle("Authentication");
      setMessage("BASIC and DIGEST Authentication.");

      populate();

      return contents;
   }


   protected Control createDialogArea( Composite parent){
      Composite composite = new Composite(parent, SWT.NONE | SWT.BORDER);
      composite.setLayout(new GridLayout());
      composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      Composite gr = createControlGroup(composite);

      return gr;
   }


   private Composite createControlGroup( Composite parent){

      // // -----------
      // Composite compTop = new Composite(parent, SWT.NONE);
      // GridLayout layoutTop = new GridLayout();
      // layoutTop.numColumns = 1;
      // layoutTop.marginLeft = 16;
      // compTop.setLayout(layoutTop);
      // boxAuth = new Button(compTop, SWT.CHECK);
      // boxAuth.setText("Use Authentication");
      // boxAuth.setSelection(false);
      // boxAuth.addSelectionListener(new SelectionAdapter() {
      //
      // @Override
      // public void widgetSelected( SelectionEvent e){
      // enableAll();
      //
      // // okBtn.setEnabled(isEnabled());
      // // AuthDialog.this.authItem.setBasic(basicCheck.getSelection());
      // }
      // });

      Composite composite = new Composite(parent, SWT.NONE);
      GridLayout layout1 = new GridLayout();
      layout1.numColumns = 2;
      composite.setLayout(layout1);

      // ------------
      Composite composite2 = new Composite(composite, SWT.NONE);
      GridLayout layout2 = new GridLayout();
      layout2.numColumns = 1;
      composite2.setLayout(layout2);

      // ------------
      Group authGroup = new Group(composite2, SWT.NONE);
      authGroup.setText("Auth Type");
      GridLayout layout = new GridLayout();
      layout.numColumns = 1;
      authGroup.setLayout(layout);
      this.basicCheck = new Button(authGroup, SWT.CHECK);
      basicCheck.setText("BASIC");
      basicCheck.setSelection(false);
      basicCheck.addSelectionListener(new SelectionAdapter() {

         @Override
         public void widgetSelected( SelectionEvent e){
            enableBoxes();

            okBtn.setEnabled(isEnabled());
            AuthDialog.this.authItem.setBasic(basicCheck.getSelection());
         }
      });

      this.digestCheck = new Button(authGroup, SWT.CHECK);
      digestCheck.setText("DIGEST");
      digestCheck.setSelection(false);
      digestCheck.addSelectionListener(new SelectionAdapter() {

         @Override
         public void widgetSelected( SelectionEvent e){
            enableBoxes();

            okBtn.setEnabled(isEnabled());
            AuthDialog.this.authItem.setDigest(digestCheck.getSelection());
         }
      });
      GridData data = new GridData(GridData.FILL_HORIZONTAL);
      data.widthHint = 160;
      data.heightHint = 70;
      authGroup.setLayoutData(data);

      // ------------
      Group preGroup = new Group(composite2, SWT.NONE);
      preGroup.setText("Preemptive?");
      GridLayout preLayout = new GridLayout();
      preLayout.numColumns = 2;
      preGroup.setLayout(preLayout);
      GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
      data2.heightHint = 40;

      preGroup.setLayoutData(data2);
      this.preemptiveCheck = new Button(preGroup, SWT.CHECK);
      preemptiveCheck.setText("Preemptive");
      preemptiveCheck.setSelection(true);
      preemptiveCheck.addSelectionListener(new SelectionAdapter() {

         @Override
         public void widgetSelected( SelectionEvent e){
            AuthDialog.this.authItem.setPreemtptive(preemptiveCheck.getSelection());
         }
      });

      // ------------
      createControlWidgets(composite);

      enableBoxes();

      return composite;
   }


   // private void enableAll(){
   // if (boxAuth.getSelection()) {
   // this.basicCheck.setEnabled(true);
   // this.digestCheck.setEnabled(true);
   // this.preemptiveCheck.setEnabled(true);
   //
   // } else {
   // this.basicCheck.setEnabled(false);
   // this.digestCheck.setEnabled(false);
   // this.preemptiveCheck.setEnabled(false);
   // }
   //
   // enableBoxes();
   // }

   private void enableBoxes(){

      if (basicCheck.getSelection() || digestCheck.getSelection()) {
         usernameBox.setEnabled(true);
         passBox.setEnabled(true);
         hostBox.setEnabled(true);
         portBox.setEnabled(true);
         realmBox.setEnabled(true);

      } else {
         usernameBox.setEnabled(false);
         passBox.setEnabled(false);
         hostBox.setEnabled(false);
         portBox.setEnabled(false);
         realmBox.setEnabled(false);
      }

   }


   private void createControlWidgets( Composite controlGroup){

      Group group = new Group(controlGroup, SWT.NONE);
      group.setText("Details");
      GridLayout layout = new GridLayout();
      layout.numColumns = 2;
      layout.marginHeight = 10;
      layout.marginWidth = 20;
      group.setLayout(layout);
      group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      GridData grData = new GridData(GridData.FILL_HORIZONTAL);
      grData.widthHint = 160;

      new Label(group, SWT.NONE).setText("Username *");
      this.usernameBox = new Text(group, SWT.BORDER);
      usernameBox.setLayoutData(grData);

      new Label(group, SWT.NONE).setText("Password *");
      this.passBox = new Text(group, SWT.PASSWORD | SWT.BORDER);
      passBox.setLayoutData(grData);

      usernameBox.addModifyListener(new ModifyListener() {

         public void modifyText( ModifyEvent e){
            okBtn.setEnabled(isEnabled());
            AuthDialog.this.authItem.setUsername(usernameBox.getText());
         }
      });
      passBox.addModifyListener(new ModifyListener() {

         public void modifyText( ModifyEvent e){
            okBtn.setEnabled(isEnabled());
            AuthDialog.this.authItem.setPass(passBox.getText());
         }
      });

      new Label(group, SWT.NONE).setText("Host");
      this.hostBox = new Text(group, SWT.BORDER);
      hostBox.setLayoutData(grData);
      hostBox.addModifyListener(new ModifyListener() {

         public void modifyText( ModifyEvent e){
            AuthDialog.this.authItem.setHost(hostBox.getText());
         }
      });

      new Label(group, SWT.NONE).setText("Port");
      this.portBox = new Text(group, SWT.BORDER);
      GridData grData2 = new GridData(SWT.BEGINNING);
      grData2.widthHint = 30;
      portBox.setLayoutData(grData2);
      portBox.addModifyListener(new ModifyListener() {

         public void modifyText( ModifyEvent e){
            AuthDialog.this.authItem.setPort(portBox.getText());
         }
      });

      new Label(group, SWT.NONE).setText("Realm");
      this.realmBox = new Text(group, SWT.BORDER);
      realmBox.setLayoutData(grData);
      realmBox.addModifyListener(new ModifyListener() {

         public void modifyText( ModifyEvent e){
            AuthDialog.this.authItem.setRealm(realmBox.getText());
         }
      });
   }


   public void populate(){
      CoreContext ctx = CoreContext.getContext();
      AuthItem item = (AuthItem) ctx.getObject(CoreObjects.AUTH_ITEM);
      try {
         if (item != null) {
            this.authItem = item;
            basicCheck.setSelection(item.isBasic());
            digestCheck.setSelection(item.isDigest());
            preemptiveCheck.setSelection(item.isPreemtptive());
            usernameBox.setText(item.getUsername());
            passBox.setText(item.getPass());
            hostBox.setText(item.getHost());
            portBox.setText(item.getPort());
            realmBox.setText(item.getRealm());

            if (isEnabled()) {
               okBtn.setEnabled(true);
               usernameBox.setEnabled(true);
               passBox.setEnabled(true);
               hostBox.setEnabled(true);
               portBox.setEnabled(true);
               realmBox.setEnabled(true);
            }
         } else {
            this.authItem = new AuthItem();
         }
      } catch (SWTException e) {
         // dispose exception
         ExceptionHandler.handle(e);
      }
   }


   private boolean isEnabled(){
      return (basicCheck.getSelection() || digestCheck.getSelection()) && !BaseUtils.isEmpty(usernameBox.getText()) && !BaseUtils.isEmpty(passBox.getText());
   }


   protected void createButtonsForButtonBar( Composite parent){
      okBtn = createButton(parent, IDialogConstants.OK_ID, "Apply Authentication", true);
      okBtn.addSelectionListener(new SelectionAdapter() {

         public void widgetSelected( SelectionEvent e){
            CoreContext.getContext().putObject(CoreObjects.AUTH_ITEM, AuthDialog.this.authItem);
            ((HdViewPart) AuthDialog.this.viewPart).fireAuthEnable(true);
            fireExecuteFolderItems();
         }
      });
      okBtn.setEnabled(false);

      Button removeBtn = createButton(parent, IDialogConstants.CANCEL_ID, "Don't Use Authentication", true);
      removeBtn.addSelectionListener(new SelectionAdapter() {

         public void widgetSelected( SelectionEvent e){
            AuthDialog.this.authItem.setBasic(false);
            AuthDialog.this.authItem.setDigest(false);
            CoreContext.getContext().putObject(CoreObjects.AUTH_ITEM, AuthDialog.this.authItem);
            ((HdViewPart) AuthDialog.this.viewPart).fireAuthEnable(false);
            fireExecuteFolderItems();
         }
      });

   }


   private void fireExecuteFolderItems(){
      IViewSite site = AuthDialog.this.viewPart.getViewSite();
      HdViewPart part = (HdViewPart) site.getPart();
      FolderView folderView = part.getFolderView();
      folderView.enableAuth();
   }

}
