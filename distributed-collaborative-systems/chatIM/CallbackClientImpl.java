import java.rmi.*;
import java.rmi.server.*;
import java.util.Map;

public class CallbackClientImpl extends UnicastRemoteObject implements CallbackClientInterface {
   public ChatClient c;
   public CallbackClientImpl(ChatClient client) throws RemoteException {
      c = client;
   }

   public void handleNotify(Map <String, String> data){
	   c.handleCallback(data);	   
   }      

}// end CallbackClientImpl class   