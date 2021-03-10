import java.util.ArrayList;
import java.util.HashMap;

/*
* Lagrar meddelanden i en Hashmap som inte kan levereras pga mottagare inte är online.
*/
public class UnsentMessages {
    private HashMap<String, ArrayList<Message>> unsent = new HashMap<>();

    /*
    * Lägger till användarnamn och meddelande i hashmapen
    */
    public synchronized void put(String username, Message message) {
        if (unsent.get(username) == null) {
            ArrayList<Message> list = new ArrayList<>();
            list.add(message);
            unsent.put(username, list);
        }
    }

    /*
    * Hämtar ArrayList med meddelanden som väntar på att bli levererade till given användare
    * Inparameter String
    */
    public synchronized ArrayList<Message> get(String username) {
        ArrayList<Message> messages = unsent.getOrDefault(username, null);
        unsent.remove(username);
        return messages;
    }
}
