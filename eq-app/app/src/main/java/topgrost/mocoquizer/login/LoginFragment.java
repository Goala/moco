package topgrost.mocoquizer.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashSet;
import java.util.Set;

import topgrost.mocoquizer.R;



public class LoginFragment extends Fragment {
    private EditText mEmailField;
    private EditText mPasswordField;
    SharedPreferences sharedPref;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,
                container, false);

        mEmailField = view.findViewById(R.id.etEmail_R);
        mPasswordField  = view.findViewById(R.id.etPassword_R);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        Set<String> emptySet = new HashSet<>();
        Set<String> emails = getStringSet("email", emptySet);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line, emails.toArray(new String[emails.size()]));

        AutoCompleteTextView actv = (AutoCompleteTextView) view.findViewById(R.id.etEmail_R);
        actv.setThreshold(1);
        actv.setAdapter(adapter);


        Button button = view.findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean valid = validateForm();
                if (valid) {
                    addStringSetElement("email",mEmailField.getText().toString());
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

    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        return sharedPref.getStringSet(key, defaultValue);
    }

    public void saveStringSet(String key, Set<String> value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public void addStringSetElement(String key, String value) {
        Set<String> set = getStringSet(key, new HashSet<String>());
        set.add(value);
        saveStringSet(key, set);
    }
}
