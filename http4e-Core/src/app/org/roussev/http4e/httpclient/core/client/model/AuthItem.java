package org.roussev.http4e.httpclient.core.client.model;

import java.io.Serializable;

import org.roussev.http4e.httpclient.core.util.BaseUtils;

public class AuthItem implements Serializable {

   private static final long serialVersionUID = 2589875060526422803L;

   private boolean           isPreemtptive;
   private boolean           isDigest;
   private boolean           isBasic;
   private String            username;
   private String            pass;
   private String            host;
   private String            port;
   private String            realm;


   public boolean isPreemtptive(){
      return isPreemtptive;
   }


   public void setPreemtptive( boolean isPreemtptive){
      this.isPreemtptive = isPreemtptive;
   }


   public boolean isDigest(){
      return isDigest;
   }


   public void setDigest( boolean isDigest){
      this.isDigest = isDigest;
   }


   public boolean isBasic(){
      return isBasic;
   }


   public void setBasic( boolean isBasic){
      this.isBasic = isBasic;
   }


   public String getUsername(){
      return BaseUtils.noNull(username);
   }


   public void setUsername( String username){
      this.username = username;
   }


   public String getPass(){
      return BaseUtils.noNull(pass);
   }


   public void setPass( String pass){
      this.pass = pass;
   }


   public String getHost(){
      return BaseUtils.noNull(host);
   }


   public void setHost( String host){
      this.host = host;
   }


   public String getPort(){
      return BaseUtils.noNull(port);
   }


   public void setPort( String port){
      this.port = port;
   }


   public String getRealm(){
      return BaseUtils.noNull(realm);
   }


   public void setRealm( String realm){
      this.realm = realm;
   }


   @Override
   public String toString(){
      return "AuthItem{isPreemtptive=" + isPreemtptive + ", isDigest=" + isDigest + ", isBasic=" + isBasic + ", username=" + username + ", pass=" + pass + ", host=" + host + ", port=" + port + ", realm=" + realm + "}";
   }

}
