import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface TeleServerInterface extends Remote {
    public void handleEvent(String cName, Point p, Dimension d, Color c, int STATUS_CODE) throws RemoteException;
    public Point getCurrPoint() throws RemoteException;
    public Dimension getCurrDim() throws RemoteException;
    public Map <String, Color> getColorList() throws RemoteException;
    public Map <String, Point> getPointList() throws RemoteException;
    public void registerCallback(String cName, Color c, TeleClientCallbackInterface cbClient) throws RemoteException;
    public void unRegisterCallback(String cName) throws RemoteException;
}
