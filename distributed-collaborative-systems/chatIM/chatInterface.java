import java.rmi.Remote;
import java.rmi.RemoteException;

public interface chatInterface extends Remote {
    public String handleEvent(String cName, String cStatus, String msg, int STATUS_CODE) throws RemoteException;    
}
