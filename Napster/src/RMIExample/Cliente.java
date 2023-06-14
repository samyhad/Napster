import java.rmi.registry.LocateRegistry;
import java.rmi.Registry;

public class Cliente {

  public static void main(String[] args){
    Registry reg = LocateRegistry.getRegistry();
    reg.lookup("rmi://127.0.0.1/servicohora")
  }
}
