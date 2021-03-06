package topgrost.mocoquizer.login.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import topgrost.mocoquizer.R;
import topgrost.mocoquizer.login.LoginActivity;
import topgrost.mocoquizer.model.User;
import topgrost.mocoquizer.quiz.QuizActivity;

public class RegisterFragment extends Fragment {

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mPasswordField2;
    private EditText mAliasField;
    private List <String> awardedUsernames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,
                container, false);

        mEmailField = view.findViewById(R.id.etEmail_R);
        mPasswordField  = view.findViewById(R.id.etPassword_R);
        mPasswordField2 = view.findViewById(R.id.etPassword2_R);
        mAliasField = view.findViewById(R.id.etAlias);

        awardedUsernames = getAwardedUsernames();
        Button button = view.findViewById(R.id.btnRegister);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean valid = validateForm();
                if (valid) {
                    LoginActivity login = (LoginActivity) getActivity();
                    login.createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString(), mAliasField.getText().toString());
                }
            }
        });
        return view;
    }


    private boolean validateForm() {
        boolean valid = true;

        String alias = mAliasField.getText().toString();
        boolean userNameAwarded = checkIfUsernameExists(alias, awardedUsernames);
        if (TextUtils.isEmpty(alias) || alias.trim().equals("")) {
            mAliasField.setError("Username erforderlich!");
            valid = false;
        }else if(alias.trim().length()< 4) {
            mAliasField.setError("Der Username muss mindestens 4 Zeichen enthalten");
            valid= false;
        }else if(userNameAwarded != false) {
            mAliasField.setError("Der Username ist bereits vergeben");
            valid= false;
        } else {
                mEmailField.setError(null);
        }


        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("E-Mail erforderlich!");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Passwort erforderlich!");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        String password_2 = mPasswordField2.getText().toString();
        if(!TextUtils.equals(password, password_2)){
            mPasswordField2.setError("Passwörter stimmen nicht überein!");
            valid = false;
        }
        return valid;
    }

    private List<String> getAwardedUsernames(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final List<String> userNames = new ArrayList<>();
        final DatabaseReference userRef = database.getReference(User.class.getSimpleName().toLowerCase() + "s");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        userNames.add(user.child("name").getValue().toString().trim());
                    }
                }catch(Exception e){
                    Toast.makeText(getContext(), "Fehler beim Verlassen der Lobby", Toast.LENGTH_LONG).show();
                    Log.d(RegisterFragment.class.getSimpleName(), e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Fehler beim Verlassen der Lobby", Toast.LENGTH_LONG).show();
                Log.d(RegisterFragment.class.getSimpleName(), databaseError.getMessage());
            }
        });
        return userNames;

    }

    private boolean checkIfUsernameExists(String userName, List<String> existingUsernames){
        if(existingUsernames.contains(userName.trim())){
            return true;
        }
        return false;
    }






}
