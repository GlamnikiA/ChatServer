import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Clients {
    private HashMap<User, Server.ClientHandler> clients;


    public Clients() {
        this.clients = new HashMap<>();
    }

    public synchronized void put(User user, Server.ClientHandler client) {
        clients.put(user,client);
    }
    public synchronized Server.ClientHandler get(User user) {
        return clients.get(user);
    }

    public synchronized boolean containsKey(User user) {
        return clients.containsKey(user);
    }
    public synchronized void remove(User user) {
        clients.remove(user);
    }

}