import java.lang.Exception;
import java.net.Socket;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.rmi.*;


public class HiloController extends Thread {

	private Socket skCliente;
	private String iprmi = "";

	private String server = "rmi://"+iprmi+":"+puertormi;

	public HiloController(Socket p_cliente, String iprmi, String puertormi) {

		this.skCliente = p_cliente;
		this.iprmi = iprmi;
		this.puertormi = puertormi;

	}

	/*
	* Lee datos del socket. Supone que se le pasa un buffer con hueco
	*	suficiente para los datos. Devuelve el numero de bytes leidos o
	* 0 si se cierra fichero o -1 si hay error.
	*/
	public String leeSocket (Socket p_sk, String p_Datos){
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



	private String comprobarCasos(String Cadena) {

		if (Cadena.equals("/controladorSD") || Cadena.equals("/controladorSD/")) {

			return leerIndex();
		}

		//Array de Strings
		String[] urltotal;
		String[] sondas;

		//Strings
		String sensor="";
		String sondaynum="";
		String sonda="";

		//sensor
		urltotal = Cadena.substring(15).split("\\?");
		sensor = urltotal[0];

		sondaynum = urltotal[1];
		sondas = sondaynum.split("=");
		sonda = sondas[1];

		server="rmi://"+iprmi+":"+puertormi+"/SondaRMI"+sonda;

		InterfazSondas objeto = null;

		try {
			System.setSecurityManager(new RMISecurityManager());
			objeto = (InterfazSondas) Naming.lookup(server);
		} catch(Exception e) {

			return null;
		}

		String devolver = "";

		switch(sensor) {

			case "sonda":
				try {
					devolver=Integer.toString(objeto.getSonda());
				} catch(Exception e) {
					return null;
				}
				break;

			case "invernadero":
				try {
					devolver=Integer.toString(objeto.getId());
				} catch(Exception e) {
					return null;
				}
				break;

				case "humedad":
					try {
						devolver=Integer.toString(objeto.getHumedad());
					} catch(Exception e) {
						return null;
					}
					break;


					case "temperatura":
						try {
							devolver=Integer.toString(objeto.getTemperatura());
						} catch(Exception e) {
							return null;
						}
						break;

						case "fecha":
							try {
								devolver=objeto.getFecha();
							} catch(Exception e) {
								return null;
							}
							break;

							case "hora":
								try {
									devolver=objeto.getHora();
								} catch(Exception e) {
									return null;
								}
								break;

						default:
							return "<html><body><b><font size=10>ERROR: VARIABLE NO ENCONTRADA</font></b></body></html>";
		}

		return devolver;

	}



	public String leerIndex() {

		String devolver = "";
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;

		try {
			archivo = new File("index.html");
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);

			String linea;
			while((linea = br.readLine()) !=null) {
				devolver = devolver + linea;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return devolver;
	}



	public void run() {

		String Cadena = "";
		String Cadenaux;

		try {

			Cadena = this.leeSocket(skCliente, Cadena);
			System.out.println("El usuario quiere " + Cadena);
			String probar = comprobarCasos(Cadena);
			escribeSocket(skCliente, probar);

			skCliente.close();

		} catch (Exception e) {

			System.out.println("Error: " + e.toString());
		}
	}
}
