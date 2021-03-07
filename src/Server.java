import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {
    private int port;
    private Clients clients;
    private UnsentMessages unsent;

    public Server(int port) {
        this.port = port;
        clients = new Clients();
        unsent = new UnsentMessages();
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
                System.out.println(user.getUsername() + " är ansluten");
                clients.put(user, this);

                checkNewMessages(user);

                while (true) {
                    try {
                        Message message = (Message) ois.readObject();
                        message.setReceived();
                        deliverMessage(message);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }


            System.out.println("Klient nerkopplad");
        }

        private void checkNewMessages(User user) {
            ArrayList<Message> messages = unsent.get(user);

            if(messages != null) {
                for (Message message: messages) {
                    try {
                        oos.writeObject(message);
                        oos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void deliverMessage(Message message) {

            for (User reveiver: message.getReceivers()) {
                if(clients.containsKey(reveiver)) {
                    try {
                        ObjectOutputStream out = clients.get(reveiver).getOos();
                        out.writeObject(message);
                        out.flush();
                        message.setDelivered();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    unsent.put(reveiver, message);
                }
            }
        }
    }
    //ToDo: Servern ska klara av att en användare (klient) avslutar sin anslutning till systemet.

    //ToDo: uppdatera anslutna användare med en lista vilken innehåller samtliga anslutna användare.
    // Uppdatering ska ske varje gång någon användare ansluter sig / avslutar sin anslutning.

    //ToDo: Logga all trafik i systemet till fil/filer på hårddisken. Via ett UI på serversidan ska man kunna
    // se all trafik mellan två valda tidpunkter.

    public static void main(String[] args) {
        new Server(123);
    }
}
