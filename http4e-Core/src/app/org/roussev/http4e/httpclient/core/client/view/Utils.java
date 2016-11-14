package org.roussev.http4e.httpclient.core.client.view;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreContext;
import org.roussev.http4e.httpclient.core.CoreObjects;
import org.roussev.http4e.httpclient.core.client.model.AuthItem;
import org.roussev.http4e.httpclient.core.client.model.ItemModel;
import org.roussev.http4e.httpclient.core.client.model.ModelEvent;
import org.roussev.http4e.httpclient.core.client.model.ProxyItem;
import org.roussev.http4e.httpclient.core.misc.CoreException;
import org.roussev.http4e.httpclient.core.util.HttpBean;
import org.roussev.http4e.httpclient.core.util.ParseUtils;
import org.roussev.http4e.java6.Properties6;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class Utils {

   public static boolean isIDE(){
      return CoreContext.getContext().getObject(CoreObjects.IS_STANDALONE) == null;
   }


   public static boolean isAutoAssistInvoked( KeyEvent e){
      if ((e.keyCode == 32) && ((e.stateMask & SWT.CTRL) != 0)) {
         return true;

      } else if (((e.keyCode == 32) && ((e.stateMask & SWT.COMMAND) != 0))) {
         return true;

      } else if ((e.character == ' ') && ((e.stateMask & SWT.CTRL) != 0)) {
         return true;

      } else if ((e.character == ' ') && ((e.stateMask & SWT.COMMAND) != 0)) {
         return true;

      }
      return false;

   }


   public static ToolItem getItem( int position, ViewForm vForm){
      ToolItem item = null;
      Control[] arr1 = vForm.getChildren();
      for (int i = 0; i < arr1.length; i++) {
         if (arr1[i] instanceof ToolBar) {
            ToolBar tBar = (ToolBar) arr1[i];
            ToolItem[] arr2 = tBar.getItems();
            return arr2[position];
         }
      }
      return item;
   }


   public static boolean isHttp( String url){
      if (url != null && url.startsWith(CoreConstants.PROTOCOL_HTTP)) {
         return true;
      }
      return false;
   }


   public static boolean isHttpS( String url){
      if (url != null && url.startsWith(CoreConstants.PROTOCOL_HTTPS)) {
         return true;
      }
      return false;
   }


   public static String appendProtocol( String url){
      if (url != null && !isHttp(url) && !isHttpS(url)) {
         url = CoreConstants.PROTOCOL_HTTP + url;
      }
      return url;
   }


   public static String trimProtocol( String url){
      if (url != null) {
         if (isHttp(url)) {
            url = url.substring(CoreConstants.PROTOCOL_HTTP.length(), url.length());
         } else if (isHttpS(url)) {
            url = url.substring(CoreConstants.PROTOCOL_HTTPS.length(), url.length());
         }
      }
      return url;
   }


   public static void viewToModel( ItemModel iModel, ItemView iView){

      if (iView.getTabName() != null) {
         iModel.setName(iView.getTabName());
      } else {
         iModel.setName(trimProtocol(iView.urlCombo.getText()));
      }
      iModel.setHttpMethod(iView.httpCombo.getText());
      iModel.setUrl(appendProtocol(iView.urlCombo.getText()));
      iModel.setHSashWeights(iView.hSash.getWeights());
      iModel.setVSashWeights(iView.vSash.getWeights());

      // TODO bytes[], mulitpart ?
      iModel.setBody(iView.bodyView.getText());

      iModel.clearHeaders();
      iModel.clearParameters();

      textToModelHeaders(iView.headerView.getHeaderText(), iModel);
      String txt = iView.paramView.getParamText();
      txt = txt.replace('\\', '/');
      textToModelParams(txt, iModel);

      // TODO use Auth and Proxy per item not globally
      CoreContext ctx = CoreContext.getContext();
      iModel.setProxy((ProxyItem) ctx.getObject(CoreObjects.PROXY_ITEM));
      iModel.setAuth((AuthItem) ctx.getObject(CoreObjects.AUTH_ITEM));
   }


   public static void textToModelHeaders( String text, ItemModel iModel){
      iModel.clearHeaders();
      if (text != null && !text.trim().equals("")) {
         Properties6 p = new Properties6();
         try {
            InputStreamReader inR = new InputStreamReader(new ByteArrayInputStream(text.getBytes("UTF8")), "UTF8");
            BufferedReader buf = new BufferedReader(inR);
            p.load(buf);

         } catch (UnsupportedEncodingException e) {
            throw new CoreException(CoreException.UNSUPPORTED_ENCODING, e);
         } catch (IOException e) {
            throw new CoreException(CoreException.IO_EXCEPTION, e);
         }

         for (Iterator iter = p.entrySet().iterator(); iter.hasNext();) {
            Map.Entry me = (Map.Entry) iter.next();
            iModel.addHeader((String) me.getKey(), (String) me.getValue());
         }
      }
   }


   public static void textToModelParams( String text, ItemModel iModel){
      iModel.clearParameters();
      if (!CoreConstants.EMPTY_TEXT.equals(text)) {
         Properties6 p = new Properties6();
         try {
            InputStreamReader inR = new InputStreamReader(new ByteArrayInputStream(text.getBytes("UTF8")), "UTF8");
            BufferedReader buf = new BufferedReader(inR);
            p.load(buf);

         } catch (UnsupportedEncodingException e) {
            throw new CoreException(CoreException.UNSUPPORTED_ENCODING, e);
         } catch (IOException e) {
            throw new CoreException(CoreException.IO_EXCEPTION, e);
         }

         for (Iterator iter = p.entrySet().iterator(); iter.hasNext();) {
            Map.Entry me = (Map.Entry) iter.next();
            iModel.addParameter((String) me.getKey(), (String) me.getValue());
         }
      }
   }


   public static void modelToView( ItemModel iModel, ItemView iView){

      iView.httpCombo.setText(iModel.getHttpMethod());
      iView.urlCombo.setText(iModel.getUrl());
      if (iModel.getHeaders().isEmpty()) {
         iModel.fireExecute(new ModelEvent(ModelEvent.HEADERS_FOCUS_LOST, iModel));
      } else {
         iView.headerView.setText(listToString(iModel.getHeaders()));
      }
      if (iModel.getParameters().isEmpty()) {
         iModel.fireExecute(new ModelEvent(ModelEvent.PARAMS_FOCUS_LOST, iModel));
      } else {
         iView.paramView.setParamText(listToString(iModel.getParameters()));
      }

      iView.bodyView.setText(iModel.getBody());
      iView.requestView.setHttpText(iModel.getRequest());
      iView.responseView.setHttpText(iModel.getResponse());
      iView.tabItem.setText(ParseUtils.toTitle(iModel.getName()));
      iView.tabeNameText.setText(iView.tabItem.getText());
   }


   private static HttpBean urlToHttpBean( String url){

      String protocol = "";
      String domain = "";
      String path = "";
      String port = "";
      int domainInx = -1;
      String domainPortPath = "";

      String[] splitt = url.split("://");
      protocol = splitt[0];

      try {
         domainPortPath = splitt[1];
         domainInx = domainPortPath.indexOf("/");
         if (domainInx < 0) {
            int questIndx = domainPortPath.indexOf("?");
            if (questIndx < 0) {
               domainInx = domainPortPath.length();
            } else {
               domainInx = questIndx;
            }
         }
      } catch (Exception ignore) {
         // ignore.printStackTrace();
      }
      try {
         domain = domainPortPath.substring(0, domainInx);
         path = domainPortPath.substring(domainInx, domainPortPath.length());

         String[] domainPort = domain.split(":");
         domain = domainPort[0];
         port = domainPort[1];
      } catch (Exception ignore) {
         // ignore.printStackTrace();
      }

      HttpBean bean = new HttpBean();
      bean.setProtocol(protocol);
      bean.setDomain(domain);
      bean.setPath(path);
      bean.setPort(port);

      return bean;
   }


   public static HttpBean modelToHttpBean( ItemModel iModel){

      HttpBean b = urlToHttpBean(iModel.getUrl());

      b.setMethod(iModel.getHttpMethod());

      boolean headersOnly = "GET".equalsIgnoreCase(iModel.getHttpMethod()) || "HEAD".equalsIgnoreCase(iModel.getHttpMethod()) || "OPTIONS".equalsIgnoreCase(iModel.getHttpMethod()) || "TRACE".equalsIgnoreCase(iModel.getHttpMethod()) || "DELETE".equalsIgnoreCase(iModel.getHttpMethod());

      if (headersOnly) {
         b.setBody("");
      } else {
         b.setBody(iModel.getBody());
      }

      for (String hName : iModel.getHeaders().keySet()) {
         List<String> hList = iModel.getHeaderValuesIgnoreCase(hName);
         StringBuilder sb = new StringBuilder();
         for (int i = 0; i < hList.size(); i++) {
            sb.append(hList.get(i));
            if (i < hList.size() - 1) {
               sb.append(",");
            }
         }
         String hVal = sb.toString();
         b.getHeaders().put(hName, hVal);
      }

      if ("POST".equalsIgnoreCase(iModel.getHttpMethod())) {
         // TODO it is retrieving only the last parameter from multiple paramset
         for (String key : iModel.getParameters().keySet()) {
            List<String> pList = iModel.getParameters().get(key);
            b.getParams().put(key, "");
            if (pList != null) {
               for (String pVal : pList) {
                  b.getParams().put(key, pVal);
               }
            }
         }
      }

      return b;
   }


   public static String listToString( Map map){
      StringBuilder sb = new StringBuilder();
      int size = map.size();
      int cnt = 0;
      for (Iterator it = map.keySet().iterator(); it.hasNext();) {
         String key = (String) it.next();
         cnt++;
         sb.append(key + CoreConstants._EQ);
         List values = (List) map.get(key);
         for (int i = 0; i < values.size(); i++) {
            if (i != 0)
               sb.append(CoreConstants._SEMICOL + CoreConstants._SPACE);
            sb.append(values.get(i));
         }
         if (cnt != size)
            sb.append("\n");
      }
      return sb.toString();
   }

}
