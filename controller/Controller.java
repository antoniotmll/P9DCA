
import java.net.*;

public class Controller {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		String puerto = ""; //variable donde se guarda el puerto a escuchar
		String iprmi = "";
		String puertormi = "";
		
		
		try {
			
			//Comprobamos el número de argumentos
			if (args.length < 3) {
				
				System.out.println("Debe indicar el puerto de escucha del servidor y"
						+ " la ip y el puerto rmi");
				
				System.out.println("$./Servidor puerto_servidor ip_rmi puerto_rmi");
				System.exit(1);
			}
			
			puerto = args[0]; //Primer argumento 
			iprmi = args[1]; //Segundo argumento
			puertormi = args[2]; //Tercer argumento
			
			
			ServerSocket skServidor  = new ServerSocket(Integer.parseInt(puerto));
			System.out.println("Escucho al puerto " + puerto);
			
			
			//Mantenemos la comunicación con el cliente
			for(;;) {
				
				//Se espera a que el cliente quiera conectarse
				Socket skCliente = skServidor.accept(); //Crea objeto
				System.out.println("Sirviendo al cliente...");
				
				
				//Hilos para atender a los diferentes clientes
				Thread t = new HiloController(skCliente, iprmi, puertormi);
				t.start();			
			
			}			
		}
		
		catch(Exception e) {
			
			System.out.println("Error: " + e.toString());
		}
	}
	

}

