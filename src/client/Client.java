package client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import org.json.simple.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.Request;
import server.Response;
import server.ResponseCode;
import server.Action;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*; 
public class Client {
	private static  int serverPort;
	private  static String serverIP;
	private Socket socket ;
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	public String getServerIP() {
		return serverIP;
	}
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	public Client(int serverPort, String serverIP) {
		super();
		this.serverPort = serverPort;
		this.serverIP = serverIP;
	}
	
	public void  displayOnConsole() throws IOException{
		System.out.println("Please choose an option.");
		System.out.println("1) Download a file from the server");
		System.out.println("2) Upload file to server");
		System.out.println("3) Delete a file from the server");
		System.out.println("4) View folder contents on the server");
		Scanner sc= new Scanner(System.in); 
		String RequiredTaskNumber= sc.nextLine();
		chosenAction(RequiredTaskNumber);
		
	}
	
	public void chosenAction(String RequiredTaskNumber) throws IOException {
		String RequiredTaskNumber1=RequiredTaskNumber;
		Scanner sc= new Scanner(System.in); 
		Action action;
		switch(RequiredTaskNumber1) {
		case "1":
			System.out.println("Write the file path");
			action=Action.downloadFile;
			break;
		case "2":
			System.out.println("Write the path of the local file on the PC");
			action=Action.fileUploading;
		break;
		case "3":
			System.out.println("Write the file path");
			action=Action.deleteFile;
		break;
		case "4":
			System.out.println("Write the folder path");
			action=Action.viewFolderContent;
		break;
		default:
			System.out.println("error 405 MethodNotAllowed -Please enter a valid input (number from 1 to 4)!");
			RequiredTaskNumber1= sc.nextLine();
			chosenAction(RequiredTaskNumber1);
			return;
		
		}
		getPath(RequiredTaskNumber1,action);
		
	}
	
	public void getPath(String RequiredTaskNumber,Action action) throws IOException {
		Scanner sc= new Scanner(System.in); 
		String path= sc.nextLine();
		if(isPathValid(path)==false) {
			System.out.println("Please enter a valid path");
			 getPath(RequiredTaskNumber,action);}
		else {
			requestToJason(path,action);
			 }
			
		
	}
	
	public boolean isPathValid(String path) throws IOException{

		 File f = new File(path);
		    try {
		       f.getCanonicalPath();
		       return true;
		    }
		    catch (IOException e) {
		       return false;
		    }
		    
    }
	
	public void requestToJason(String path,Action action) throws IOException {
		JSONObject obj = new JSONObject();
		Request r=new Request();
		r.getParameters().setAction(action);
		r.getParameters().setFilePath(path);
		 if(action==Action.downloadFile) {
			 r.setBody(readTextFile(path));
		    }
		 String jasonRequest=new Gson().toJson(r);
		 outputConnection(jasonRequest);
	}
	
	
	 public String readTextFile(String fileName) throws IOException {
	        String content = new String(Files.readAllBytes(Paths.get(fileName)));
	        return content;
	    }
	 
	 public void connection() {
		 try { 
			 socket = new Socket(serverIP,serverPort); }
		 catch (IOException e) {
			        e.printStackTrace();
			    }
	 }
	 
	public void inputConnection() {
		try { 
         InputStream is = socket.getInputStream();
         DataInputStream dis = new DataInputStream(is);
         String s=dis.readUTF();
         JsonObject json = new JsonParser().parse(s).getAsJsonObject();
         responseToObject(json);
	}  
		catch (IOException e) {
        e.printStackTrace();
    }
		}
	public void outputConnection(String obj) {
		try {
		 OutputStream os = socket.getOutputStream();
         DataOutputStream dos = new DataOutputStream(os);
         dos.writeUTF(obj.toString());
		}  
		catch (IOException e) {
        e.printStackTrace();
		}
	}
	
	
	
	public void responseToObject(JsonObject obj) {
		Gson gson = new Gson(); 
		Response r = gson.fromJson(obj, Response.class);
		//System.out.println(r.getBody());
		displayResultsOnConsole(r);
	}
	
	
	public void displayResultsOnConsole(Response response){
		switch(response.getHeader().getResponseCode()) {
		case ok:
			lastImplementationForDisplay(response);
			
			break;
		case notFound:
			System.out.println("Error 404 (Not Found)");
			break;
		case InternalServerError:
			System.out.println("Error 500 (Internal Server Error)");
			break;
		case NotImplemented:
			System.out.println("Error 501 (Not Implemented)");
			break;
		}
	
	}
	
	public void lastImplementationForDisplay(Response response) {
		switch(response.getHeader().getAction()) {
		case downloadFile:
			   try {
					Files.write(Paths.get("C:\\Users\\user\\OneDrive\\Desktop\\client"), response.getBody().getBytes(), StandardOpenOption.CREATE);
					System.out.println("downloaded successfully");
				} catch (IOException e) {
					System.out.println("Error download into local file");
					// TODO Auto-generated catch block
					}
			break;
		case fileUploading:
			System.out.println("uploaded successfully");
			break;
		case deleteFile:
			System.out.println("deleted successfully");
			break;
		case viewFolderContent:
			System.out.println("Folder Content: ");
			System.out.println(response.getBody());
			break;
		
		}
		
		
	}
	
	

}
