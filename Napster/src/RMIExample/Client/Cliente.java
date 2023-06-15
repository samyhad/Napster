package RMIExample.Client;


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import RMIExample.model.ServicoHora;
import RMIExample.model.Hora;

public class Cliente {

  public static void main(String[] args) throws Exception{
    Registry reg = LocateRegistry.getRegistry();
    ServicoHora shc = (ServicoHora) reg.lookup("rmi://127.0.0.1/servicoHora");
    Hora h = shc.obterHora("Cli1");
      System.out.println(h.nomeCliente +" "+ h.timestamp);
    
      
  }
}
