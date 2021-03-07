import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

public class Client {
    private ClientUI gui;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean running;
    private Contacts contacts;
    private ContactsGUI contactsGUI;
    private ArrayList<User> receiverList = new ArrayList<>();

    public Client() {
        gui = new ClientUI(this);
        contacts = new Contacts();
    }
    public void buttonPressed(ButtonType button) {
        switch (button) {
            case Connect:
                connect();
                break;
            case Disconnect:
                disconnect();
                break;
            case Send:
                sendMessage(getMessageFromView());
                break;
            case Contacts:
                contactsGUI = new ContactsGUI(this);
                contactsGUI.createFrame();
                String[] onlineUsers = getUsernames(contacts.getOnlineContacts());
                contactsGUI.setOnlineUserList(onlineUsers);
                break;
            case ContactsSend:
                updateReceiverList();
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
        user.setConnected(true);
        sendUserToServer(user);
        new ListenToServer().start();
    }
    private void disconnect() {
        setRunning(false);
        User user = getUserFromView();
        user.setConnected(false);
        sendUserToServer(user);
        gui.resetGUI();
        gui.getTaChatbox().setText("");
        gui.getTaChatbox().append("Du har lämnat chatten");
        close();
    }
    private void close() {
        try {
            socket.close();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void contactListIndicesChanged(int index) {
    }
    private User getUserFromView() {
        String username = gui.getTfName().getText();
        ImageIcon profilePicture = gui.getProfilePicture();
        User user = new User(username, profilePicture);
        return user;
    }
    private Message getMessageFromView() {
        Message message = null;
        String text = gui.getTfMessage().getText();
        ImageIcon icon = gui.getMessageImage();
        User user = getUserFromView();
        if(receiverList.isEmpty()) {
            message = new Message(user, contacts.getOnlineContacts(), text, icon);
        } else {
            ArrayList<User> newReceiverList = new ArrayList<>(receiverList);
            message = new Message(user, newReceiverList, text, icon);
        }
        return message;
    }
    private void sendUserToServer(User user) {
        try {
            out.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendMessage(Message message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void updateReceiverList() {
        int index = contactsGUI.getListIndex();
        User receiver = contacts.getContactAt(index);
        gui.setTextForLabel(receiver.getUsername());
        receiverList.add(receiver);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
    private String[] getUsernames(ArrayList<User> users) {
        String[] username = new String[users.size()];
        for(int i = 0; i < users.size(); i++) {
            username[i] = users.get(i).getUsername();
        }
        return username;
    }

    private class ListenToServer extends Thread {

        public void run() {
            running = true;
            while(running) {
                try {
                    Object obj = in.readObject();
                    if(obj instanceof User) {
                        if(((User) obj).isConnected()) {
                            contacts.setOnlineUser((User)obj);
                            gui.displayUser(GUIUtilities.createUserLabel(((User) obj).getImage(), ((User) obj).getUsername()));
                        } else {
                            contacts.removeOnlineContact((User)obj);
                            gui.removeUser(GUIUtilities.createUserLabel(((User) obj).getImage(), ((User) obj).getUsername()));
                        }
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
