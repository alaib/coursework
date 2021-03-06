package relayServer;

import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

import otHelper.EditWithOTTimeStampInterface;
import client.ClientCallbackInterface;

/**
 * Relay Server Remote Interface
 * @author ravikirn
 *
 */
public interface RelayServerInterface extends Remote {
    public Point getCurrPoint() throws RemoteException;
    public int registerCallback(String cName, String cStatus, ClientCallbackInterface cbClient) throws RemoteException;
    public void unRegisterCallback(String cName) throws RemoteException;
	public void handleTelePointerEvent(String cName, Point p, int STATUS_CODE) throws RemoteException;
	public void handleChatEvent(String cName, String[] data, int STATUS_CODE) throws RemoteException;
	public void handleOTEvent(String cName, EditWithOTTimeStampInterface ed, String newTopic, int STATUS_CODE) throws RemoteException;
	public String[] getLatecomerMsgs() throws RemoteException;
    public String getCurrentTopic() throws RemoteException;
}