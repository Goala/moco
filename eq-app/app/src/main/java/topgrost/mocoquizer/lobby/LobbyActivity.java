package topgrost.mocoquizer.lobby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import topgrost.mocoquizer.R;

public class LobbyActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference test = database.child("message").child("Q3QQo56YbC50Rfy4lOkl");

    }
}
