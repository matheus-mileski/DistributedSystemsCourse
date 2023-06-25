package chat.client;

import chat.model.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClientInterface extends Remote {
    void deliverMessage(Message message) throws RemoteException;
}
