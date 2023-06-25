package chat.client;

import chat.model.User;
import chat.server.ChatServerInterface;
import chat.view.ChatWindowFX;
import javafx.application.Application;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ChatClientMain {

    private static ChatClientMain instance;
    private static User user;
    private static ChatClient client;
    private static ChatServerInterface server;

    public ChatClientMain() {
        instance = this;
    }

    public static ChatClientMain getInstance() {
        if (instance == null) {
            instance = new ChatClientMain();
        }
        return instance;
    }

    public static void main(String[] args) {
        try {
            // Locate the registry where the remote object is bound
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // Lookup the remote object
            server = (ChatServerInterface) registry.lookup("ChatServer");

            // Create Scanner for user input
            Scanner scanner = new Scanner(System.in);

            // Create a user
            System.out.println("Enter your username:");
            String username = scanner.nextLine();
            System.out.println("Enter your display name:");
            String displayName = scanner.nextLine();

            user = new User(username, displayName);
            client = new ChatClient(server, user);

            // Launch the JavaFX application
            Application.launch(ChatWindowFX.class, args);

        } catch (Exception e) {
            System.err.println("Client exception: " + e);
        }
    }

    public User getUser() {
        return user;
    }

    public ChatClient getClient() {
        return client;
    }

    public ChatServerInterface getServer() {
        return server;
    }
}
