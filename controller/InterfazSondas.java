import java.rmi.*;
import java.net.MalformedURLException;

public interface InterfazSondas extends Remote {

  public void Escribir(int invernadero, int temperatura, int humedad, String fecha, String hora) throws RemoteException;
  public int getSonda() throws RemoteException;
  public int getId() throws RemoteException;
  public int getHumedad() throws RemoteException;
  public int getTemperatura() throws RemoteException;
  public String getFecha() throws RemoteException;
  public String getHora() throws RemoteException;
  public void setTemperatura(int temp) throws RemoteException;
  public void setHumedad(int hum) throws RemoteException;

}
