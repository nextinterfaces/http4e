package org.roussev.http4e.jmeter;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.roussev.http4e.httpclient.core.ExceptionHandler;
import org.roussev.http4e.httpclient.core.util.HttpBean;

public class ExportTemplateTransformer {

   private String   templateFile;
   private HttpBean httpBean;


   public ExportTemplateTransformer( String templateFile, HttpBean httpBean) {
      this.templateFile = templateFile;
      this.httpBean = httpBean;
   }

   public void doWrite( Writer writer){
      try {

         Properties p = new Properties();
         
//          p.setProperty("resource.loader", "file");
////          p.setProperty("file.resource.loader.path", "./src");
//          p.setProperty("file.resource.loader.class",
//          "org.apache.velocity.runtime.resource.loader.FileResourceLoader");

         p.setProperty("resource.loader", "class");
         p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

         Velocity.init(p);

         VelocityContext context = new VelocityContext();
         context.put("httpBean", httpBean);

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
   
   public static void main( String[] args){
      
      new ExportTemplateTransformer("/resources/http4e-item.vm", new HttpBean()).doWrite(new StringWriter());
   }
}
