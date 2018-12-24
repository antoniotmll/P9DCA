import java.lang.Exception;
import java.net.Socket;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HiloServidor extends Thread {

	private Socket skCliente;
	private String ipCtrl = "";
	private String puertoCtrl = "";

	public HiloServidor(Socket p_cliente, String ipCtrl, String puertoCtrl) {

		this.skCliente = p_cliente;
		this.ipCtrl = ipCtrl;
	

	}

	/*
	* Lee datos del socket. Supone que se le pasa un buffer con hueco
	*	suficiente para los datos. Devuelve el numero de bytes leidos o
	* 0 si se cierra fichero o -1 si hay error.
	*/
	public String leeSocket (Socket p_sk, String p_Datos) {
		try
		{
			InputStream aux = p_sk.getInputStream();
			BufferedReader flujo = new BufferedReader(new InputStreamReader(aux));
			p_Datos = flujo.readLine();

		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
      return p_Datos;
	}



	/*
	* Escribe dato en el socket cliente. Devuelve numero de bytes escritos,
	* o -1 si hay error.
	*/
	public void escribeSocket (Socket p_sk, String p_Datos) {

		try {

			OutputStream aux = p_sk.getOutputStream();
			PrintWriter flujoSalida = new PrintWriter(new OutputStreamWriter(aux));
			flujoSalida.println(p_Datos);
			flujoSalida.flush();

		} catch (Exception e) {

			System.out.println("Error: " + e.toString());
		}
		return;
	}
/*
 * *********************************
 * 			 MÉTODO AÑADIDO
 * *********************************
 */


	@SuppressWarnings("resource")
	public String comprobarCadena(String Cadena) {

		String devolver = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n\r\n";
		String[] arrayLinea = Cadena.split(" ");

		//comprobamos si la petición es GET
		if (arrayLinea[0].equals("GET")) {
			//Para mostrar el index
			if (arrayLinea[1].equals("/")) {
				try {
					//Mostramos el index
					File archivo = null;
					FileReader fr = null;
					BufferedReader br = null;
					archivo = new File("index.html");
					fr = new FileReader(archivo);
					br = new BufferedReader(fr);
					//Leemos el fichero
					String linea;
					while ((linea = br.readLine()) != null) {
						devolver = devolver + linea;
					}
				} catch (Exception ex) {
					Logger.getLogger(HiloServidor.class.getName()).log(Level.SEVERE, null, ex);
				}
				//Si es el controlador
			} else if (Cadena.contains("controladorSD")) {

				try {
											//conecta una ip con un puerto
					Socket sock = new Socket(ipCtrl, Integer.parseInt(puertoCtrl));
					escribeSocket(sock, arrayLinea[1]);
					String devaux = "";
					devolver = devolver+leeSocket(sock,devaux);
					sock.close();

				} catch (Exception ex) {

					System.out.println("Error: 409 Conflict");
					devolver = "HTTP/1.1 409 ERROR\r\n" + "Content-Type: text/html\r\n" + "\r\n\r\n";

					try {
						//Mostramos el index
						File archivo = null;
						FileReader fr = null;
						BufferedReader br = null;
						archivo = new File("error409.html");
						fr = new FileReader(archivo);
						br = new BufferedReader(fr);
						//Leemos el fichero
						String linea;
						while ((linea = br.readLine()) != null) {
							devolver = devolver + linea;
						}
					} catch (Exception exe) {
						return null;
					}

				}
			//Si no es el index ni el controlador, leemos el recurso
		} else {
				//Leemos el archivo
				File archivo = null;
				FileReader fr = null;
				BufferedReader br = null;
				String leer;
				String sinseparar; // /estatico.html
				String separada[];
				try {

					sinseparar = arrayLinea[1];
					separada = sinseparar.split("/");
					archivo = new File(separada[1]);

					if (archivo.exists()) {

						fr = new FileReader(archivo);
						br = new BufferedReader(fr);
						String linea;
						while ((linea = br.readLine()) != null) {
							devolver = devolver + linea;
						}
						//Si se solicita un recurso no accesible (recurso estatico que no existe) ERROR 404
					} else {

						System.out.println("Error: HTTP 404 Not Found");
						devolver = "HTTP/1.1 404 ERROR\r\n" + "Content-Type: text/html\r\n" + "\r\n\r\n";
					//Mostramos el error 404 en la web
					archivo = new File("error404.html");
					fr = new FileReader(archivo);
					br = new BufferedReader(fr);

					//Leemos el fichero
					String linea;
					while ((linea = br.readLine()) != null) {

						devolver = devolver + linea;
					}
					}

				} catch (Exception e) {

					e.printStackTrace();

				} finally {
					/*
					 * Finally para asegurarnos que pase lo que pase el
					 * fichero siempre se cerrará
					 */
					try  {
						if (null != fr) {
							fr.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
			//método no permitido
		} else if (!arrayLinea[0].equals("GET")) { //comprobamos que es diferente de GET

			System.out.println("Error: 405 Method Not Allowed"); //Si el comando es diferente de GET salta el error
		  devolver = "HTTP/1.1 405 ERROR\r\n" + "Content-Type: text/html\r\n" + "\r\n\r\n";

			try {
				//Mostramos el index
				File archivo = null;
				FileReader fr = null;
				BufferedReader br = null;
				archivo = new File("error405.html");
				fr = new FileReader(archivo);
				br = new BufferedReader(fr);
				//Leemos el fichero
				String linea;
				while ((linea = br.readLine()) != null) {
					devolver = devolver + linea;
				}
			} catch (Exception ex) {
				Logger.getLogger(HiloServidor.class.getName()).log(Level.SEVERE, null, ex);
			}

		}

		devolver = devolver + "\r\n";
		return devolver;
	}

	public void run() {
		String Cadena = "";
		String aux;

		try {

			Cadena = this.leeSocket(skCliente, Cadena);
			System.out.println("El usuario quiere " + Cadena);
			aux = comprobarCadena(Cadena);
			escribeSocket(skCliente, aux);
			skCliente.close();

		} catch (Exception e) {

			System.out.println("Error: " + e.toString());
		}
	}
}
