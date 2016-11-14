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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.roussev.http4e.httpclient.core.CoreConstants;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class ItemModel implements Model {

   private static final long   serialVersionUID = -6572164711472818414L;

   /**
    * List<ModelListener> listeners
    */
   private List<ModelListener> listeners        = new ArrayList<ModelListener>();

   private final FolderModel   folderModel;
   private Item                item;
   private String[] payloadFiles;


   
   public String[] getPayloadFiles(){
      return payloadFiles;
   }


   
   public void setPayloadFiles( String[] payloadFiles){
      this.payloadFiles = payloadFiles;
   }


   public ItemModel( FolderModel folderModel) {
      this(folderModel, new Item());
   }


   public ItemModel( FolderModel folderModel, Item item) {
      this.folderModel = folderModel;
      this.item = item;
      this.item.availableProxies = folderModel.getAvailableProxies();
      this.item.availableKeystore = folderModel.getAvailableKeystore();
   }


   public ItemModel clone(){
      ItemModel clone = new ItemModel(folderModel);

      Map<String, List<String>> headers = getHeaders();
      for (String h : headers.keySet()) {
         List<String> hVals = headers.get(h);
         for (String val : hVals) {
            clone.addHeader(h, val);
         }
      }

      Map<String, List<String>> params = getParameters();
      for (String h : params.keySet()) {
         List<String> hVals = params.get(h);
         for (String val : hVals) {
            clone.addParameter(h, val);
         }
      }

      clone.setAuth(this.getAuth());
      clone.setAvailableKeystore(this.getAvailableKeystore());
      clone.setAvailableProxies(this.getAvailableProxies()); // TODO
      clone.setBody(this.getBody());
      clone.setCurrentProxy(this.getCurrentProxy());
      clone.setHSashWeights(this.getHSashWeights());
      clone.setHttpMethod(this.getHttpMethod());
      clone.setName(this.getName());
      clone.setProxy(this.getProxy());
      clone.setRequest(this.getRequest());
      clone.setResponse(this.getResponse());
      clone.setUrl(this.getUrl());
      clone.setVSashWeights(this.getVSashWeights());

      return clone;
   }


   public void init( int hashcode){
      item.hashcode = hashcode;
   }


   public void addListener( ModelListener listener){
      listeners.add(listener);
   }


   public void removeListener( ModelListener listener){
      listeners.remove(listener);
   }


   public void fireExecute( ModelEvent e){
      for (ModelListener listener : listeners) {
         listener.executed(e);
      }
   }


   public FolderModel getParent(){
      return folderModel;
   }


   /**
    * Map<String, List<String>> getHeaders
    */
   public Map<String, List<String>> getHeaders(){
      return item.headers;
   }


   public List<String> getHeaderValuesIgnoreCase( String headerName){
      for (Object headKeyObj : getHeaders().keySet()) {
         String headKey = (String) headKeyObj;
         if (headerName.equalsIgnoreCase(headKey)) {
            return getHeaders().get(headKey);
         }
      }
      return CoreConstants.BLANK_LIST;
   }


   public void clearHeaders(){
      item.headers.clear();
   }


   public void addHeader( String headerName, String headerValue){
      String ct = "Content-Type";
      String h = headerName;
      if(ct.equalsIgnoreCase(headerName)){
         h = ct;
      }
      List<String> headColl = item.headers.get(h);
      if (headColl == null) {
         headColl = new ArrayList<String>();
      }
      headColl.add(headerValue);
      item.headers.put(h, headColl);
   }


   public String getHttpMethod(){
      return item.httpMethod;
   }


   public void setHttpMethod( String httpMethod){
      item.httpMethod = httpMethod;
   }


   public String getName(){
      return item.name;
   }


   public void setName( String name){
      item.name = name;
   }


   /**
    * Map<String, List<String>> getParameters()
    */
   public Map<String, List<String>> getParameters(){
      return item.parameters;
   }


   public void clearParameters(){
      item.parameters.clear();
   }


   public void addParameter( String paramName, String paramValue){
      List<String> paramColl = item.parameters.get(paramName);
      if (paramColl == null) {
         paramColl = new ArrayList<String>();
      }
      paramColl.add(paramValue);
      item.parameters.put(paramName, paramColl);
   }


   public String getUrl(){
      return item.url;
   }


   public void setUrl( String url){
      item.url = url;
   }


   public ProxyItem getProxy(){
      return this.item.proxyItem;
   }


   public AuthItem getAuth(){
      return this.item.authItem;
   }


   public void setProxy( ProxyItem proxyItem){
      this.item.proxyItem = proxyItem;
   }


   public void setAuth( AuthItem authItem){
      this.item.authItem = authItem;
   }


   public ProxyItem getCurrentProxy(){
      return item.currentProxy;
   }


   public void setCurrentProxy( ProxyItem proxy){
      item.currentProxy = proxy;
   }


   public List<ProxyItem> getAvailableProxies(){
      return item.availableProxies;
   }


   public void setAvailableProxies( List<ProxyItem> availableProxies){
      item.availableProxies = availableProxies;
   }


   public String getAvailableKeystore(){
      return item.availableKeystore;
   }


   public void setAvailableKeystore( String availableKeystore){
      item.availableKeystore = availableKeystore;
   }


   public String getResponse(){
      return item.response;
   }


   public void setResponse( String response){
      this.item.response = response;
   }


   public String getRequest(){
      return item.request;
   }


   public void setRequest( String request){
      this.item.request = request;
   }


   public Serializable getSerializable(){
      return item;
   }


   public void load( Serializable serializable){
      this.item = (Item) serializable;
   }


   public String getBody(){
      return item.body;
   }


   public void setBody( String body){
      this.item.body = body;
   }


   public int[] getHSashWeights(){
      if (item.hSashWeights != null) {
         return item.hSashWeights;
      }
      return new int[] { CoreConstants.H_SASH_EQ, CoreConstants.H_SASH_EQ, CoreConstants.H_SASH_EQ };
   }


   public void setHSashWeights( int[] weights){
      item.hSashWeights = weights;
   }


   public int[] getVSashWeights(){
      if (item.vSashWeights != null) {
         return item.vSashWeights;
      }
      return new int[] { CoreConstants.V_SASH_EQ, CoreConstants.V_SASH_EQ, CoreConstants.V_SASH_EQ };
   }


   public void setVSashWeights( int[] weights){
      item.vSashWeights = weights;
   }

   public void setParameteredArgs( Map<String, String> parameteredArgs){
      item.parameteredArgs = parameteredArgs;
   }
   public Map<String, String> getParameteredArgs (){
      return item.parameteredArgs;
   }

   public boolean isEmpty(){
      return CoreConstants.EMPTY_TEXT.equals(item.name);
   }


   public int hashCode(){
      return item.hashCode();
   }


   public boolean equals( Object obj){
      if (obj != null && obj instanceof ItemModel) {
         ItemModel im = (ItemModel) obj;
         return im.hashCode() == this.hashCode();
      }
      return false;
   }


   public String toString(){
      return "\n\tItemModel" + item + "";
   }

}
