import java.util.ArrayList;
import java.util.HashMap;

public class UnsentMessages {
    private HashMap<String, ArrayList<Message>> unsent = new HashMap<>();
    // egna tillägg
    public synchronized void put(String username, Message message) {
    // hämta ArrayList – om null skapa en och placera i unsend
    // lägga till Message i ArrayList

        if (unsent.get(username) == null) {
            ArrayList<Message> list = new ArrayList<>();
            list.add(message);
            unsent.put(username, list);
        }
    }
    public synchronized ArrayList<Message> get(String username) {
        ArrayList<Message> messages = unsent.getOrDefault(username, null);
        unsent.remove(username);
        return messages;
    }

    // fler synchronized-metoder som behövs

}
