package org.roussev.http4e.httpclient.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Preferences;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.CoreContext;
import org.roussev.http4e.httpclient.core.ExceptionHandler;
import org.roussev.http4e.httpclient.core.client.model.FolderModel;
import org.roussev.http4e.httpclient.core.client.model.ItemModel;
import org.roussev.http4e.httpclient.core.client.view.Utils;
import org.roussev.http4e.httpclient.core.misc.CoreException;
import org.roussev.http4e.java6.Properties6;
import org.roussev.http4e.jmeter.ExportSessionsTransformer;
import org.roussev.http4e.jmeter.ExportTemplateTransformer;
import org.roussev.http4e.jmeter.HttpToJmxTransformer;
import org.roussev.http4e.jmeter.LiveHttpHeadersParser;

public class BaseUtils {
   
   public static String getJavaVersion(){
      String ver = null;
      try {
         ver = "1.0";
         Class.forName("java.lang.Void");
         ver = "1.1";
         Class.forName("java.lang.ThreadLocal");
         ver = "1.2";
         Class.forName("java.lang.StrictMath");
         ver = "1.3";
         Class.forName("java.net.URI");
         ver = "1.4";
         Class.forName("java.util.Scanner");
         ver = "5";
         Class.forName("javax.annotation.processing.Completions");
         ver = "6";
      } catch (Throwable t) {
      }
      return ver;
   }
   
   public static void writeToPrefs( String prefName, byte[] prefData){
      try {
         Plugin pl = (Plugin) CoreContext.getContext().getObject("p");
         Preferences prefs = pl.getPluginPreferences();

         String str64 = new String(Base64.encodeBase64(prefData), "UTF8");
         prefs.setValue(prefName, str64);
         pl.savePluginPreferences();

      } catch (UnsupportedEncodingException e) {
         ExceptionHandler.handle(e);
      } catch (Exception ignore) {
         ExceptionHandler.handle(ignore);
      }
   }


   public static byte[] readFromPrefs( String prefName){
      try {
         Plugin pl = (Plugin) CoreContext.getContext().getObject("p");
         Preferences prefs = pl.getPluginPreferences();
         String str64 = prefs.getString(prefName);
         byte[] data = Base64.decodeBase64(str64.getBytes("UTF8"));
         return data;

      } catch (UnsupportedEncodingException e) {
         ExceptionHandler.handle(e);
      } catch (Exception ignore) {
         ExceptionHandler.handle(ignore);
      }
      return null;
   }


   /**
    * Decodes a String from a given charset
    */
   public static String decode( String text, String charsetName){
      Charset charset = Charset.forName(charsetName);
      CharsetDecoder decoder = charset.newDecoder();
      ByteBuffer byteBuff;
      try {
         byteBuff = ByteBuffer.wrap(text.getBytes(charsetName));
         return decoder.decode(byteBuff).toString();

      } catch (CharacterCodingException e) {
         throw new CoreException(CoreException.UNSUPPORTED_ENCODING, "CharacterCodingException", e);
      } catch (UnsupportedEncodingException ue) {
         throw new CoreException(CoreException.UNSUPPORTED_ENCODING, ue);
      }
   }


   /**
    * Encodes a String to given charset
    */
   public static String encode( String text, String charsetName){
      try {
         Charset charset = Charset.forName(charsetName);
         CharsetEncoder encoder = charset.newEncoder();

         ByteBuffer byteBuff = encoder.encode(CharBuffer.wrap(text));
         return new String(byteBuff.array(), charsetName);

      } catch (CharacterCodingException e) {
         throw new CoreException(CoreException.UNSUPPORTED_ENCODING, "CharacterCodingException", e);
      } catch (UnsupportedEncodingException ue) {
         throw new CoreException(CoreException.UNSUPPORTED_ENCODING, ue);
      }
   }


   public static boolean isEmpty( String str){
      return str == null || str.trim().equals("");
   }


