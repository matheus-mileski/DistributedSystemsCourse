package chat.view;

import chat.client.ChatClient;
import chat.model.Message;
import chat.model.User;
import chat.server.ChatServerInterface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.Serial;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class ChatController implements Serializable {
    @Serial
    private static final long serialVersionUID = 4L;
    private final ChatServerInterface server;
    private final ChatClient client;
    private final User localUser;
    private final TextArea chatTextArea;
    @FXML
    private final TextField textField;

    public ChatController(ChatServerInterface server, ChatClient client, User localUser, TextArea chatTextArea, TextField textField) {
        this.server = server;
        this.client = client;
        this.localUser = localUser;
        this.chatTextArea = chatTextArea;
        this.textField = textField;
    }

    public void sendMessageToServer(String chatRoomId, String text) throws MalformedURLException {
        try {
            Message message = new Message(chatRoomId, text, localUser);
            server.sendMessage(chatRoomId, message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(ActionEvent event) throws MalformedURLException {
        String text = textField.getText();
        if (!text.isBlank()) {
            // Invoke the method to send the message to the server
            sendMessageToServer("geral", text);
            // Then clear the text field
            textField.setText("");
        }
    }

}

