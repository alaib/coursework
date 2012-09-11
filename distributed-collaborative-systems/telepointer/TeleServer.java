//RMI
import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class TeleServer{
	Registry reg;
	public TeleServer(){   
		try {
			reg = java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println("RMI registry ready.");
		} catch (Exception e) {
			System.out.println("Exception starting RMI registry:");
			e.printStackTrace();
		}
		
		try {			
			Naming.rebind("rmi://localhost/Telepointer", new TeleServerImpl(""));			
			System.out.println("Server is connected and ready for operation.");
		} catch (Exception e) {
			System.out.println("Server not connected: " + e);
		}
	}
	
	public static void main(String[] argv) {				
		final TeleServer tServer = new TeleServer();
    	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
            	try{
            		 UnicastRemoteObject.unexportObject(tServer.reg,true);            		
            	}catch(Exception e){
            		System.out.println("chatClient exception: "+ e);
            	}
            }
        }, "Shutdown-thread"));
	}
}
