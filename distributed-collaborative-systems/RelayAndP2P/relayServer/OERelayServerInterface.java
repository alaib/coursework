package relayServer;

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
public interface OERelayServerInterface extends Remote {
    public Point getCurrPoint() throws RemoteException;
    public void registerCallback(String cName, String cStatus, OEClientCallbackInterface cbClient) throws RemoteException;
    public void unRegisterCallback(String cName) throws RemoteException;
	public void handleTelePointerEvent(String cName, Point p, int STATUS_CODE) throws RemoteException;
	public void handleChatEvent(String cName, String[] data, int STATUS_CODE) throws RemoteException;
}