   public static String noNull( String str){
      return str != null ? str : CoreConstants.EMPTY_TEXT;
   }


   public static String noNull( String str, String val){
      return str != null ? str : val;
   }


   public static Properties6 loadProperties( String propResource){
      Properties6 properties = null;
      if (propResource == null) {
         throw new IllegalArgumentException("propertiesResource not provided !");
      }

      InputStream is;
      try {
         is = new FileInputStream(propResource);

      } catch (FileNotFoundException e) {
         is = Thread.currentThread().getContextClassLoader().getResourceAsStream(propResource);
      }

      try {
         if (is == null || is.available() < 1) {
            throw new RuntimeException("Properties '" + propResource + "' not initilized. Skipping..");
         }
         properties = new Properties6();
         InputStreamReader inR = new InputStreamReader(is, "UTF8");
         BufferedReader bufR = new BufferedReader(inR);
         properties.load(bufR);

      } catch (IOException ioe) {
         throw new RuntimeException(ioe);
      }

      return properties;
   }


   public static List<String> loadList( InputStream is){

      if (is == null) {
         throw new IllegalArgumentException("InputStream empty.");
      }
      byte[] data;
      try {
         data = new byte[is.available()];
         is.read(data);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }

      String str = new String(data);
      StringTokenizer st = new StringTokenizer(str, "\n\r");
      List<String> result = new ArrayList<String>();
      while (st.hasMoreTokens()) {
         result.add(st.nextToken());
      }
      return result;
   }


   public static boolean isHttp4eIdentifier( char c){
      return Character.isJavaIdentifierPart(c) || c == '-';
   }


