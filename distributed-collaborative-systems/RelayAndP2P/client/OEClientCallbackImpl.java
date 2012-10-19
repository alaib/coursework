package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


/**
 * Implements OEClientCallbackInterface
 * @author ravikirn
 *
 */
public class OEClientCallbackImpl extends UnicastRemoteObject implements OEClientCallbackInterface {
   public ChatClient cm;
   
   public OEClientCallbackImpl(ChatClient ccm) throws RemoteException {
      cm = ccm;      
   }   
   
   public void handleTelePointerNotify(Point p, int STATUS_CODE){
	   cm.handleTelePointerNotify(p, STATUS_CODE);
   }

	@Override
	public void handleChatEventNotify(String[] data,  int STATUS_CODE) throws RemoteException {
		cm.handleChatEventNotify(data, STATUS_CODE);
		
	}
}
