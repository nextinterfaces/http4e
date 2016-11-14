package org.roussev.http4e.httpclient.core.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.roussev.http4e.httpclient.core.ExceptionHandler;

public class HttpBean {

   private int                 id;
   private String              body;
   private String              domain;
   private String              protocol;
   private String              port;
   private String              path;
   private String              method;
   private String              contentType; // used in Flex
   private Map<String, String> headers = new HashMap<String, String>();
   private Map<String, String> params  = new HashMap<String, String>();
   private Map<String, String> csharpSpecialHeaders = new HashMap<String, String>();
   

   private String              request;
   private String              response;


   public Map<String, String> getParams(){
      return params;
   }


   public void setParams( Map<String, String> params){
      this.params = params;
   }


   public Map<String, String> getHeaders(){
      return headers;
   }


   public void setHeaders( Map<String, String> headers){
      this.headers = headers;
   }


   public String getBody(){
      return body;
   }


   public void setBody( String body){
      this.body = body;
   }


   public String getDomain(){
      return domain;
   }


   public void setDomain( String domain){
      this.domain = domain;
   }


   public String getProtocol(){
      return protocol;
   }


   public void setProtocol( String protocol){
      this.protocol = protocol;
   }


   public String getPath(){
      return path;
   }


   public void setPath( String path){
      this.path = path;
   }


   public String getMethod(){
      return method;
   }


   public void setMethod( String method){
      this.method = method;
   }
   
   public String getContentType(){
      return contentType;
   }
   
   public void setContentType( String contentType){
      this.contentType = contentType;
   }

   public String getPort(){
      return port;
   }

   
   public int getId(){
      return id;
   }


   public String getBodyShort(){      
      if(body == null || body.length() < 15){
         return body;
      }
      
      String res = "";
      try {
         res = body.substring(0, 15) + "..";
      } catch (Exception e) {
         // ignore
      }
      
      return res;
   }


   
   
   public String getRequest(){
      return request;
   }


   
   public void setRequest( String request){
      this.request = request;
   }


   
   public String getResponse(){
      return response;
   }


   
   public void setResponse( String response){
      this.response = response;
   }


   public void setId( int id){
      this.id = id;
   }


   public String getNonullPort(){
      if (port == null || port.trim().length() == 0) {
         return "80";
      } else {
         return port;
      }
   }
   
   public boolean isSecure(){
      return "https".equalsIgnoreCase(protocol);
   }

   public String getUrl(){
      String aPort = "";
      if (port != null && port.trim().length() > 0) {
         aPort = ":" + port;
      }
      
      String aURI = (path == null) ? "" : path; 
      
      return protocol + "://" + domain + aPort + aURI;
   }

   public String getServer(){
      String aPort = "";
      if (port != null && port.trim().length() > 0) {
         aPort = ":" + port;
      }
      
      return protocol + "://" + domain + aPort;
   }

   public String getHref(){
      return "raw/00" + id + "_http4e.html";
   }

   public String getUrlNoQuery(){
      String aPort = "";
      if (port != null && port.trim().length() > 0) {
         aPort = ":" + port;
      }
      
      String aURI = (path == null) ? "" : path; 
      
      String uriNoQuery = "";
      try {
         String[] strip = aURI.split("\\?");
         uriNoQuery = strip[0];
      } catch (Exception e) {
         ExceptionHandler.handle(e);
      }
      
      return protocol + "://" + domain + aPort + uriNoQuery;
   }

   public String getQueryTerm(){
     
      String aURI = (path == null) ? "" : path; 
      
      String queryTerm = "";
      try {
         String[] strip = aURI.split("\\?");
         queryTerm = strip[1];
      } catch (ArrayIndexOutOfBoundsException e) {
         //ignore normal
      } catch (Exception e) {
         ExceptionHandler.handle(e);
      }
      
      return queryTerm;
   }


   public void setPort( String port){
      this.port = port;
   }


   public String getParamsForUrl(){
      StringBuilder buff = new StringBuilder();
      for (String key : params.keySet()) {
         buff.append(key + "=" + params.get(key));
         buff.append("&");
      }
      //String result = URLEncoder.encode(buff.toString());
      
      return buff.toString()/*result*/;
   }


   public String getName(){
      if (path != null && path.length() > 0) {
         return path;
      }
      return domain;
   }
   
   public String getJquery(){
      return "$.ajax";
   }
   
   public String getPhpMethod(){
      String res = null;
      
      if("GET".equalsIgnoreCase(method)){
         res = "$responseCode = $client->get( $url, $headers)";
         
      } else if("POST".equalsIgnoreCase(method)){
         res = "$responseCode = $client->post( $url, $body, $headers)";
         
      } else if("PUT".equalsIgnoreCase(method)){
         res = "$responseCode = $client->put( $url, $body, $headers)";
         
      } else if("DELETE".equalsIgnoreCase(method)){
         res = "$responseCode = $client->delete( $url, $headers)";
         
      } else if("HEAD".equalsIgnoreCase(method)){
         res = "$responseCode = $client->head( $url, $headers)";

      } else if("OPTIONS".equalsIgnoreCase(method)){
         res = "//OPTIONS is not supported by HTTP_Client";
         
      } else if("TRACE".equalsIgnoreCase(method)){
         res = "//TRACE is not supported by HTTP_Client";
         
      } else {
         res = "// \"" + method + "\" Method Not Allowed.";
      }
      
      return res;
   }
   
