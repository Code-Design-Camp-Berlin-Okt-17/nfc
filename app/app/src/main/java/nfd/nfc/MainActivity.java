package nfd.nfc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Hash hash = new Hash();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText passInput = findViewById(R.id.passInput);
        passInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    EditText passInput = findViewById(R.id.passInput);
                    String pass = passInput.getText().toString();

                    Log.i("Unhashed Password", passInput.getText().toString());

                    String hashedPass = hash.hashPassword(pass);
                    if (hashedPass == "false") {
                        // There was a error hashing the password
                    }
                }
                return false;
            }
        });
    }
}
