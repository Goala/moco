package topgrost.mocoquizer.lobby;

import android.icu.util.TimeZone;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import topgrost.mocoquizer.R;
import topgrost.mocoquizer.lobby.model.Game;

public class LobbyActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference test = database.child("message").child("Q3QQo56YbC50Rfy4lOkl");

    }
}