   public String getRubyMethod(){
      String res = null;
      
      if("GET".equalsIgnoreCase(method)){
         res = "puts client.get(headers)";
         
      } else if("POST".equalsIgnoreCase(method)){
         res = "puts client.post(body, headers)";
         
      } else if("PUT".equalsIgnoreCase(method)){
         res = "puts client.put(body, headers)";
         
      } else if("DELETE".equalsIgnoreCase(method)){
         res = "puts client.delete(headers)";
         
      } else if("HEAD".equalsIgnoreCase(method)){
         res = "#//HEAD is not supported by Rest-Client";

      } else if("OPTIONS".equalsIgnoreCase(method)){
         res = "#//OPTIONS is not supported by Rest-Client";
         
      } else if("TRACE".equalsIgnoreCase(method)){
         res = "#//TRACE is not supported by Rest-Client";
         
      } else {
         res = "// \"" + method + "\" Method Not Allowed.";
      }
      
      return res;
   }
   
   public String getActionScriptService(){
      
      String res = null;
      
      if("GET".equalsIgnoreCase(method)){
         res = "http.doGet(getURI(), getParams());";
         
      } else if("POST".equalsIgnoreCase(method)){
         res = "http.doPost(getURI(), getBody(), getContentType(), getParams());";
         
      } else if("PUT".equalsIgnoreCase(method)){
         res = "http.doPut(getURI(), getBody(), getContentType(), getParams());";
         
      } else if("DELETE".equalsIgnoreCase(method)){
         res = "http.doDelete(getURI(), getParams());";
         
      } else if("HEAD".equalsIgnoreCase(method)){
         res = "http.doHead(getURI(), getParams());";
         
      } else if("OPTIONS".equalsIgnoreCase(method)){
         res = "http.doOptions(getURI(), getBody(), getParams());";
         
      } else {
         res = "// \"" + method + "\" Method Not Allowed.";
      }
      
      return res;
   }
   
   public void filterXml(){
      setBody(StringEscapeUtils.escapeXml(body));
      setDomain(StringEscapeUtils.escapeXml(domain));
      setPath(StringEscapeUtils.escapeXml(path));
      setPort(StringEscapeUtils.escapeXml(port));
      
      for (String key : getHeaders().keySet()) {
         String val = getHeaders().get(key);
         getHeaders().put(key, StringEscapeUtils.escapeXml(val));
      }

      for (String key : getParams().keySet()) {
         String val = getParams().get(key);
         getParams().put(key, StringEscapeUtils.escapeXml(val));
      }

      setRequest(StringEscapeUtils.escapeXml(request));
      setResponse(StringEscapeUtils.escapeXml(response));
   }

   public void filterJava(){
      setBody(StringEscapeUtils.escapeJava(body));
      setDomain(StringEscapeUtils.escapeJava(domain));
      setPath(StringEscapeUtils.escapeJava(path));
      setPort(StringEscapeUtils.escapeJava(port));
      
      for (String key : getHeaders().keySet()) {
         String val = getHeaders().get(key);
         getHeaders().put(key, StringEscapeUtils.escapeJava(val));
      }

      for (String key : getParams().keySet()) {
         String val = getParams().get(key);
         getParams().put(key, StringEscapeUtils.escapeJava(val));
      }
   }

   public void filterJavaSrcipt(){
      setBody(StringEscapeUtils.escapeJavaScript(body));
      setDomain(StringEscapeUtils.escapeJavaScript(domain));
      setPath(StringEscapeUtils.escapeJavaScript(path));
      setPort(StringEscapeUtils.escapeJavaScript(port));
      
      for (String key : getHeaders().keySet()) {
         String val = getHeaders().get(key);
         getHeaders().put(key, StringEscapeUtils.escapeJavaScript(val));
      }

      for (String key : getParams().keySet()) {
         String val = getParams().get(key);
         getParams().put(key, StringEscapeUtils.escapeJavaScript(val));
      }
   }
   
   /**
    * Returns the list of special headers being used at current HTTP call
    */   
   public void filterCSharpSpecialHeaders(){
      Map<String, String> reservedHeaders = new HashMap<String, String>();
      reservedHeaders.put("Accept", "Accept");
      reservedHeaders.put("Connection", "Connection");
      reservedHeaders.put("Content-Type", "ContentType");
      reservedHeaders.put("Content-Length", "ContentLength");
      reservedHeaders.put("Date", "Date");
      reservedHeaders.put("Expect", "Expect");
      reservedHeaders.put("Host", "Host");
      reservedHeaders.put("If-Modified-Since", "IfModifiedSince");
      reservedHeaders.put("Proxy-Connection", "Proxy");
      reservedHeaders.put("Referer", "Referer");
      reservedHeaders.put("Transfer-Encoding", "TransferEncoding");
      reservedHeaders.put("User-Agent", "UserAgent");
      
      Map<String, String> removedHeaders = new HashMap<String, String>();
      for (String key : reservedHeaders.keySet()) {
         if(headers.containsKey(key)){
            removedHeaders.put(reservedHeaders.get(key), headers.get(key));
            headers.remove(key);
         }
      }
      csharpSpecialHeaders.putAll(removedHeaders);
   }
   

   public Map<String, String> getCSharpSpecialHeaders(){
      return csharpSpecialHeaders;      
   }
   
   @Override
   public String toString(){
      return "HttpBean{" + " \nmethod=" + method + ", \nprotocol=" + protocol + ", \ndomain=" + domain + ", \nport=" + port + ", \npath=" + path + ", \nbody=" + body + ", \nheaders=" + headers + "}";
   }

}
