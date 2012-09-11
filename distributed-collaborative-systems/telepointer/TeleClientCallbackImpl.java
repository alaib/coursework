import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TeleClientCallbackImpl extends UnicastRemoteObject implements TeleClientCallbackInterface {
   public TeleClient tc;
   public TeleClientExtra tcExtra;
   public int extraFlag;
   
   public TeleClientCallbackImpl(TeleClient client) throws RemoteException {
      tc = client;
      extraFlag = 0;
   }

   public TeleClientCallbackImpl(TeleClientExtra client) throws RemoteException {
	  tcExtra = client;
	  extraFlag = 1;
   }
   
   public void handleNotify(Point p, Dimension d, Color c, int STATUS_CODE){
	   if(this.extraFlag == 1){
		   tcExtra.handleCallback(p, d, STATUS_CODE);
	   }else{
		   tc.handleCallback(p, d, STATUS_CODE);
   	   }
   }      
}