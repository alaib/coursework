package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


/**
 * Implements ClientCallbackInterface
 * @author ravikirn
 *
 */
public class ClientCallbackImpl extends UnicastRemoteObject implements ClientCallbackInterface {
   public ChatClient cm;
   
   public ClientCallbackImpl(ChatClient ccm) throws RemoteException {
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
