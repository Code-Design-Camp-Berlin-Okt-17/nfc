package nfd.nfc;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback, NfcAdapter.ReaderCallback  {
    Hash hash = new Hash();
    AES aes = new AES();
    private EditText mEditText;
    private String key = "1234";
    public boolean isSending;
    NfcAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = NfcAdapter.getDefaultAdapter(this);

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

        byte[] passEnc = aes.encrypt(pass, "Str");

        System.out.println(passEnc);
    }

    public void send(){

        //disableReaderMode();

        if (mAdapter == null) {
            mEditText.setText("Sorry this device does not have NFC.");
            return;
        }

        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
        }

        mAdapter.setNdefPushMessageCallback(this, this);
        mAdapter.setOnNdefPushCompleteCallback(this, this);

    }

    public void receive() {
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage NDEFMessage = (NdefMessage) rawMessages[0]; // only one message transferred
            String message = new String(NDEFMessage.getRecords()[0].getPayload());

            if(message.equals("accepted")) {
                Intent activityIntent = new Intent(this, confirmationActivity.class);
                startActivity(activityIntent);
            }else {
                Intent activityIntent = new Intent(this, confirmationActivity.class);
                startActivity(activityIntent);
            }

        } else {
            //isLoading
        }

    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", key.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {

        isSending=false;
        receive();

    }

    @Override
    public void onTagDiscovered(Tag tag){

        //no idea

    }
}
