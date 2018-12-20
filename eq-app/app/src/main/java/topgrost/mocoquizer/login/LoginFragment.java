package topgrost.mocoquizer.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import topgrost.mocoquizer.R;



public class LoginFragment extends Fragment {
    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,
                container, false);

        mEmailField = view.findViewById(R.id.etEmail_R);
        mPasswordField  = view.findViewById(R.id.etPassword_R);


        Button button = view.findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean valid = validateForm();
                if (valid) {
                    LoginActivity login = (LoginActivity) getActivity();
                    login.signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
                }
            }
        });
        return view;
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("E-Mail");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }


}
