import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServerInterface extends Remote {
    public String[] handleEvent(String cName, String cStatus, String msg, int STATUS_CODE) throws RemoteException;
    public void registerCallback(String cName, CallbackClientInterface cbClient) throws RemoteException;
}
