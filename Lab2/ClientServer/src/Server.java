import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Objects;

public class Server {

    private static Socket socket;
    private static ServerSocket server;

    private static DataInputStream entrada;
    private static DataOutputStream saida;

    private int porta = 1025;
    public final static Path path = Paths
            .get("data\\fortune-br.txt");
    private int NUM_FORTUNES = 0;

    public class FileReader {

        public int countFortunes() throws FileNotFoundException {

            int lineCount = 0;

            InputStream is = new BufferedInputStream(new FileInputStream(
                    path.toString()));
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    is))) {

                String line = "";
                while (!(line == null)) {

                    if (line.equals("%"))
                        lineCount++;

                    line = br.readLine();

                }// fim while

                System.out.println(lineCount);
            } catch (IOException e) {
                System.out.println("SHOW: Excecao na leitura do arquivo.");
            }
            return lineCount;
        }

        public void parser(HashMap<Integer, String> hm)
                throws FileNotFoundException {

            InputStream is = new BufferedInputStream(new FileInputStream(
                    path.toString()));
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    is))) {

                int lineCount = 0;

                String line = "";
                while (!(line == null)) {

                    if (line.equals("%"))
                        lineCount++;

                    line = br.readLine();
                    StringBuffer fortune = new StringBuffer();
                    while (!(line == null) && !line.equals("%")) {
                        fortune.append(line + "\n");
                        line = br.readLine();
                        // System.out.print(lineCount + ".");
                    }

                    hm.put(lineCount, fortune.toString());
//                    System.out.println(fortune.toString());

//                    System.out.println(lineCount);
                }// fim while

            } catch (IOException e) {
                System.out.println("SHOW: Excecao na leitura do arquivo.");
            }
        }

        public String read(HashMap<Integer, String> hm)
                throws FileNotFoundException {

            SecureRandom rand = new SecureRandom();
            int fortuneKey = rand.nextInt(NUM_FORTUNES);
            String fortuneValue = hm.get(fortuneKey);

            System.out.println("Random fortune (" + fortuneKey + "): " + fortuneValue);

            return fortuneValue;
        }

        public void write(HashMap<Integer, String> hm, String fortuneValue)
                throws FileNotFoundException {

            try {
                FileWriter writer = new FileWriter(path.toString(), true);
                writer.write("\n%\n" + fortuneValue);
                writer.close();
                System.out.println("Random fortune appended to file: " + path.toString());

                // Update the hashmap and the total of fortunes
                parser(hm);
                NUM_FORTUNES = countFortunes();
            } catch (IOException e) {
                System.out.println("An error occurred while writing to file: " + e.getMessage());
            }
        }
    }

    public void iniciar() {
        System.out.println("Servidor iniciado na porta: " + porta);
        try {
            FileReader fr = new FileReader();

            // Criar porta de recepcao
            server = new ServerSocket(porta);
            loop: while (true) {
                socket = server.accept();  //Processo fica bloqueado, ah espera de conexoes

                NUM_FORTUNES = fr.countFortunes();
                HashMap hm = new HashMap<Integer, String>();
                fr.parser(hm);

                // Criar os fluxos de entrada e saida
                entrada = new DataInputStream(socket.getInputStream());
                saida = new DataOutputStream(socket.getOutputStream());

                // Recebimento do valor inteiro
                String valor = entrada.readUTF();
                System.out.println(valor);

                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(valor);

                switch (obj.get("method").toString()){
                    case "read":
                        String msg = fr.read(hm);

                        JSONObject responseRead = new JSONObject();

                        responseRead.put("result", msg);

                        saida.writeUTF(responseRead.toJSONString());

                        break;
                    case "write":
                        String msgWrite = obj.get("args").toString();
                        fr.write(hm, msgWrite);

                        boolean endsWithNewline = msgWrite.endsWith("\n");
                        JSONObject responseWrite = new JSONObject();

                        if(!endsWithNewline){
                            responseWrite.put("result", "false");
                        }else{
                            responseWrite.put("result", msgWrite);
                        }

                        saida.writeUTF(responseWrite.toJSONString());

                        break;
                    case "shutdown":
                        saida.writeUTF("shutdown server");
                        break loop;
                    default:
                        saida.writeUTF("invalid method");
                        break loop;

                }
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new Server().iniciar();

    }

}