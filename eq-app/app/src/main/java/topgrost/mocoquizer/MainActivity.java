package topgrost.mocoquizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import topgrost.mocoquizer.browser.GameBrowserActivity;
import topgrost.mocoquizer.lobby.LobbySetupActivity;
import topgrost.mocoquizer.login.LoginActivity;
import topgrost.mocoquizer.quiz.QuizEditorActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCreateQuiz = findViewById(R.id.btnCreateQuiz);
        btnCreateQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QuizEditorActivity.class);
                startActivity(intent);
            }
        });

        Button btnHostQuiz = findViewById(R.id.btnHostQuiz);
        btnHostQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LobbySetupActivity.class);
                startActivity(intent);
            }
        });

        Button btnSearchQuiz = findViewById(R.id.btnSearchQuiz);
        btnSearchQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameBrowserActivity.class);
                startActivity(intent);
            }
        });
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
