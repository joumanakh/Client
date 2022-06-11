package client;
import java.io.File;
import org.json.simple.JSONObject; 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*; 
public class Client {
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
		switch(RequiredTaskNumber1) {
		case "1":
		case "3":
			System.out.println("Write the file path");
		break;
		case "2":
			System.out.println("Write the path of the local file on the PC");
		break;
		case "4":
			System.out.println("Write the folder path");
		break;
		default:
			System.out.println("Please enter a valid input (number from 1 to 4)!");
			RequiredTaskNumber1= sc.nextLine();
			chosenAction(RequiredTaskNumber1);
			return;
		
		}
		getPath(RequiredTaskNumber1);
		
	}
	public void getPath(String RequiredTaskNumber) throws IOException {
		Scanner sc= new Scanner(System.in); 
		String path= sc.nextLine();
		if(isPathValid(path)==false) {
			System.out.println("Please enter a valid path");
			 getPath(RequiredTaskNumber);}
		else {
			requestToJason(RequiredTaskNumber,path);
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
	public void requestToJason(String RequiredTaskNumber,String path) throws IOException {
		JSONObject obj = new JSONObject();
		obj.put("Required Task Number", RequiredTaskNumber);
	    obj.put("path", path);
	    if(RequiredTaskNumber.equals("1")) {
	    	obj.put("body", readTextFile(path));
	    }
	    else {
	    	obj.put("body","");
	    }
	    connection(obj);
	}
	 public String readTextFile(String fileName) throws IOException {
	        String content = new String(Files.readAllBytes(Paths.get(fileName)));
	        return content;
	    }
	public void connection(JSONObject obj) {
		
	}
	

}
