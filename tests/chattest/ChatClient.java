import java.io.*;
import java.net.*;

public class ChatClient {
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

            System.out.println("Connecté ! Pour quitter, entrez /quit.");

            String tosend;

            new ClientThread(s).start();

            do {
                tosend = in.readLine();
                if(!tosend.equals("/quit"))
                    oos.writeObject(tosend);
            } while (!tosend.equals("/quit"));

            /* putzing */
            oos.close();
            s.close();
            
            in.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}

class ClientThread extends Thread {

    Socket s;
    ObjectInputStream ois;

    ClientThread(Socket s) {
        this.s = s;
    }

    public void run() {
        try {
            ois = new ObjectInputStream(s.getInputStream());

            while(s.isConnected()) {
                Object o = ois.readObject();
                System.out.println((String) o);
            }

            ois.close();
        } catch (SocketException se) {
            System.out.println("Déconnecté.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }
}
