package sessionServer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import client.*;

/**
 * RMI Server Remote Interface
 * @author ravikirn
 *
 */
public interface SessionServerInterface extends Remote {
    public void registerCallback(String cName, String cStatus, ClientCallbackInterface cbClient) throws RemoteException;
    public void unRegisterCallback(String cName) throws RemoteException;
    public void sendMyCallbackToUser(String source, String dest) throws RemoteException;
    public String getUserList() throws RemoteException;
    public String[] updateClientStatus(String cName, String cStatus) throws RemoteException;
    public ClientCallbackInterface getCallback(String cName) throws RemoteException;
}
