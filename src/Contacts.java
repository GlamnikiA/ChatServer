import java.io.*;
import java.util.ArrayList;

public class Contacts {
    private ArrayList<User> onlineContacts;
    private ArrayList<User> savedContacts;

    public Contacts() {
        onlineContacts = new ArrayList<>();
        savedContacts = new ArrayList<>();
    }
    public void setOnlineContact(User user) {
        if(!onlineContacts.contains(user)) {
            onlineContacts.add(user);
        }
    }
    public void setSavedContact(User user) {
        if(!savedContacts.contains(user)) {
            savedContacts.add(user);
        }
    }
    public void removeOnlineContact(User user) {
        ArrayList<User> contactsToRemove = new ArrayList<>();
        for (User u : onlineContacts) {
            if (u.equals(user)) {
                contactsToRemove.add(u);
            }
        }
        onlineContacts.removeAll(contactsToRemove);
    }
    public void removeContact(int index) {
        savedContacts.remove(index);
    }
    public void writeContactsToFile(String filename) {
        try(ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
            out.writeInt(savedContacts.size());
            for (User u: savedContacts) {
                out.writeObject(u);
            }
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } ;
    }
    public void readContactsFromFile(String filename) {
        File file = new File(filename);
        if(file.length() > 0) {
            try {
                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
                int n = in.readInt();
                while(n > 0) {
                    setSavedContact((User)in.readObject());
                    n--;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public ArrayList<User> getOnlineContacts() {
        return onlineContacts;
    }
    public ArrayList<User> getSavedContacts() {
        return savedContacts;
    }

    public User getContactAt(int index) {
        return onlineContacts.get(index);
    }
    public User getSavedContactAt(int index) {
        return savedContacts.get(index);
    }
}
