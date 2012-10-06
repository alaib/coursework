import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OEClientCallbackInterface extends Remote {
    public void handleTelePointerNotify(Point p, int STATUS_CODE) throws RemoteException;    
    public void handleChatEventNotify(String[] data, int STATUS_CODE) throws RemoteException;    
}
