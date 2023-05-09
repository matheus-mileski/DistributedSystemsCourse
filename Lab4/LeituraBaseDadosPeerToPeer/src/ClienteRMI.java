/**
 * Laboratorio 4
 * Autor: Lucio Agostinho Rocha
 * Ultima atualizacao: 04/04/2023
 */

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClienteRMI {

    // Método para realizar a leitura de uma fortuna
    public void readFortune(IMensagem stub) {
        try {
            // Implementação do método 'read' para a leitura de 1 (uma) fortuna
            // aleatória do servidor.
            Mensagem mensagem = new Mensagem("", "1"); // Criar a mensagem com a operação 'read'
            Mensagem resposta = stub.enviar(mensagem); // Invocar o método remoto
            System.out.println("Fortuna recebida: " + resposta.getMensagem()); // Exibir a fortuna recebida
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para realizar a escrita de uma fortuna
    public void writeFortune(IMensagem stub) {
        try {
            // Implementação do método 'write' para a escrita de 1 (uma) fortuna
            // aleatória no servidor.
            Scanner sc = new Scanner(System.in);
            System.out.println("Digite uma fortuna para adicionar: ");
            String novaFortuna = sc.nextLine(); // Captura a fortuna digitada pelo usuário
            Mensagem mensagemEscrita = new Mensagem(novaFortuna, "2"); // Criar a mensagem com a operação 'write'
            stub.enviar(mensagemEscrita); // Invocar o método remoto
            System.out.println("Fortuna escrita com sucesso!"); // Exibir confirmação de escrita bem-sucedida
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {

        //TODO: Obter a Lista de pares disponiveis do arquivo Peer.java
        Peer[] listaPeers = Peer.values();
        ClienteRMI cliente = new ClienteRMI();
        try {

            Registry registro = LocateRegistry.getRegistry("127.0.0.1", 1099);


            //Escolhe um peer aleatorio da lista de peers para conectar
            SecureRandom sr = new SecureRandom();

            IMensagem stub = null;
            Peer peer = null;

            boolean conectou = false;
            while (!conectou) {
                peer = listaPeers[sr.nextInt(listaPeers.length)];
                try {
                    stub = (IMensagem) registro.lookup(peer.getNome());
                    conectou = true;
                } catch (java.rmi.ConnectException e) {
                    System.out.println(peer.getNome() + " indisponivel. ConnectException. Tentanto o proximo...");
                } catch (java.rmi.NotBoundException e) {
                    System.out.println(peer.getNome() + " indisponivel. NotBoundException. Tentanto o proximo...");
                }
            }
            System.out.println("Conectado no peer: " + peer.getNome());


            String opcao = "";
            Scanner leitura = new Scanner(System.in);
            do {
                System.out.println("1) Read");
                System.out.println("2) Write");
                System.out.println("x) Exit");
                System.out.print(">> ");
                opcao = leitura.next();
                switch (opcao) {
                    case "1": {
                        cliente.readFortune(stub);
                        break;
                    }
                    case "2": {
                        cliente.writeFortune(stub);
                        break;
                    }
                }
            } while (!opcao.equals("x"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
