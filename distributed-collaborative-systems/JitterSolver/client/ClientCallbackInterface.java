package client;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Template for Client Callback
 * @author ravikirn
 *
 */

public interface ClientCallbackInterface extends Remote {
    public void handleTelePointerNotify(Point []p, int STATUS_CODE) throws RemoteException;    
    public void handleChatEventNotify(String[] data, int STATUS_CODE) throws RemoteException;    
    public void addClientCallback(String cName, ClientCallbackInterface cb) throws RemoteException;
    public void removeClientCallback(String cName) throws RemoteException;
}
