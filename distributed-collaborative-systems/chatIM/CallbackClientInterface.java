import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface CallbackClientInterface extends Remote {
    public void handleNotify(Map <String, String> data) throws RemoteException;    
}