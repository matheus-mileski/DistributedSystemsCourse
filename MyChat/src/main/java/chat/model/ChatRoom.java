package chat.model;

import chat.client.ChatClientInterface;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {

    private final String chatId;
    private final String name;
    private final String description;
    private final User owner;
    private final List<Message> messages;
    private final List<User> users;
    private final List<ChatClientInterface> clients;

    public ChatRoom(String chatId, String name, String description, User creator) {
        this.chatId = chatId;
        this.name = name;
        this.description = description;
        this.owner = creator;
        this.messages = new ArrayList<>();
        this.users = new ArrayList<>();
        this.clients = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void addClient(ChatClientInterface client) {
        this.clients.add(client);
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "chatId='" + chatId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", messages=" + messages +
                ", users=" + users +
                '}';
    }

    public List<ChatClientInterface> getClients() {
        return clients;
    }

}

