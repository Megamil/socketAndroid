package net.megamil.chatio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class ChatActivity extends AppCompatActivity {

    EditText edtChat,edtChatList;
    TextView textViewChatTitle;
    String name,server;

    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        edtChat             = findViewById(R.id.editTextChat);
        edtChatList         = findViewById(R.id.editTextChatList);
        textViewChatTitle   = findViewById(R.id.textViewChatTitle);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            name    = extras.getString("name");
            server  = extras.getString("server");
        }

        try {
            mSocket = IO.socket(server);
        } catch (URISyntaxException e) {finish();}

        mSocket.emit("name", name);

        //listen
        mSocket.on("chatResponse",chatResponse);
        mSocket.on("users",users);

        edtChat.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if(!edtChat.getText().toString().isEmpty())
                        mSocket.emit("chat", edtChat.getText());
                    edtChat.setText("");
                }
                return false;
            }
        });

        mSocket.connect();

    }

    private Emitter.Listener chatResponse = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = (String) args[0];
                    edtChatList.setText(edtChatList.getText()+"\n"+stripHtml(data));
                }
            });
            System.out.println("Call chatResponse");
        }

    };

    private Emitter.Listener users = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Integer data = (Integer) args[0];
                    textViewChatTitle.setText(data+" Usuário(s) Online");
                }
            });
            System.out.println("Call chatResponse");
        }

    };

    public Spanned stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }

}
