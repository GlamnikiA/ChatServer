import java.util.ArrayList;
import java.util.HashMap;

public class UnsentMessages {
    private HashMap<User, ArrayList<Message>> unsent = new HashMap<>();
    // egna tillägg
    public synchronized void put(User user, Message message) {
    // hämta ArrayList – om null skapa en och placera i unsend
    // lägga till Message i ArrayList

        if (unsent.get(user) == null) {
            ArrayList<Message> list = new ArrayList<>();
            list.add(message);
            unsent.put(user, list);
        } else {
            unsent.get(user).add(message);
        }

    }
    public synchronized ArrayList<Message> get(User user) {
        ArrayList<Message> messages = unsent.getOrDefault(user, null);
        unsent.remove(user);
        return messages;
    }

    // fler synchronized-metoder som behövs

}
