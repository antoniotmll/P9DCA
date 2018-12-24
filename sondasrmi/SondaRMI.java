import java.io.Serializable;
import java.rmi.*;
import java.rmi.server.*;
import java.io.*;
import java.util.*;

public class SondaRMI extends UnicastRemoteObject implements Serializable, InterfazSondas {

//	String tipoSensor;
	int temperatura;
	int humedad;
	String fecha;
	String hora;
	int sonda; // ¿valdria como identificador de la sonda?
	int invernadero;


	//preguntar si va ahí el id
	public SondaRMI(int sonda) throws RemoteException {

		super();
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		this.sonda = sonda;

		try {

			archivo = new File ("sonda"+sonda+".txt");
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);

			String linea;
			while((linea = br.readLine()) != null) {

				String[] arrayLinea = linea.split("=");

				for(int i = 0; i < arrayLinea.length; i++) {

					if(arrayLinea[i].equals("Identificador")) {
						invernadero = Integer.parseInt(arrayLinea[i+1]);
					}

					if (arrayLinea[i].equals("Temperatura")) {
						temperatura = Integer.parseInt(arrayLinea[i+1]);
					}

					if (arrayLinea[i].equals("Humedad")) {
						humedad = Integer.parseInt(arrayLinea[i+1]);
					}

					if (arrayLinea[i].equals("Fecha")) {
						fecha = (arrayLinea[i+1]);
					}

					if (arrayLinea[i].equals("Hora")) {
						hora = (arrayLinea[i+1]);
					}
				}
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

		System.out.println("El identificador es: " + invernadero);
		System.out.println("La temperatura es: " + temperatura);
		System.out.println("La humedad es: " + humedad);
		System.out.println("La fecha es: " + fecha);
		System.out.println("La hora es: " + hora);

	}

	public void Escribir(int invernadero, int temperatura, int humedad, String fecha, String hora){

		FileWriter fichero = null;
		PrintWriter pw = null;

		try {

			fichero = new FileWriter("sonda"+sonda+".txt");
			pw = new PrintWriter(fichero);

			pw.println("Identificador="+invernadero);
			pw.println("Temperatura="+temperatura);
			pw.println("Humedad="+humedad);
			pw.println("Fecha="+fecha);
			pw.println("Hora="+hora);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/*
	 * GETTERS Y SETTERS
	 */

	public int getSonda() {

		return sonda;
	}

	public int getId() {

		return invernadero;
	}

	public int getTemperatura() {

		return temperatura;
	}

	public int getHumedad() {

		return humedad;
	}

	public String getFecha() {

		Calendar dat = new GregorianCalendar();
		int dia = dat.get(Calendar.DAY_OF_MONTH);
		int mes = dat.get(Calendar.MONTH) + 1;
		int anyo = dat.get(Calendar.YEAR);

		this.fecha = String.valueOf(dia)+ "/" + String.valueOf(mes)+ "/" + String.valueOf(anyo);

		return this.fecha;
	}

	public String getHora() {

	 Calendar dat = new GregorianCalendar();
	 int hour = dat.get(Calendar.HOUR_OF_DAY);
	 int min = dat.get(Calendar.MINUTE);
	 int seg = dat.get(Calendar.SECOND);

	 this.hora = String.valueOf(hour)+ ":" + String.valueOf(min)+ ":" + String.valueOf(seg);

		return this.hora;
	}


	/*
	* ACTUADORES
	*/

	public void setTemperatura(int temp) {

		temperatura = temp;
		System.out.println("La temperatura ahora es: " + temp);
		Escribir(invernadero, temperatura, humedad, fecha, hora);
	}

	public void setHumedad(int hum) {

		humedad = hum;
		System.out.println("La temperatura ahora es: " + hum);
		Escribir(invernadero, temperatura, humedad, fecha, hora);
	}
}
