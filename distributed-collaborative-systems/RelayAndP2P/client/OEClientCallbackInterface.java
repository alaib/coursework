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

public interface OEClientCallbackInterface extends Remote {
    public void handleTelePointerNotify(Point p, int STATUS_CODE) throws RemoteException;    
    public void handleChatEventNotify(String[] data, int STATUS_CODE) throws RemoteException;    
}
