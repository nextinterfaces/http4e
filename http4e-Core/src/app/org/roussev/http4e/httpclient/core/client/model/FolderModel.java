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
package org.roussev.http4e.httpclient.core.client.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.text.StrTokenizer;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreContext;
import org.roussev.http4e.httpclient.core.CoreObjects;
import org.roussev.http4e.httpclient.core.ExceptionHandler;
import org.roussev.http4e.httpclient.core.misc.CoreException;
import org.roussev.http4e.httpclient.core.util.BaseUtils;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class FolderModel implements Model {

   private static final long             serialVersionUID  = -6512224765472818414L;
   public static final String            PREF_VIEW_NAME    = "v";

   private final Map<Integer, ItemModel> itemMap           = new HashMap<Integer, ItemModel>();
   private final List<ItemModel>         itemList          = new ArrayList<ItemModel>();
   private final List<ModelListener>     modelListeners    = new ArrayList<ModelListener>();
   private final Set<String>             urlHistory        = new HashSet<String>();
   private List<ProxyItem>               availableProxies  = new ArrayList<ProxyItem>();
   private String                        availableKeystore = null;


   public FolderModel( String availableProxiesString, String availableKeystoresString) {
      this.availableProxies.add(ProxyItem.createDirectConnectionProxy());
      for (String proxysettings : new StrTokenizer(availableProxiesString, "#").getTokenArray()) {
         try {
            this.availableProxies.add(ProxyItem.createFromString(proxysettings));
         } catch (Exception e) {
            // TODO handle error
         }
      }
      this.availableKeystore = availableKeystoresString;
   }


   @SuppressWarnings("unchecked")
   public List<ItemModel> deserialize( byte[] data){
      List<ItemModel> imList = new ArrayList<ItemModel>();
      this.removeAll();
      List<ItemModel> serList;
      try {
         ByteArrayInputStream bais = new ByteArrayInputStream(data);
         ObjectInputStream ois = new ObjectInputStream(bais);
         serList = (List<ItemModel>) ois.readObject();
         int size = serList.size();
         for (int i = size - 1; i > -1; i--) {
            Serializable ser = (Serializable) serList.get(i);
            ItemModel im = new ItemModel(this);
            im.load(ser);
            if (!im.isEmpty())
               imList.add(im);
         }
         ois.close();

      } catch (EOFException ignore) {
      } catch (FileNotFoundException ignore) {
      } catch (ClassNotFoundException ignore) {
      } catch (IOException e) {
         ExceptionHandler.warn("deserialize: " + e);// warning may be ..
      }

      if (imList.size() < 1) {
         // System.out.println("FolderModel: No items deserialized. Creating one
         // ..");
         ItemModel im = new ItemModel(this);
         imList.add(im);
      }

      int cnt = 0;
      // Add available Proxies to ItemModels
      for (ItemModel itemModel : imList) {
         itemModel.setAvailableProxies(availableProxies);
         itemModel.setAvailableKeystore(availableKeystore);
         if (cnt == 0) { // FIXME. Those values belong to FolderModel, not
                         // ItemModel
            CoreContext ctx = CoreContext.getContext();
            ctx.putObject(CoreObjects.AUTH_ITEM, itemModel.getAuth());
            ctx.putObject(CoreObjects.PROXY_ITEM, itemModel.getProxy());
            ctx.putObject(CoreObjects.PARAMETERIZE_ARGS, itemModel.getParameteredArgs());
         }
      }

      return imList;
   }


   public byte[] serialize(){
      try {
         // FileOutputStream fout = new
         // FileOutputStream(CoreConstants.SERIALIZED_PATH);
         // ObjectOutputStream oos = new ObjectOutputStream(fout);
         List<Serializable> serList = new ArrayList<Serializable>();

         CoreContext ctx = CoreContext.getContext();
         Map<String, String> mapParmzArgs = (Map<String, String>) ctx.getObject(CoreObjects.PARAMETERIZE_ARGS);

         for (ItemModel im : itemList) {
            im.setParameteredArgs(mapParmzArgs);
            im.fireExecute(new ModelEvent(ModelEvent.FOLDER_INIT, CoreConstants.NULL_MODEL));
            serList.add(im.getSerializable());
         }
         // oos.writeObject(serList);
         // oos.close();

         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         ObjectOutputStream oos2 = new ObjectOutputStream(baos);
         oos2.writeObject(serList);
         oos2.close();

         return baos.toByteArray();

      } catch (Exception e) {
         throw new CoreException(CoreException.GENERAL, e);
      }
   }


   public void addListener( ModelListener listener){
      modelListeners.add(listener);
   }


   public void removeListener( ModelListener listener){
      modelListeners.remove(listener);
   }


   public void fireExecute( ModelEvent e){
      for (ModelListener listener : modelListeners) {
         listener.executed(e);
      }
   }


   public void addUrlToHistory( String url){
      urlHistory.add(url);
   }


   public String[] getUrlHistory(){
      String[] urls = new String[urlHistory.size()];
      int i = 0;
      for (Iterator<String> iter = urlHistory.iterator(); iter.hasNext(); i++) {
         urls[i] = iter.next();
      }
      return urls;
   }


   public synchronized void putItem( ItemModel itemModel){
      itemMap.put(new Integer(itemModel.hashCode()), itemModel);
      itemList.add(itemModel);
   }


   /**
    * Using the overloaded remove(int) instead of remove(Object)
    */
   public synchronized void removeItem( Integer hashcode){
      itemList.remove(getItemModel(hashcode));
      itemMap.remove(hashcode);
   }


   public synchronized void removeAll(){
      itemMap.clear();
      itemList.clear();
   }


   public List<ProxyItem> getAvailableProxies(){
      return availableProxies;
   }


   public String getAvailableKeystore(){
      return availableKeystore;
   }


   public synchronized int getItemCount(){
      return itemMap.size();
   }


   public synchronized ItemModel getItemModel( Integer id){
      return (ItemModel) itemMap.get(id);
   }


   public List<ItemModel> getItemModels(){
      return itemList;
   }


   public Serializable getSerializable(){
      throw new RuntimeException("Method not implemented");
   }


   public void load( Serializable data){
      throw new RuntimeException("Method not implemented");
   }


   public void doDispose(){

      BaseUtils.writeToPrefs(PREF_VIEW_NAME, this.serialize());
      for (ItemModel im : itemList) {
         im.fireExecute(new ModelEvent(ModelEvent.ITEM_DISPOSE, CoreConstants.NULL_MODEL));
      }
      this.removeAll();
   }


   public void doAuth(){
      for (ItemModel im : itemList) {
         im.fireExecute(new ModelEvent(ModelEvent.AUTH, CoreConstants.NULL_MODEL));
      }
   }


   public void doProxy(){
      for (ItemModel im : itemList) {
         im.fireExecute(new ModelEvent(ModelEvent.PROXY, CoreConstants.NULL_MODEL));
      }
   }


   public String toString(){
      return "FolderModel{" + "items=" + itemMap + "}";
   }

}
