/**
 * Laboratorio 4
 * Autor: Lucio Agostinho Rocha
 * Ultima atualizacao: 04/04/2023
 */

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class ServidorImpl implements IMensagem {
    Principal principal = new Principal();
    ArrayList<Peer> alocados;

    public ServidorImpl() {
        alocados = new ArrayList<>();
    }

    public static void main(String[] args) {
        ServidorImpl servidor = new ServidorImpl();
        servidor.iniciar();
    }

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
            String jsonObjAttributes[] = json.replace("\"", "").replace("{", "").replace("}", "").split(",");
            String method = jsonObjAttributes[0].replace("method:", "");

            if (method.equals("read")) {
                String fortune = principal.read();
                result += "\"" + fortune + "\"}";
            } else if (method.equals("write")) {
                String fortune = jsonObjAttributes[1].replace("args:[", "").replace("]", "") + "\n";
                principal.write(fortune);
                result += "\"" + fortune + "\"}";
            } else {
                result += "\"false\"}";
            }
        } catch (Exception error) {
            result += "\"false\"}";
        }

        return result;
    }

    public void iniciar() {
        try {
            //TODO: Adquire aleatoriamente um 'nome' do arquivo Peer.java
            Peer[] listaPeers = Peer.values();
            Registry servidorRegistro;
            try {
                servidorRegistro = LocateRegistry.createRegistry(1099);
            } catch (java.rmi.server.ExportException e) { //Registro jah iniciado
                System.out.print("Registro jah iniciado. Usar o ativo.\n");
            }
            servidorRegistro = LocateRegistry.getRegistry(); //Registro eh unico para todos os peers
            String[] listaAlocados = servidorRegistro.list();
            for (int i = 0; i < listaAlocados.length; i++)
                System.out.println(listaAlocados[i] + " ativo.");

            SecureRandom sr = new SecureRandom();
            Peer peer = listaPeers[sr.nextInt(listaPeers.length)];

            int tentativas = 0;
            boolean repetido = true;
            boolean cheio = false;
            while (repetido && !cheio) {
                repetido = false;
                peer = listaPeers[sr.nextInt(listaPeers.length)];
                for (int i = 0; i < listaAlocados.length && !repetido; i++) {

                    if (listaAlocados[i].equals(peer.getNome())) {
                        System.out.println(peer.getNome() + " ativo. Tentando proximo...");
                        repetido = true;
                        tentativas = i + 1;
                    }

                }
                //System.out.println(tentativas+" "+listaAlocados.length);

                //Verifica se o registro estah cheio (todos alocados)
                if (listaAlocados.length > 0 && //Para o caso inicial em que nao ha servidor alocado,
                        //caso contrario, o teste abaixo sempre serah true
                        tentativas == listaPeers.length) {
                    cheio = true;
                }
            }

            if (cheio) {
                System.out.println("Sistema cheio. Tente mais tarde.");
                System.exit(1);
            }

            IMensagem skeleton = (IMensagem) UnicastRemoteObject.exportObject(this, 0); //0: sistema operacional indica a porta (porta anonima)
            servidorRegistro.rebind(peer.getNome(), skeleton);
            System.out.print(peer.getNome() + " Servidor RMI: Aguardando conexoes...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
