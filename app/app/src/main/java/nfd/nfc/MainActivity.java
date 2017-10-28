package nfd.nfc;

import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;

public class MainActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {
    Hash hash = new Hash();
    AES aes = new AES();
    String[] unlockingDeviceKeys;

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
                    String hashedPass = hash.hashPassword(pass);

                    onEnter(pass, hashedPass);
                    if (hashedPass == "false") {
                        // There was a error hashing the password
                    }
                }
                return false;
            }
        });
    }

    private void onEnter(String pass, String hashedPass) {
        System.out.println(pass + " - " + hashedPass);
    }

    public void receiveResult() {
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage NDEFMessage = (NdefMessage) rawMessages[0]; // only one message transferred
            String message = new String(NDEFMessage.getRecords()[0].getPayload());

            // On receive
            File f = new File("config.json");
            if (f.exists()) {
                if(message.length()==1) {
                    // Fehler an Sender senden: Schloss wurde schon einmal eingerichtet!
                } else {
                    //prüfen, ob key in gesamt key vorhanden
                }
            } else {
                newLock(Integer.parseInt(message));
            }
        }
    }

    public void newLock(int user){
        String key = "";
        for(int i=0; i<key.length(); i++) {
            key = key + genCode(user);
        }
        try {
            // key in datei schreiben
        } catch (IOException e) {
            System.out.println(e);
            Context context = getApplicationContext();
            CharSequence text = "File saving error...";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public String genCode(int user){
        String out = "";
        for(int i=0; i<16; i++){
            int r = (int)Math.random()*10;
            out = out + r;
        }
        return out;
    }
}
