import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TeleClientCallbackImpl extends UnicastRemoteObject implements TeleClientCallbackInterface {   
   public TeleClientMenu tcExtra;   
      
   public TeleClientCallbackImpl(TeleClientMenu client) throws RemoteException {
	  tcExtra = client;	  
   }
   
   public void handleNotify(Point p, Dimension d, Color c, int STATUS_CODE){	   
	   tcExtra.handleCallback(p, d, STATUS_CODE);	   
   }      
}