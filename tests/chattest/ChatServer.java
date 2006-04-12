import java.io.*;
import java.net.*;
import java.util.Vector;

public class ChatServer {
    public static void main(String[] args) {

        /* input clavier, grumble grumble */
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        /* zockets et autres */
        ServerSocket ss;
        Broadcaster b = new Broadcaster();

        try {
            System.out.println("Attente sur le port 4662...");
            ss = new ServerSocket(4662);

            System.out.println("En cours.");

            do {
                Socket s = ss.accept();
                b.add(s);
                new ChatServerThread(s, b).start();
            } while (true);

            /* putzing */
            //ss.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        //in.close();
    }

}

class ChatServerThread extends Thread {

    Broadcaster b;
    Socket s;
    String host;

    ObjectInputStream ois;

    ChatServerThread(Socket socket, Broadcaster broadcaster) {
        this.s = socket;
        this.host = s.getInetAddress().toString();
        this.b = broadcaster;
    }

    public void run() {
        try {
            Object obj;
            ois = new ObjectInputStream(s.getInputStream());

            System.out.println("Connexion : "+host);
            b.broadcast("Connexion : "+host);
            while(s.isConnected()) {
                obj = ois.readObject();
                b.broadcast("<"+host+"> : "+((String) obj));
                System.out.println("<"+host+"> : "+(String) obj);
            }
            System.out.println("Déconnexion : "+host);

            ois.close();
        } catch (EOFException eofe) {
            b.remove(s);
            System.out.println("Déconnexion : "+host);
            b.broadcast("Déconnexion : "+host);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }
}

class Broadcaster {

    Vector v;

    public Broadcaster() {
        v = new Vector();
    }

    public synchronized void add(Socket s) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            v.add(oos);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public synchronized void remove(Socket s) {
        try {
            for(int i = 0 ; i < v.size() ; i++) {
                if(s.getOutputStream() == v.get(i)) {
                    ((ObjectOutputStream) v.get(i)).close();
                    v.remove(v.get(i));
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public synchronized void broadcast(Object obj) {
        try {
            for(int i = 0 ; i < v.size() ; i++) {
                ((ObjectOutputStream) v.get(i)).writeObject(obj);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
