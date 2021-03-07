import java.util.ArrayList;

public class Contacts {
    private ArrayList<User> onlineContacts;
    private ArrayList<User> savedContacts;

    public Contacts() {
        onlineContacts = new ArrayList<>();
        savedContacts = new ArrayList<>();
    }
    public void setOnlineUser(User user) {
        if(!onlineContacts.contains(user)) {
            onlineContacts.add(user);
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
    public ArrayList<User> getOnlineContacts() {
        return onlineContacts;
    }
    public User getContactAt(int index) {
        return onlineContacts.get(index);
    }

}
