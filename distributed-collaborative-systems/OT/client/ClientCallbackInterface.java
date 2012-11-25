package client;

import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

import otHelper.EditWithOTTimeStampInterface;

/**
 * Template for Client Callback
 * @author ravikirn
 *
 */

public interface ClientCallbackInterface extends Remote {
    public void handleTelePointerNotify(Point p, int STATUS_CODE) throws RemoteException;    
    public void handleChatEventNotify(String[] data, int STATUS_CODE) throws RemoteException;    
    public void transformInsertAndExecute(String clientName, EditWithOTTimeStampInterface edit, String newTopic) throws RemoteException;
}
