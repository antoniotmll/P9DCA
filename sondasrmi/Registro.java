//package sondasrmi;

import java.rmi.*;
/*import java.rmi.server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.net.*;
import java.io.*;*/

public class Registro {

	//@SuppressWarnings("deprecation")
	public static void main (String[] args) {

		String URLRegistro = "";

        try {

        	System.setSecurityManager(new RMISecurityManager());
					SondaRMI rmi = new SondaRMI(Integer.parseInt(args[0]));
       		URLRegistro = "/SondaRMI"+args[0];
            Naming.rebind (URLRegistro, rmi);
            System.out.println("Sonda " + args[0] + " preparada");

        } catch (Exception ex) {

            System.out.println(ex);
        }
    }
}
