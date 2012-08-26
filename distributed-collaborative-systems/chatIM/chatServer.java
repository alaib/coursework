//RMI
import java.rmi.Naming;

public class chatServer{			
	public chatServer(){          
	}
	
	public static void main(String[] argv) {
		try {			
			Naming.rebind("rmi://localhost/Chat", new chatImpl(""));
			System.out.println("Server is connected and ready for operation.");
		} catch (Exception e) {
			System.out.println("Server not connected: " + e);
		}
	}
}
