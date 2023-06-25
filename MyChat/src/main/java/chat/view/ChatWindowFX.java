package chat.view;

import chat.client.ChatClientMain;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class ChatWindowFX extends Application {
    private static TextArea textArea;
    private TextField textField;
    private Button sendButton;
    private ChatController controller;

    // Method to append a message to the text area
    public static void appendMessage(String message) {
        textArea.appendText(message + "\n");
    }

    @Override
    public void start(Stage primaryStage) throws RemoteException {
        // Components
        textArea = new TextArea();
        textArea.setEditable(false); // Prevent user from typing directly into the text area

        ScrollPane scrollPane = new ScrollPane(textArea); // Add scrolling functionality
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        textField = new TextField();
        sendButton = new Button("Send");

        // Layout
        BorderPane root = new BorderPane();
        root.setCenter(scrollPane);

        HBox bottomLayout = new HBox(10, textField, sendButton);
        bottomLayout.setAlignment(Pos.CENTER);
        root.setBottom(bottomLayout);

        // Initialize ChatController here when the JavaFX application starts
        ChatClientMain chatClientMain = ChatClientMain.getInstance();
        controller = new ChatController(
                chatClientMain.getServer(),
                chatClientMain.getClient(),
                chatClientMain.getUser(),
                textArea,
                textField
        );
        chatClientMain.getServer().joinDefaultChatRoom(chatClientMain.getUser(), chatClientMain.getClient());

        // Event handling
        sendButton.setOnAction(event -> {
            String text = textField.getText();
            if (!text.isBlank()) {
                // Invoke the RMI method to send the message to the server here
                try {
                    controller.sendMessageToServer("geral", text);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                // Clear the text field
                textField.setText("");
            }
        });

        // Event handling to send message pressing Enter
        textField.setOnAction(event -> {
            String text = textField.getText();
            if (!text.isBlank()) {
                // Invoke the RMI method to send the message to the server here
                try {
                    controller.sendMessageToServer("geral", text);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                // Clear the text field
                textField.setText("");
            }
        });

        // Set up the stage
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setTitle(chatClientMain.getUser().getNickname() + " - Chat Geral");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
