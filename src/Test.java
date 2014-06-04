import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class Test {

	public static void test(){
		ServerSocket listener;
		try {
			listener = new ServerSocket(1919);
			
//
//			while(true){
//			
//			Socket socket=listener.accept();
//			
//			PrintWriter write=new PrintWriter(socket.getOutputStream(), true);
//			
//			write.println("GET / HTTP/1.1");
//			write.println("Host: stackoverflow.com");
//
//			write.println("This is a test");
//			
//			System.out.println("Test succeeds!");
//			
//			write.close();
//			}

			while(true){
				
			Socket socket=listener.accept();
			
			BufferedReader write=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String s="";
			
			System.out.println("Test succeeds!");
			while(s!=null)
				System.out.println((s=write.readLine()));
			
			write.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
