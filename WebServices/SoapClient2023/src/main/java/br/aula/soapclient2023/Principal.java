package br.aula.soapclient2023;

public class Principal {
    public static void main(String[] args) {
        System.out.println("Principal: envia mensagem para o servidor");
        //String soapEndpointUrl = "http://wscorreio.multilaser.com.br/soap/AtendeCliente.wsdl";
        String soapEndpointUrl = "https://apps.correios.com.br/SigepMasterJPA/AtendeClienteService/AtendeCliente?wsdl";
        String soapAction = "http://cliente.bean.master.sigep.bsb.correios.com.br";
        SoapClient sc = new SoapClient();

        //Requisicao1
        System.out.println("\n---Requisicao1---");
        sc.setCEP("86812600");
        sc.callSoapWebService(soapEndpointUrl, soapAction);

        System.out.println("Fim!");
    }
}
