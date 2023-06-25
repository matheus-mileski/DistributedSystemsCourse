package chat.server;

import chat.model.User;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChatServerMain {
    public static void main(String[] args) {
        try {
            // Create and export the registry instance on a specific port
            Registry registry = LocateRegistry.createRegistry(1099);

            // Create server object
            ChatServer server = new ChatServer();
            User root = new User("root", "root");

            server.createChatRoom("geral", "Geral", "Sala de bate-papo geral!", root);

            // Bind the remote object's stub in the registry
            registry.bind("ChatServer", server);

            System.out.println("Chat Server is ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
        }
    }
}