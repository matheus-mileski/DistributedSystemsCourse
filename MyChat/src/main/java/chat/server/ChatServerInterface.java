package chat.server;

import chat.client.ChatClientInterface;
import chat.model.Message;
import chat.model.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServerInterface extends Remote {
    void joinChatRoom(String chatRoomId, User user, ChatClientInterface client) throws RemoteException;

    void sendMessage(String chatRoomId, Message message) throws RemoteException;

    void joinDefaultChatRoom(User user, ChatClientInterface client) throws RemoteException;
}
