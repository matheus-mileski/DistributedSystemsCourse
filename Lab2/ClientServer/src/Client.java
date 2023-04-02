import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Client {

    private static Socket socket;
    private static DataInputStream entrada;
    private static DataOutputStream saida;

    private int porta = 1025;

    public void read() throws IOException, ParseException {
        System.out.println("Cliente iniciado na porta: " + porta);

        socket = new Socket("127.0.0.1", porta);

        entrada = new DataInputStream(socket.getInputStream());
        saida = new DataOutputStream(socket.getOutputStream());

        JSONObject obj = new JSONObject();

        obj.put("method", "read");
        obj.put("args", "");

        saida.writeUTF(obj.toJSONString());
        String resultado = entrada.readUTF();

        JSONParser parser = new JSONParser();
        JSONObject response = (JSONObject) parser.parse(resultado);

        System.out.println(response.get("result").toString());

        socket.close();
    }

    public void write(String msg) throws IOException, ParseException {
        System.out.println("Cliente iniciado na porta: " + porta);

        socket = new Socket("127.0.0.1", porta);

        entrada = new DataInputStream(socket.getInputStream());
        saida = new DataOutputStream(socket.getOutputStream());

        JSONObject obj = new JSONObject();

        obj.put("method", "write");
        obj.put("args", msg);

        saida.writeUTF(obj.toJSONString());
        String resultado = entrada.readUTF();

        JSONParser parser = new JSONParser();
        JSONObject response = (JSONObject) parser.parse(resultado);

        System.out.println(response.get("result").toString());

        socket.close();
    }

    public void shutdownServer() throws IOException{
        System.out.println("Cliente iniciado na porta: " + porta);

        socket = new Socket("127.0.0.1", porta);

        entrada = new DataInputStream(socket.getInputStream());
        saida = new DataOutputStream(socket.getOutputStream());

        JSONObject obj = new JSONObject();

        obj.put("method", "shutdown");
        obj.put("args", "");

        saida.writeUTF(obj.toJSONString());
        String resultado = entrada.readUTF();
        System.out.println(resultado);

        socket.close();
    }

    public void displayMenu() throws IOException, ParseException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Please select an option:");
            System.out.println("1. Read");
            System.out.println("2. Write");
            System.out.println("3. Exit");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    System.out.println("You selected 'Read'");
                    try {
                        read();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "2":
                    System.out.println("You selected 'Write'");
                    System.out.println("Please enter the message:");
                    String message = scanner.nextLine();
                    System.out.println("You entered: " + message);

                    write(message);

                    break;
                case "3":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid input. Please try again.");
                    break;
            }
        }
    }

    public void iniciar() {
        try {

            displayMenu();
            shutdownServer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client().iniciar();
    }

}