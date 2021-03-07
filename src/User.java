import javax.swing.*;
import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private ImageIcon image;
    private boolean isConnected;

    public User(String username, ImageIcon image) {
        this.username = username;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public ImageIcon getImage() {
        return image;
    }

    public int HaschCode() {
        return username.hashCode();
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean equals(Object obj) {
        if(obj != null && obj instanceof User) {
            return username.equals(((User)obj).getUsername());
        }
        return false;
    }
}
