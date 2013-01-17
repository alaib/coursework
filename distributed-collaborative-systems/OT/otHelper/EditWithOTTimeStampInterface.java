package otHelper;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EditWithOTTimeStampInterface extends Remote{
	public void print() throws RemoteException;
	public String printStr() throws RemoteException;
	public int getPos() throws RemoteException;
	public int isServer() throws RemoteException;
	public int getId() throws RemoteException;
	public void setId(int priority) throws RemoteException;
	public void setServer(int server) throws RemoteException;
	public Character getChar() throws RemoteException;
	public int getLocalCount() throws RemoteException;
	public int getRemoteCount() throws RemoteException;
	public int isGreaterThanOrEqualTo(EditWithOTTimeStampInterface t2) throws RemoteException;
	public void incrementRemote() throws RemoteException;
	public void incrementLocal() throws RemoteException;
	public EditWithOTTimeStampInterface copy() throws RemoteException;
	public void setPos(int val) throws RemoteException;
}