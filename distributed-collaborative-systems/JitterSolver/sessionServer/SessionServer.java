package sessionServer;

//RMI
import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import tracer.TraceableListenerMod;
import util.trace.TraceableBus;

/**
* Session Server 
* @author ravikirn
*
*/

public class SessionServer{
	Registry reg;
	//public static TraceableListenerMod aListener = new TraceableListenerMod();
	
	public SessionServer(){   
		//TraceableBus.addTraceableListener(aListener);
		try {
			reg = java.rmi.registry.LocateRegistry.createRegistry(1100);
			System.out.println("RMI registry ready.");
		} catch (Exception e) {
			System.out.println("Exception starting RMI registry:");
			e.printStackTrace();
		}
		
		try {			
			Naming.rebind("rmi://localhost/SessionServer", new SessionServerImpl());			
			System.out.println("Session Server is connected and ready for operation.");
		} catch (Exception e) {
			System.out.println("Session Server not connected: " + e);
		}
	}
	
	public static void main(String[] argv) {				
		final SessionServer tServer = new SessionServer();
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