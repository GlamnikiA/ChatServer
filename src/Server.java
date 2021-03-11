import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


/*
* Flertrådad chatserver.
*/
public class Server implements Runnable {
    private int port;
    private Clients clients;
    private UnsentMessages unsent;
    private Logger logger;
    private ArrayList<User> users;
    private ConcurrentHashMap<String, ArrayList<Message>> unsentMessages = new ConcurrentHashMap<>();

    public Server(int port) {
        this.port = port;
        clients = new Clients();
        users = new ArrayList<>();
        unsent = new UnsentMessages();
        logger = new Logger();
        logger.CreateLoggerGUI();
        new Thread(this).start();
    }

    @Override
    public void run() {
        System.out.println("Server startad");

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Server ansulten till klient");
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void broadcastUser(User user) throws IOException {
        for (User u: users) {
            clients.get(u).getOos().writeObject(user);
        }
    }
    public void removeUser(User user){
        clients.remove(user);
        users.remove(user);
    }

    /*
    * Inre klass för att hantera klienter som ansluter sig till servern.
    */
    public class ClientHandler extends Thread {
        private Socket socket;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        private User user;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        }

        public ObjectOutputStream getOos() {
            return oos;
        }


        @Override
        public void run() {
            try {
                user = (User) ois.readObject();
                broadcastUser(user);
                checkNewMessages(user);
                unsent.clear();
                clients.put(user, this);
                users.add(user);
                for (User u : users) {
                    oos.writeObject(u);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                while (true) {
                    Object obj = ois.readObject();
                    if (obj instanceof User) {
                        if (((User) obj).isConnected()) {
                            clients.put((User) obj, this);
                            users.add((User) obj);
                            for (User u : users) {
                                oos.writeObject(u);
                            }
                        } else {
                            removeUser((User) obj);
                            broadcastUser((User) obj);
                            break;
                        }
                    } else if (obj instanceof Message) {
                        ((Message) obj).setReceived();
                        deliverMessage((Message) obj);

                    }
                }
                close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println("Klient nerkopplad");
            logger.LogDisconnect(user.getUsername(), socket.getInetAddress().toString());
        }

        /*
        * Stänger nätverksstömmar och socket.
        * Returtyp: void
        */
        private void close() {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if(ois != null) {
                    ois.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if(socket != null) {
                    socket.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*
        * Kollar om det finns några olevererade meddelanden givet ett användarnamn. Om det finns så levereras meddelandena.
        * Parameter: User
        * Returtyp: void
        */

        private void checkNewMessages(User user) {
            ArrayList<Message> messages = unsent.get(user.getUsername());

            if (messages != null) {
                for (Message message : messages) {
                    try {
                        Message msg = new Message(message.getSender(), message.getReceivers(),"Oläst meddelande : " + message.getText(), message.getImage());
                        msg.setDelivered();
                        oos.writeObject(msg);
                        logger.LogMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /*
        * Levererar ett mottaget meddelande till mottagare. Om en mottagare är offline sparas meddelandet till
        * mottagaren kommer online.
        * Inparameter: Message
        * Returtyp: void
        */
        private void deliverMessage(Message message) {
            for (User receiver : message.getReceivers()) {
                for (User u : users) {
                    if (u.equals(receiver)) {
                        try {
                            clients.get(u).getOos().writeObject(message);
                            message.setDelivered();
                            logger.LogMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        unsent.put(receiver.getUsername(), message);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new Server(101);
    }
}
