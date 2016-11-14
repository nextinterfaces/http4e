/*
 *  Copyright 2017 Eclipse HttpClient (http4e) http://nextinterfaces.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.roussev.http4e.httpclient.core.client.view;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreContext;
import org.roussev.http4e.httpclient.core.CoreObjects;
import org.roussev.http4e.httpclient.core.client.model.FolderModel;
import org.roussev.http4e.httpclient.core.client.model.ItemModel;
import org.roussev.http4e.httpclient.core.client.model.ModelEvent;
import org.roussev.http4e.httpclient.core.client.model.ModelListener;
import org.roussev.http4e.httpclient.core.misc.ResourceCache;
import org.roussev.http4e.httpclient.core.misc.Styles;
import org.roussev.http4e.httpclient.core.util.BaseUtils;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;

public class FolderView implements CoreConstants, ModelListener {

   private CTabFolder        folder;
   private final FolderModel model;


   public FolderView( final Composite parent, final FolderModel folderModel) {
      model = folderModel;
      CoreContext.getContext().putObject(CoreObjects.RESOURCE_CACHE, new ResourceCache(/* parent */));
      model.addListener(this);
      buildFolder(parent);

      byte[] data = BaseUtils.readFromPrefs(FolderModel.PREF_VIEW_NAME);
      List<ItemModel> imList = model.deserialize(data);
      int size = imList.size();
      for (int i = size - 1; i > -1; i--) {
         buildTab(new CTabItem(folder, SWT.NONE), imList.get(i));
      }
   }


   public void enableAuth(){
      model.doAuth();
   }


   public void enableProxy(){
      model.doProxy();
   }


   private void buildFolder( Composite parent){

      // IDocument document;
      folder = new CTabFolder(parent, SWT.MULTI | SWT.CLOSE | SWT.BORDER);
      folder.setTabPosition(SWT.DOWN);
      folder.setTabHeight(20);
      // folder.setSimple(false);
      // folder.setCapture(true);
      folder.setBackgroundMode(SWT.INHERIT_DEFAULT);
      folder.setBorderVisible(false);

      folder.addCTabFolder2Listener(new CTabFolder2Adapter() {

         public void close( CTabFolderEvent e){
            if (model.getItemCount() > 1) {
               CTabItem tabItem = ((CTabItem) e.item);
               model.removeItem(new Integer(tabItem.hashCode()));
            } else {
               e.doit = false;
            }
         }
      });

      folder.addMouseListener(new MouseAdapter() {

         public void mouseDoubleClick( MouseEvent e){
            addTab();
         }
      });
   }


   public int getSelectionItemHash(){
      return folder.getSelection().hashCode();
   }


   public Control getControl(){
      return folder;
   }


   public void removeTab(){
      folder.getSelection().dispose();
   }


   public void duplicateTab(){
      CTabItem tab = new CTabItem(folder, SWT.NONE);
      ItemModel im0 = model.getItemModel(folder.getSelection().hashCode());
      buildTab(tab, im0.clone());
   }


   public void addTab(){
      CTabItem tab = new CTabItem(folder, SWT.NONE);
      ItemModel iModel = new ItemModel(model);
      buildTab(tab, iModel);
   }


   public void buildTab( ItemModel iModel){
      buildTab(new CTabItem(folder, SWT.NONE), iModel);
   }


   public void buildTab( CTabItem tab, ItemModel iModel){
      iModel.init(tab.hashCode());
      ItemView iView = new ItemView(tab, iModel);
      Utils.modelToView(iModel, iView);
      folder.setSelection(tab);
      model.putItem(iModel);
      iModel.fireExecute(new ModelEvent(ModelEvent.HTTP_METHOD_CHANGE, iModel));
      iModel.fireExecute(new ModelEvent(ModelEvent.PARAMS_FOCUS_LOST, iModel));
      iView.urlCombo.setFocus();
      iModel.fireExecute(new ModelEvent(ModelEvent.AUTH, CoreConstants.NULL_MODEL));
      iModel.fireExecute(new ModelEvent(ModelEvent.PROXY, CoreConstants.NULL_MODEL));
      // iView.urlCombo.setItems(model.getUrlHistory());
      // iModel.fireExecute( new ModelEvent(ModelEvent.CONTENT_TYPE_CHANGE,
      // iModel));
   }


   public void executed( ModelEvent e){
      if (e.getType() == ModelEvent.ITEM_ADD) {
         CTabItem tab = new CTabItem(folder, SWT.NONE);
         ItemModel iModel = (ItemModel) e.getModel();
         buildTab(tab, iModel);

      } else if (e.getType() == ModelEvent.FOLDER_FOCUS_GAINED) {
         folder.setSelectionForeground(ResourceUtils.getColor(Styles.BACKGROUND_ENABLED));
         Display display = Display.getCurrent();
         folder.setSelectionBackground(new Color[] { display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND), display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND), display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT) }, new int[] { 100, 100 }, true);
         folder.setSelectionForeground(display.getSystemColor(SWT.COLOR_TITLE_FOREGROUND));

      } else if (e.getType() == ModelEvent.FOLDER_FOCUS_LOST) {
         Display display = Display.getCurrent();
         folder.setSelectionBackground(new Color[] { display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND), display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND), display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT) }, new int[] { 100, 100 }, true);
         folder.setSelectionForeground(display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));

      } else if (e.getType() == ModelEvent.PARAMETERIZE_CHANGE) {
         for (ItemModel m : model.getItemModels()) {
            m.fireExecute(new ModelEvent(ModelEvent.PARAMS_FOCUS_LOST, CoreConstants.NULL_MODEL));
         }
      }
   }


   public void setFocus( boolean gained){
      if (gained) {
         model.fireExecute(new ModelEvent(ModelEvent.FOLDER_FOCUS_GAINED, CoreConstants.NULL_MODEL));
      } else {
         model.fireExecute(new ModelEvent(ModelEvent.FOLDER_FOCUS_LOST, CoreConstants.NULL_MODEL));
      }
   }


   public FolderModel getModel(){
      return model;
   }


   public void focus(){
      folder.setFocus();
   }


   public void doDispose(){
      ResourceCache imageCache = (ResourceCache) CoreContext.getContext().getObject(CoreObjects.RESOURCE_CACHE);
      if (imageCache != null)
         imageCache.dispose();
      model.doDispose();
      folder.dispose();
   }


   public int getSelectionIndex(){
      return folder.getSelectionIndex();
   }
   // // ================ MAIN ======================
   // public static void main( String[] args){
   // String ROOT_CORE = "C:/.work/http4e/org.roussev.http4e/Core";
   // String ROOT_UI = "C:/.work/http4e/org.roussev.http4e/Plugin";
   //       
   // CoreContext.getContext().putObject(CoreObjects.ROOT_PATH_CORE, ROOT_CORE);
   // CoreContext.getContext().putObject(CoreObjects.ROOT_PATH_UI, ROOT_UI);
   //       
   // CoreContext.getContext().putObject(CoreConstants.IS_STANDALONE, "nonull");
   //
   // Display display = new Display();
   // Shell shell = new Shell(display);
   // final ResourceCache imageCache = new ResourceCache();
   // // shell.setImage(imageCache.getImage(CoreImages.LOGO));
   // shell.setLayout(new FillLayout());
   // shell.setBounds(425, 625, 825, 350);
   //
   // final FolderView folderView = new FolderView(shell, new FolderModel());
   // shell.addDisposeListener(new DisposeListener() {
   // public void widgetDisposed( DisposeEvent e){
   // imageCache.dispose();
   // folderView.doDispose();
   // }
   // });
   // // TODO add similar listener to plugin view
   // shell.addShellListener(new ShellAdapter() {
   // public void shellActivated( ShellEvent e){
   // folderView.setFocus(true);
   // }
   //
   // public void shellDeactivated( ShellEvent e){
   // folderView.setFocus(false);
   // }
   // });
   //
   // shell.open();
   // while (!shell.isDisposed()) {
   // if (!display.readAndDispatch())
   // display.sleep();
   // }
   // display.dispose();
   // }
}
