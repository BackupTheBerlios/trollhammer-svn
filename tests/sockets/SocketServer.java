import java.io.*;
import java.net.*;

public class SocketServer {
    public static void main(String[] args) {

        /* input clavier, grumble grumble */
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        /* zockets et autres */
        ServerSocket ss;

        try {
            System.out.println("Attente sur le port 4662...");
            ss = new ServerSocket(4662);

            System.out.println("En cours.");

            do {
                Socket s = ss.accept();
                new SocketServerThread(s).start();
            } while (true);

            /* putzing */
            //ss.close();
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        //in.close();
    }
}

class SocketServerThread extends Thread {

    Socket s;
    String host;

    ObjectInputStream ois;

    SocketServerThread(Socket socket) {
        this.s = socket;
        this.host = s.getInetAddress().toString();
    }

    public void run() {
        try {
            Object obj;
            ois = new ObjectInputStream(s.getInputStream());

            System.out.println("Connexion : "+host);
            while(s.isConnected()) {
                obj = ois.readObject();

                System.out.println("["+host+"] Objet type : "+obj.getClass().getName()+"\n"+obj.toString());
            }
            System.out.println("Déconnexion : "+host);

            ois.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }
}
