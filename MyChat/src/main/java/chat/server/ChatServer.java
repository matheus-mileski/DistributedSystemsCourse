package chat.server;

import chat.client.ChatClientInterface;
import chat.model.ChatRoom;
import chat.model.Message;
import chat.model.User;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer extends UnicastRemoteObject implements ChatServerInterface {
    private final Map<String, ChatRoom> chatRooms;

    public ChatServer() throws RemoteException {
        chatRooms = new ConcurrentHashMap<>();
    }

    public void createChatRoom(String chatId, String name, String description, User creator) {
        ChatRoom chatRoom = new ChatRoom(chatId, name, description, creator);
        chatRooms.put(chatId, chatRoom);
    }

    @Override
    public void joinChatRoom(String chatRoomId, User user, ChatClientInterface client) throws RemoteException {
        ChatRoom chatRoom = chatRooms.get(chatRoomId);

        // Check if chat room exists
        if (chatRoom != null) {
            // Check if user is not already in the chat room
            if (!chatRoom.getUsers().contains(user)) {
                chatRoom.addUser(user);
                chatRoom.addClient(client);
                System.out.println(user.getNickname() + " joined the chat room " + chatRoom.getName());
                Message message = null;
                try {
                    message = new Message(chatRoomId, "Joined the chat room " + chatRoom.getName(), user);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                sendMessage(chatRoomId, message);
            } else {
                System.out.println(user.getNickname() + " is already in the chat room " + chatRoom.getName());
            }
        } else {
            System.out.println("Chat room " + chatRoomId + " does not exist.");
        }

    }

    @Override
    public void sendMessage(String chatRoomId, Message message) throws RemoteException {
        ChatRoom chatRoom = chatRooms.get(chatRoomId);
        if (chatRoom != null) {
            chatRoom.addMessage(message);
            System.out.println(message.getSender().getNickname() + " sent message to chat room " + chatRoom.getName());
            for (ChatClientInterface c : chatRoom.getClients()) {
                c.deliverMessage(message);
            }
        } else {
            System.out.println("Chat room " + chatRoomId + " does not exist.");
        }
    }

    @Override
    public void joinDefaultChatRoom(User user, ChatClientInterface client) throws RemoteException {
        joinChatRoom("geral", user, client);
    }

}
