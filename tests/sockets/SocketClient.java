import java.io.*;
import java.net.*;

public class SocketClient {
    public static void main(String[] args) {

        /* input clavier, grumble grumble */
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        /* zockets et autres */
        Socket s;
        ObjectOutputStream oos;
        OutputStreamWriter ow;
        String host = null;

        System.out.println("Se connecter sur :");

        try {
            host = in.readLine();
            System.out.println("Connexion sur "+host+" port 4662...");

            s = new Socket(host,4662);
            oos = new ObjectOutputStream(s.getOutputStream());

            System.out.println("Connecté ! Pour quitter, entrez une ligne vide.");

            String tosend;

            do {
                tosend = in.readLine();
                if(!tosend.equals(""))
                    oos.writeObject(tosend);
            } while (!tosend.equals(""));

            /* putzing */
            oos.close();
            s.close();
            
            in.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}
