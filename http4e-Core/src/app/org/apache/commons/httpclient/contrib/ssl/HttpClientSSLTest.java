package org.apache.commons.httpclient.contrib.ssl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;


public class HttpClientSSLTest {
    
    public static void main(String[] args) throws Exception {
        
                HttpClient httpclient;
                PostMethod httppost;
        //        
        ////        httpclient = new HttpClient();
        ////        httpget = new GetMethod("https://www.verisign.com/"); 
        ////        try { 
        ////          httpclient.executeMethod(httpget);
        ////          System.out.println(httpget.getStatusLine());
        ////        } catch (Exception e) {
        ////            e.printStackTrace();
        ////        } finally {
        ////          httpget.releaseConnection();
        ////        }
        //        
        
        Protocol myhttps = new Protocol("https", new StrictSSLProtocolSocketFactory()/*new EasySSLProtocolSocketFactory()*/, 443);
        httpclient = new HttpClient();
        //        httpclient.getHostConfiguration().setHost("www.verisign.com", 443, myhttps);
        //        httpclient.getHostConfiguration().setHost("login.coreproject.biz", 443, myhttps);
        httpclient.getHostConfiguration().setHost("www.google.com", 443, myhttps);
        httppost = new PostMethod("/accounts/ServiceLoginAuth?service=mail");
        httppost.setParameter("email", "dimiter.stoinov@gmail.com");
        httppost.setParameter("password", "qweqwe123");
        try {
            httpclient.executeMethod(httppost);
            System.out.println(httppost.getStatusLine());
            System.out.println(httppost.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httppost.releaseConnection();
        }
    }
    

    public static final String TARGET_HTTPS_SERVER = "www.verisign.com";
    public static final int TARGET_HTTPS_PORT = 443;
    

    public static void testSSL() throws Exception {
        
        Socket socket = SSLSocketFactory.getDefault().createSocket(TARGET_HTTPS_SERVER, TARGET_HTTPS_PORT);
        try {
            Writer out = new OutputStreamWriter(socket.getOutputStream(), "ISO-8859-1");
            out.write("GET / HTTP/1.1\r\n");
            out.write("Host: " + TARGET_HTTPS_SERVER + ":" + TARGET_HTTPS_PORT + "\r\n");
            out.write("Agent: SSL-TEST\r\n");
            out.write("\r\n");
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "ISO-8859-1"));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } finally {
            socket.close();
        }
    }
    
}
