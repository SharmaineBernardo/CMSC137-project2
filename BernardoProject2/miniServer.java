/*
	SHARMAINE D. BERNARDO
	Project # 2
	References: https://github.com/NanoHttpd/nanohttpd
							https://www.codeproject.com/tips/1040097/create-simple-http-server-in-java
							
*/


import java.net.*;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;


public class miniServer extends Thread{   
   private Socket server;
   private static ServerSocket serverSocket;
   public miniServer(Socket server) throws IOException{
         this.server = server;
   }

	
	public static void waitForConnection(int port){

	  try{
	    while(true){
    	serverSocket = new ServerSocket(port);
      System.out.println("Waiting for connection on port " + serverSocket.getLocalPort() + "...");
    	Socket temp = serverSocket.accept();
      miniServer webServer = new miniServer(temp);
      Thread thread = new Thread(webServer);
      thread.start();
      System.out.println("Waiting for other connections..");
      serverSocket.close();
      }
    }catch(IOException e){}

	}   
	
	public static String createRes(BufferedReader file, String filename, String table){
	int length = 0;
	String responseHeader = null;
	final Date CT = new Date();
	final SimpleDateFormat SDF = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
	String[] type = filename.split("\\.");
	String filetype = "", response = "",  buffer = "";;

	
	if(type[1].equals("html")){
		filetype = "text/html";
	}else if(type[1].equals("css")){
		filetype = "text/css";
	}else if(type[1].equals("js")){
		filetype = "application/javascript";
	}
	
	responseHeader = "HTTP/1.1 ";
	
	if(file != null){
		responseHeader = responseHeader+"200: OK\n";
	}else{
		responseHeader = responseHeader+"404: Not Found\n";
	}
	
	if(file != null){
		try {
			while((buffer = file.readLine())!= null){
				if(Pattern.matches("\\webServer*<body>\\webServer*",buffer ) && table != null){
					response = response + buffer+"\n";
					response = response + table + "\n";
				}else{
				response = response+buffer+"\n";
				}
			}
		} catch (IOException e) {}
	}else{
		response="<h1>File Not Found</h1><p>Not on the Serverr</p>";
	}
	
	responseHeader = responseHeader+"Connection: keep-alive \nContent-Length: "+response.length()+"\nContent-Type: "+filetype+"\nAccept-Ranges: bytes\nDate: "+SDF.format(CT)+"\nKeep-Alive: timeout=5, max=100\nServer: Apachedaw\n\n";
	
	responseHeader = responseHeader+response;
	return responseHeader;
	
}


   public static void main(String [] args) throws IOException
   {
   	Scanner scanner = new Scanner(System.in);   
   	
      try
      {
     
       	System.out.print("Enter port: ");
        int port = scanner.nextInt();
        //waitForConnection(port);
        waitForConnection(port);
      }catch(ArrayIndexOutOfBoundsException e)
      {
        
      }
   }



public void run()  {
    InputStreamReader in;
    String table = null;
	try {
		
		in = new InputStreamReader(
		        this.server.getInputStream());
	     BufferedReader inFromClient =
                 new BufferedReader(in);
         String clientString = inFromClient.readLine();

       System.out.println(clientString);
         
       
      if(clientString != null){
         String[] thread = clientString.split(" ");
         FileReader input2 = null;
         
         String string4 = thread[1];
         String [] string5  = string4.split("\\?");
         
         if(string5.length == 2){
        	 table = "<table border=3><tr><th>Parameter</th><th>Value</th></tr>";
        	String [] webServer = string5[1].split("\\&");
        	
        	for(String j : webServer){
        		String [] d = j.split("=");
        		for(String df: d){
        			System.out.println(df);
        		}
        		table = table + "<tr><td>"+d[0]+"</td><td>"+d[1]+"</td></tr>";
        	}
        	table = table+"</table>";
         }else{
        	 table = null;
         }
         
         String string3 = string5[0];
    	 
         if(string3.equals("/")){
        	   input2 = new FileReader("index.html");
        	   string3 = "index.html";
         }else{
        	 try{
        	   input2 = new FileReader(string3.replace("/", ""));
        	 }catch(Exception e){}
         }	 
         String response;
         
         	if(input2 != null){
        	BufferedReader getFile  = new BufferedReader(input2);
        	if(table != null){
        	 response =  createRes(getFile, string3, table);
        	}else{
        		response =  createRes(getFile, string3, null);
        	}
        	 getFile.close();
         	}else{
         		 response =  createRes(null, string3,null);
         	}
        	
        	 DataOutputStream os;
			try {
				os = new DataOutputStream(this.server.getOutputStream());
			 	 os.writeUTF(response);
			 	 //System.out.println(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.server.close();
                   
          }

	}catch(Exception e){
		e.printStackTrace();
	}
}
}

