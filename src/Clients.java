import java.util.HashMap;

public class Clients {
    private HashMap<User, Server.ClientHandler> clients; // = ...
    // egna tillägg
    public synchronized void put(User user, Server.ClientHandler client) {
        clients.put(user,client);
    }
    public synchronized Server.ClientHandler get(User user) {
        return get(user);
    }

    public synchronized boolean containsKey(User user) {
        return clients.containsKey(user);
    }

// fler synchronized-metoder som behövs
}