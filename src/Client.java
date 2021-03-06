import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class Client {
    private ClientUI gui;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean running;

    public Client() {
        gui = new ClientUI(this);
    }
    public void buttonPressed(ButtonType button) {
        switch (button) {
            case Connect:
                connect();
                break;
            case Disconnect:
                //Kommer snart
                break;
            case Send:
                //Kommer snart

        }

    }
    //Ansluter och skickar användaren till servern.
    private void connect() {
        String host = gui.getTfHost().getText();
        int port = Integer.parseInt(gui.getTfPort().getText());
        try {
            socket = new Socket(host, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user = getUserFromView();
        sendUserToServer(user);
        new ListenToServer().start();
    }
    private User getUserFromView() {
        String username = gui.getTfName().getText();
        ImageIcon profilePicture = gui.getProfilePicture();
        User user = new User(username, profilePicture);
        return user;
    }
    private void sendUserToServer(User user) {
        try {
            out.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    private class ListenToServer extends Thread {

        public void run() {
            running = true;
            while(running) {
                try {
                    Object obj = in.readObject();
                    if(obj instanceof User) {
                        //Kommer snart
                    } else if (obj instanceof Message) {
                        //Kommer snart
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        new Client();
    }
}
