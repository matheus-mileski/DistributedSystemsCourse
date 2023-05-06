/**
 * Laboratorio 3
 * Autor: Lucio Agostinho Rocha
 * Ultima atualizacao: 04/04/2023
 */

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServidorImpl implements IMensagem {
    private Principal principal = new Principal();

    public ServidorImpl() {}

    //Cliente: invoca o metodo remoto 'enviar'
    //Servidor: invoca o metodo local 'enviar'
    @Override
    public Mensagem enviar(Mensagem mensagem) throws RemoteException {
        Mensagem resposta;
        try {
            System.out.println("Mensagem recebida: " + mensagem.getMensagem());
            resposta = new Mensagem(parserJSON(mensagem.getMensagem()));
        } catch (Exception e) {
            e.printStackTrace();
            resposta = new Mensagem("{\n" + "\"result\": false\n" + "}");
        }
        return resposta;
    }

    public String parserJSON(String json) {
        String result = "{\"result\":";
        try {
            String jsonObjAttributes[] = json.replace("\"", "")
                    .replace("{", "")
                    .replace("}", "")
                    .split(",");
            String method = jsonObjAttributes[0].replace("method:", "");

            if (method.equals("read")) {
                String fortune = principal.read();
                result += "\"" + fortune + "\"}";
            } else if (method.equals("write")) {
                String fortune = jsonObjAttributes[1].replace("args:[", "")
                        .replace("]", "") + "\n";
                principal.write(fortune);
                result += "\"" + fortune + "\"}";
            } else {
                result += "\"false\"}";
            }
        } catch(Exception error) {
            result += "\"false\"}";
        }

        return result;
    }

    public void iniciar() {
        try {
            Registry servidorRegistro = LocateRegistry.createRegistry(1099);
            IMensagem skeleton = (IMensagem) UnicastRemoteObject.exportObject(this, 0); //0: sistema operacional indica a porta (porta anonima)
            servidorRegistro.rebind("servidorFortunes", skeleton);
            System.out.print("Servidor RMI: Aguardando conexoes...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServidorImpl servidor = new ServidorImpl();
        servidor.iniciar();
    }
}
