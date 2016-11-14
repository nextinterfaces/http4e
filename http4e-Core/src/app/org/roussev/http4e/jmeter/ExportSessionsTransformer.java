package org.roussev.http4e.jmeter;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.roussev.http4e.httpclient.core.ExceptionHandler;
import org.roussev.http4e.httpclient.core.util.HttpBean;

public class ExportSessionsTransformer {

   private String               templateFile;
   private Collection<HttpBean> httpBeans;

   // private boolean parameterizeJSessionIds;

   public ExportSessionsTransformer( String templateFile, Collection<HttpBean> httpBeans) {
      this.templateFile = templateFile;
      this.httpBeans = httpBeans;
   }


   public void doWrite( Writer writer){
      try {

         Properties p = new Properties();
         // p.setProperty( "resource.loader", "file" );
         // p.setProperty( "file.resource.loader.path", "./src" );
         // p.setProperty( "file.resource.loader.class",
         // "org.apache.velocity.runtime.resource.loader.FileResourceLoader" );

         p.setProperty("resource.loader", "class");
         p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

         Velocity.init(p);

         VelocityContext context = new VelocityContext();
         context.put("httpbeans", httpBeans);

         Template template = null;

         try {
            template = Velocity.getTemplate(templateFile);
         } catch (ResourceNotFoundException e) {
            ExceptionHandler.handle(e);
         } catch (ParseErrorException e) {
            ExceptionHandler.handle(e);
         }

         if (template != null) {
            template.merge(context, writer);
         }
         /*
          * flush and cleanup
          */

         writer.flush();
         writer.close();
      } catch (Exception e) {
         ExceptionHandler.handle(e);
      }
   }


   // public void doRead(Collection<HttpBean> beans) {
   // System.out.println("Parsing http ...");
   //
   // // LiveHttpHeadersParser t = new LiveHttpHeadersParser();
   // try {
   // // t.parse(fileIn);
   // // Collection<HttpBean> bList = t.getHttpBeans();
   // for (HttpBean b : beans) {
   // // if (b.getHeaders().get("Cookie") == null) {
   // // throw new RuntimeException("'Cookie' is missing..");
   // // }
   // if (parameterizeJSessionIds) {
   // b.getHeaders().put("Cookie", "${jsessionid}");
   // //"${__CSVRead(jsessions.csv,${__counter(,cnt)})}");
   // }
   // System.out.println(b);
   // }
   // httpBeans.addAll(t.getHttpBeans());
   // } catch (Exception e) {
   // e.printStackTrace();
   // }
   // }

   public static void main( String[] args) throws FileNotFoundException{

      Collection<HttpBean> httpBeans = new ArrayList<HttpBean>();

      HttpBean bean = new HttpBean();
      bean.setBody("tBody");
      bean.setDomain("www.nextinterfaces.com");
      bean.setMethod("GET");
      bean.setPath("/");
      bean.setProtocol("http");
      httpBeans.add(bean);

      ExportSessionsTransformer t = new ExportSessionsTransformer("./resources/jmx.vm", httpBeans);

      // t.doRead("C:/packets.http");
      BufferedWriter writer = writer = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream("C:/http4e1.jmx")));

      t.doWrite(writer);
   }
}
