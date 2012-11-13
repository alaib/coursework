package relayServer;

//RMI
import java.rmi.Naming;

import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Relay Server 
 * @author ravikirn
 *
 */

public class RelayServer{
	Registry reg;
	public RelayServer(){   
		try {
			reg = java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println("RMI registry ready.");
		} catch (Exception e) {
			System.out.println("Exception starting RMI registry:");
			e.printStackTrace();
		}
		
		try {			
			Naming.rebind("rmi://localhost/RelayServer", new RelayServerImpl());			
			System.out.println("Relay Server is connected and ready for operation.");
		} catch (Exception e) {
			System.out.println("Relay Server not connected: " + e);
		}
	}
	
	public static void main(String[] argv) {				
		final RelayServer tServer = new RelayServer();
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