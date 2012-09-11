import java.awt.Dimension;
import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TeleServerInterface extends Remote {
    public void handleEvent(String cName, Point p, Dimension d, int STATUS_CODE) throws RemoteException;
    public Point getCurrPoint() throws RemoteException;
    public Dimension getCurrDim() throws RemoteException;
    public void registerCallback(String cName, TeleClientCallbackInterface cbClient) throws RemoteException;
    public void unRegisterCallback(String cName) throws RemoteException;
}
