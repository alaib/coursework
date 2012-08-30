//RMI
import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class chatServer{
	Registry reg;
	public chatServer(){   
		try {
			reg = java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println("RMI registry ready.");
		} catch (Exception e) {
			System.out.println("Exception starting RMI registry:");
			e.printStackTrace();
		}
		
		try {			
			Naming.rebind("rmi://localhost/Chat", new chatImpl(""));
			System.out.println("Server is connected and ready for operation.");
		} catch (Exception e) {
			System.out.println("Server not connected: " + e);
		}
	}
	
	public static void main(String[] argv) {				
		final chatServer chServer = new chatServer();
    	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
            	try{
            		 UnicastRemoteObject.unexportObject(chServer.reg,true);            		
            	}catch(Exception e){
            		System.out.println("chatClient exception: "+ e);
            	}
            }
        }, "Shutdown-thread"));
	}
}
