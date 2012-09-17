import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TeleClientCallbackInterface extends Remote {
    public void handleNotify(Point p, Dimension d, int STATUS_CODE) throws RemoteException;    
}
