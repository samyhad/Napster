import java.rmi.registry.LocateRegistry;
import java.rmi.Registry;
import model.ServicoHora;
import model.Hora;

public class Cliente {

  public static void main(String[] args){
    Registry reg = LocateRegistry.getRegistry();
    ServicoHora shc = (ServicoHora) reg.lookup("rmi://127.0.0.1/servicoHora");
    Hora h = shc.obterHora("Cli1")
      System.out.println(hr.nomeCliente +" "+ hr.timestamp);
    
      
  }
}
