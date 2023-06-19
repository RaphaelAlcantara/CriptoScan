package com.ifpe.criptoscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private TextView edEmail;
    private TextView edPassword;

    private FirebaseAuth fbAuth;
    private FirebaseAuthListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fbAuth = FirebaseAuth.getInstance();
        this.authListener = new FirebaseAuthListener(this);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fbAuth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        fbAuth.removeAuthStateListener(authListener);
    }

    public void Cadastrar(View view) {
        Intent intent = new Intent(this,CadastroActivity.class);
        startActivity(intent);
    }
    public void buttonSignInClick(View view) {
        edEmail = findViewById(R.id.login_email);
        edPassword = findViewById(R.id.login_senha);
        String login = edEmail.getText().toString();
        String passwd = edPassword.getText().toString();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(login, passwd)
                .addOnCompleteListener(this, task -> {
                    String msg;
                    if(task.isSuccessful())
                    {
                        msg="SIGN IN OK!";
                    }
                    else
                    {
                        msg="SIGN IN ERROR!";
                    }
                    Toast.makeText(MainActivity.this, msg,
                            Toast.LENGTH_SHORT).show();
                });

    }


}