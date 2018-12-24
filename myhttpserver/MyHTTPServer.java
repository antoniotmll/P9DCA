
import java.net.*;

public class MyHTTPServer {
	
	public static int maxCon;
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		String puerto = ""; //variable donde se guarda el puerto a escuchar
		String peticion = ""; //variable donde se guarda el número de peticiones concurrentes
		String ipCtrl = ""; //var para guardar la ip
		String puertoCtrl = ""; //var para guardar el puerto del controlador
		
		
		try {
			
			//Comprobamos el número de argumentos
			if (args.length < 4) {
				
				System.out.println("Debe indicar el puerto de escucha del servidor y"
						+ " el número de peticiones concurrentes, la ip y el puerto del controlador");
				
				System.out.println("$./Servidor puerto_servidor peticiones_concurrentes ip_controlador puerto_controlador");
				System.exit(1);
			}
			
			puerto = args[0]; //Primer argumento 
			peticion = args[1]; //Segundo argumento
			ipCtrl = args[2]; //Tercer argumento
			puertoCtrl = args[3]; //Cuarto argumento
			
			
			ServerSocket skServidor  = new ServerSocket(Integer.parseInt(puerto),Integer.parseInt(peticion));
			System.out.println("Escucho al puerto " + puerto);
	
			maxCon = Integer.parseInt(peticion);
			
			//Mantenemos la comunicación con el cliente
			for(;;) {
				
				if (maxCon > 0) {
				//Se espera a que el cliente quiera conectarse
				Socket skCliente = skServidor.accept(); //Crea objeto
				System.out.println("Sirviendo al cliente...");
				
				
				//Hilos para atender a los diferentes clientes
				Thread t = new HiloServidor(skCliente, ipCtrl, puertoCtrl);
				t.start();			
				
				 maxCon--;
				
				}
			}			
		}
		
		catch(Exception e) {
			
			System.out.println("Error: " + e.toString());
		}
	}
	

}

