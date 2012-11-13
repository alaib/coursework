package sessionServer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import client.*;

/**
 * Session Server Remote Interface
 * @author ravikirn
 *
 */
public interface SessionServerInterface extends Remote {
    public void registerCallback(String cName, String cStatus, ClientCallbackInterface cbClient) throws RemoteException;
    public String getCurrentTopic() throws RemoteException;
    public void setCurrentTopic(String newTopic) throws RemoteException;
    public Point getCurrentPoint() throws RemoteException;
    public void setCurrentPoint(Point newPoint) throws RemoteException;
    public Point[] getCurrentPointList() throws RemoteException;
    public void setCurrentPointList(Point[] newPoint) throws RemoteException;
    public void unRegisterCallback(String cName) throws RemoteException;
    public void sendMyCallbackToUser(String source, String dest) throws RemoteException;
    public String getUserList() throws RemoteException;
    public String[] updateClientStatus(String cName, String cStatus) throws RemoteException;
    public ClientCallbackInterface getCallback(String cName) throws RemoteException;
}
