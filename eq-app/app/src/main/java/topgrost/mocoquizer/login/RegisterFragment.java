package topgrost.mocoquizer.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import topgrost.mocoquizer.R;

public class RegisterFragment extends Fragment {

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mPasswordField2;
    private EditText mAliasField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,
                container, false);

        mEmailField = view.findViewById(R.id.etEmail_R);
        mPasswordField  = view.findViewById(R.id.etPassword_R);
        mPasswordField2 = view.findViewById(R.id.etPassword2_R);
        mAliasField = view.findViewById(R.id.etAlias);


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
        if (TextUtils.isEmpty(alias)) {
            mAliasField.setError("Username erforderlich!");
            valid = false;
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
}
