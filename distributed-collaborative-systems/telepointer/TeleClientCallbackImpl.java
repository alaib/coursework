import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TeleClientCallbackImpl extends UnicastRemoteObject implements TeleClientCallbackInterface {
   public TeleClient tc;
   
   public TeleClientCallbackImpl(TeleClient client) throws RemoteException {
      tc = client;      
   }   
   
   public void handleNotify(Point p, Dimension d, int STATUS_CODE){
	  tc.handleCallback(p, d, STATUS_CODE);   	   
   }      
}