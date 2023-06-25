package chat.model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.net.*;
import java.time.ZonedDateTime;
import java.util.Scanner;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private String chatId;
    private String content;
    private ZonedDateTime timestamp;
    private User sender;

    public Message(String chatId, String content, User sender) throws MalformedURLException {
        this.chatId = chatId;
        this.content = content;
        this.sender = sender;
        this.timestamp = worldTimeAPI();
    }

    public String getContent() {
        return content;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public User getSender() {
        return sender;
    }

    public ZonedDateTime worldTimeAPI() throws MalformedURLException {

        HttpURLConnection conn = null;
        try {
            URI uri = new URI("http://worldtimeapi.org/api/timezone/America/Sao_Paulo");
            URL url = uri.toURL();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                StringBuilder inline = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }

                //Close the scanner
                scanner.close();

                //Using the JSON simple library to parse the string into a json object
                JSONParser parse = new JSONParser();
                JSONObject data_obj = null;
                try {
                    data_obj = (JSONObject) parse.parse(inline.toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                String strDate = data_obj.get("datetime").toString();
                return ZonedDateTime.parse(strDate);
            }

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "chatId='" + chatId + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", sender=" + sender.getNickname() +
                '}';
    }
}
