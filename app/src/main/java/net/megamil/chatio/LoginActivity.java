package net.megamil.chatio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    EditText edtLoginName, edtLoginServer;
    Button btnLoginNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLoginName    = findViewById(R.id.editTextLoginName);
        edtLoginServer  = findViewById(R.id.editTextLoginServer);
        btnLoginNext    = findViewById(R.id.buttonLoginNext);

        btnLoginNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] array = getResources().getStringArray(R.array.users);
                String name = array[new Random().nextInt(array.length)];
                String server = "http://192.168.1.111:3000";

                if(!edtLoginName.getText().toString().isEmpty()){
                    name = edtLoginName.getText().toString();
                }

                if(!edtLoginServer.getText().toString().isEmpty()){
                    server = edtLoginServer.getText().toString();
                }

                Intent chat = new Intent(LoginActivity.this, ChatActivity.class);
                chat.putExtra("name", "<name>"+name+"</name>");
                chat.putExtra("server", server);

                startActivity(chat);
                finish();


            }
        });

    }



}

