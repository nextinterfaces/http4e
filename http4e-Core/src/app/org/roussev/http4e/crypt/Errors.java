package org.roussev.http4e.crypt;

public class Errors extends Exception {
 
   public final static int 
   SUCCESS = 0, 
   KEY_TABLE_READ = 1,
   TOKEN_READ = 2,
   EMAIL_EMPTY = 3,
   UNSUPPORTED_ENCODING = 4,
   MAJOR_MINOR_NOT_SUPPORTED = 5,
   EMAIL_DOESNOT_MATCH = 6,
   UNKNOWN = 100
   ;
   
   private int code = SUCCESS;

   public Errors(int code){  
      this.code = code;    
   }
   
   public Errors(int code, String msg){   
      super(msg);
      this.code = code;
   }
   
   public Errors(int code, Throwable throwable){   
      super(throwable);
      this.code = code;
   }

   public int getCode(){
      return code;
   }

   public void setCode( int code){
      this.code = code;
   }
   
   
   
   
}