   public static void writeJMX( String fileName, FolderModel folderModel) throws FileNotFoundException{
      Collection<HttpBean> httpBeans = new ArrayList<HttpBean>();

      for (ItemModel iModel : folderModel.getItemModels()) {
         HttpBean bean = Utils.modelToHttpBean(iModel);
         bean.filterXml();
         httpBeans.add(bean);
      }

      HttpToJmxTransformer t = new HttpToJmxTransformer("/resources/jmx.vm", httpBeans);

      t.doWrite(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName))));

      // write JMX CSV data file
      try {
         File f = new File(new File(fileName).getParent() + File.separatorChar + "http4e-jmx.csv");
         if (!f.exists()) {
            FileWriter fstream = new FileWriter(f);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("################################################\n");
            out.write("## Configure varA, varB entries as bellow\n");
            out.write("## aa=xxx, bb=xxx\n");
            out.write("################################################\n");
            out.write("JSESSIONID=xxxxxxxxxx,userId=1\n");
            out.write("JSESSIONID=yyyyyyyyyy,userId=2\n");
            out.write("JSESSIONID=zzzzzzzzzz,userId=3\n");
            out.close();
         }
      } catch (Exception ignore) {
         //
      }
   }


   public static void writeJavaHttpClient3( ItemModel iModel, Writer writer) throws FileNotFoundException{
      HttpBean bean = Utils.modelToHttpBean(iModel);
      bean.filterJava();
      new ExportTemplateTransformer("/resources/java3.vm", bean).doWrite(writer);
   }


   public static void writeJavaHttpComponent4( ItemModel iModel, Writer writer) throws FileNotFoundException{
      HttpBean bean = Utils.modelToHttpBean(iModel);
      bean.filterJava();
      new ExportTemplateTransformer("/resources/java.vm", bean).doWrite(writer);
   }


   public static void writeJsPrototype( ItemModel iModel, Writer writer) throws FileNotFoundException{
      HttpBean bean = Utils.modelToHttpBean(iModel);
      bean.filterJava();
      new ExportTemplateTransformer("/resources/js-prototype.vm", bean).doWrite(writer);
   }


   public static void writeJsJQuery( ItemModel iModel, Writer writer) throws FileNotFoundException{
      HttpBean bean = Utils.modelToHttpBean(iModel);
      bean.filterJava();
      new ExportTemplateTransformer("/resources/js-jquery.vm", bean).doWrite(writer);
   }


   public static void writeJsXhr( ItemModel iModel, Writer writer) throws FileNotFoundException{
      HttpBean bean = Utils.modelToHttpBean(iModel);
      bean.filterJava();
      new ExportTemplateTransformer("/resources/js-xhr.vm", bean).doWrite(writer);
   }


   public static void writePython( ItemModel iModel, Writer writer) throws FileNotFoundException{
      HttpBean bean = Utils.modelToHttpBean(iModel);
      bean.filterJava();
      new ExportTemplateTransformer("/resources/python.vm", bean).doWrite(writer);
   }


   public static void writeCsharp( ItemModel iModel, Writer writer) throws FileNotFoundException{
      HttpBean bean = Utils.modelToHttpBean(iModel);
      bean.filterJava();
      bean.filterCSharpSpecialHeaders();
      new ExportTemplateTransformer("/resources/csharp.vm", bean).doWrite(writer);
   }


   public static void writeVisualBasic( ItemModel iModel, Writer writer) throws FileNotFoundException{
      HttpBean bean = Utils.modelToHttpBean(iModel);
      bean.filterJava();
      bean.filterCSharpSpecialHeaders();
      new ExportTemplateTransformer("/resources/vb.vm", bean).doWrite(writer);
   }
   
   public static void writeFlex( ItemModel iModel, Writer writer) throws FileNotFoundException{
      HttpBean bean = Utils.modelToHttpBean(iModel);
      bean.filterJava();
      bean.setMethod(bean.getMethod().toUpperCase());
      List<String> contentTypeHeader = iModel.getHeaderValuesIgnoreCase("Content-Type");
      if(contentTypeHeader != null && contentTypeHeader.size() > 0){
         bean.setContentType(contentTypeHeader.get(0));
      }
      new ExportTemplateTransformer("/resources/flex.vm", bean).doWrite(writer);
   }


   public static void writeRuby( ItemModel iModel, Writer writer) throws FileNotFoundException{
      HttpBean bean = Utils.modelToHttpBean(iModel);
      bean.filterJavaSrcipt();
      bean.setMethod(bean.getMethod().toLowerCase());
      new ExportTemplateTransformer("/resources/ruby.vm", bean).doWrite(writer);
   }


   public static void writePHP( ItemModel iModel, Writer writer) throws FileNotFoundException{
      HttpBean bean = Utils.modelToHttpBean(iModel);
      bean.filterJava();
      new ExportTemplateTransformer("/resources/php.vm", bean).doWrite(writer);
   }


   public static void writeObjectiveC( ItemModel iModel, Writer writer) throws FileNotFoundException{
      HttpBean bean = Utils.modelToHttpBean(iModel);
      bean.filterJava();
      if ("GET".equalsIgnoreCase(bean.getMethod())) {
         new ExportTemplateTransformer("/resources/cocoa-get.vm", bean).doWrite(writer);

      } else if ("POST".equalsIgnoreCase(bean.getMethod())) {
         new ExportTemplateTransformer("/resources/cocoa-post.vm", bean).doWrite(writer);

      } else if ("PUT".equalsIgnoreCase(bean.getMethod())) {
         new ExportTemplateTransformer("/resources/cocoa-put.vm", bean).doWrite(writer);

      } else if ("DELETE".equalsIgnoreCase(bean.getMethod())) {
         new ExportTemplateTransformer("/resources/cocoa-delete.vm", bean).doWrite(writer);

      } else {
         try {
            writer.write("\n\n       /* Method not allowed. Only GET, POST, PUT and DELETE are supported. */");
            writer.flush();
            writer.close();
         } catch (IOException e) {
            // ignore
         }
      }
   }


   public static void writeHttp4eSessionsModel(Writer writer, FolderModel folderModel) throws FileNotFoundException{
      Collection<HttpBean> httpBeans = new ArrayList<HttpBean>();

      int inx = 0;
      for (ItemModel iModel : folderModel.getItemModels()) {
         HttpBean bean = Utils.modelToHttpBean(iModel);
         bean.filterXml();

         bean.setId(inx);
         List<String> contentTypeHeader = iModel.getHeaderValuesIgnoreCase("Content-Type");
         if(contentTypeHeader != null && contentTypeHeader.size() > 0){
            bean.setContentType(contentTypeHeader.get(0));
         } else {
            bean.setContentType("");
         }
         
         httpBeans.add(bean);
         inx++;
      }

      ExportSessionsTransformer t = new ExportSessionsTransformer("/resources/http4e-sessions.vm", httpBeans);
      t.doWrite(writer);      
   }


   public static void writeHttp4eItemModel( int inx, ItemModel iModel, Writer writer) throws FileNotFoundException{
      HttpBean bean = Utils.modelToHttpBean(iModel);
      bean.setId(inx);
      List<String> contentTypeHeader = iModel.getHeaderValuesIgnoreCase("Content-Type");
      if(contentTypeHeader != null && contentTypeHeader.size() > 0){
         bean.setContentType(contentTypeHeader.get(0));
      } else {
         bean.setContentType("");
      }
      bean.setRequest(iModel.getRequest());
      bean.setResponse(iModel.getResponse());
      bean.filterXml();
      new ExportTemplateTransformer("/resources/http4e-item.vm", bean).doWrite(writer);
   }
   
   public static void writeHttp4eSessions(String file, FolderModel folderModel){

      try {
         File exportedFile = new File(file);
         File rawDir = new File(exportedFile.getParent() + File.separator + "raw");
         if (!rawDir.exists()) {
            rawDir.mkdir();
         }
         // String tmpDir =
         // "C:/Users/Mitko/Desktop/tmp/";//System.getProperty("java.io.tmpdir");

         FileWriter fileWriter = new FileWriter(new File(exportedFile.getParent() + File.separator + "index-sessions.html"));
         BaseUtils.writeHttp4eSessionsModel(fileWriter, folderModel);
         fileWriter.close();

         FileWriter fstream = new FileWriter(exportedFile.getParent() + File.separator + "index-sessions.txt");
         BufferedWriter outTxt = new BufferedWriter(fstream);
         
         
         int inx = 0;
         for (ItemModel im : folderModel.getItemModels()) {
            try {

               FileWriter fileWriter2 = new FileWriter(new File(exportedFile.getParent() + File.separator + "raw" + File.separator + "00" + inx + "_http4e.html"));
               BaseUtils.writeHttp4eItemModel(inx, im, fileWriter2);
               fileWriter2.close();

               outTxt.write("\n----------------------------------------------------------\n");
               outTxt.write(im.getRequest());
               outTxt.write("\n");
               outTxt.write(im.getResponse());
            } catch (IOException e) {
               ExceptionHandler.handle(e);
            }
            inx++;
         }
         //Close the output stream
         outTxt.close();

         byte[] data = folderModel.serialize();
         String str64 = new String(Base64.encodeBase64(data), "UTF8");
         BufferedWriter out = new BufferedWriter(new FileWriter(exportedFile));
         out.write(str64);
         out.flush();

      } catch (IOException e) {
         ExceptionHandler.handle(e);
      }
      
//      try {
//         FileOutputStream fos = new FileOutputStream(file);
//         ZipOutputStream zos = new ZipOutputStream(fos);
//         ZipEntry ze= new ZipEntry(zipFile.getParent() + File.separator + "index.html");
//         zos.putNextEntry(ze);
//         zos.closeEntry();
//         
//         ze= new ZipEntry(rawDir + File.separator + "http4e.ser");
//         zos.putNextEntry(ze);
//         zos.closeEntry();
//         
//         inx = 0;
//         for (ItemModel im : folderModel.getItemModels()) {
//            ze= new ZipEntry(zipFile.getParent() + File.separator + "raw" + File.separator + "00" + inx + "_http4e.html");
//            zos.putNextEntry(ze);
//            zos.closeEntry();
//            inx++;
//         }
//         zos.close();
//
//       } catch (FileNotFoundException e) {
//         e.printStackTrace();
//       } catch (IOException e) {
//         e.printStackTrace();
//       }
   }
   
   
   public static List<ItemModel> importHttp4eSessions(String file, FolderModel folderModel){
      try {
         byte[] data = Base64.decodeBase64(getContents(new File(file)).getBytes("UTF8"));
         List<ItemModel> items = new FolderModel(null, null).deserialize(data);
         
         return items;
         
      } catch (Exception e) {
         return new ArrayList<ItemModel>();
      }
   }

   public static List<ItemModel> importLiveHttpHeaders(String file, FolderModel folderModel){
      try {
         
         LiveHttpHeadersParser parser = new LiveHttpHeadersParser();
         parser.parse(file);
         List<ItemModel> items = new ArrayList<ItemModel>();
         Collection<HttpBean> beans = parser.getHttpBeans();
         for (HttpBean b : beans) {
            ItemModel iModel = toItemModel(folderModel, b);
            items.add(iModel);
         }         
         return items;
         
      } catch (Exception e) {
         return new ArrayList<ItemModel>();
      }
   }
   
   public static ItemModel toItemModel(FolderModel folderModel, HttpBean b){
      ItemModel iModel = new ItemModel(folderModel);
      Map<String, String> headers = b.getHeaders();
      for (String hKey : headers.keySet()) {
         iModel.addHeader(hKey, headers.get(hKey));
      }

      iModel.setHttpMethod(b.getMethod());
      iModel.setBody(b.getBody());
      iModel.setUrl(b.getUrl());
      
//      lastBean.setProtocol("https");
//      doMethod(methodBuff.toString(), lastBean);
//      doHeaders(headBuff.toString(), lastBean);
//      doBody(bodyBuff.toString(), lastBean);
      
      return iModel;
   }
   
   static public String getContents( File aFile){
      // ...checks on aFile are elided
      StringBuilder contents = new StringBuilder();

      try {
         // use buffering, reading one line at a time
         // FileReader always assumes default encoding is OK!
         BufferedReader input = new BufferedReader(new FileReader(aFile));
         try {
            String line = null; // not declared within while loop
            /*
             * readLine is a bit quirky : it returns the content of a line MINUS
             * the newline. it returns null only for the END of the stream. it
             * returns an empty String if two newlines appear in a row.
             */
            while ((line = input.readLine()) != null) {
               contents.append(line);
//               contents.append(System.getProperty("line.separator"));
            }
         } finally {
            input.close();
         }
      } catch (IOException ex) {
         ExceptionHandler.handle(ex);
      }

      return contents.toString();
   }
   
   public static void main( String[] args){

//    FolderModel folderModel = new FolderModel(null, null);
//    Item item = new Item();
//    item.request = "PUT /helloworld/test/bbb/n/m HTTP/1.1\nContent-Type: application/x-www-form-urlencoded";
//    item.response = "HTTP/1.1 200 OK\nServer: Apache";
//    ItemModel iModel = new ItemModel(folderModel, item);
//    iModel.setBody("aasdsad xd fsdfgsdfg sd sdf sdfg ");
//    iModel.setHttpMethod("POST");
//    iModel.addHeader("Content-type", "text/xml");      
//    folderModel.putItem(iModel);
//    iModel.setUrl("http://localhost:8080/helloworld/test/ad/xcbv?aa=1");
   
//    String file = "C:/Users/Mitko/Desktop/tmp/http4e-sesions.zip";
   }

}
