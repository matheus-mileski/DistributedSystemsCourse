package chat.client;

import chat.model.Message;
import chat.model.User;
import chat.server.ChatServerInterface;
import chat.view.ChatWindowFX;
import javafx.application.Platform;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatClient extends UnicastRemoteObject implements ChatClientInterface {
    private final ChatServerInterface server;
    private final User user;

    public ChatClient(ChatServerInterface server, User user) throws RemoteException {
        this.server = server;
        this.user = user;
    }

    public void deliverMessage(Message message) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String text = message.getSender().getNickname() + " (" + message.getTimestamp() + "): " + message.getContent() + "\n";
                ChatWindowFX.appendMessage(text);
            }
        });
    }

}
