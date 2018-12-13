package topgrost.mocoquizer.lobby.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Game {

    public String name;

    public Game() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Game(String name) {
        this.name = name;
    }
}
