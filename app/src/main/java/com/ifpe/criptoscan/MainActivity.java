package com.ifpe.criptoscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ifpe.criptoscan.model.User;


public class MainActivity extends AppCompatActivity {
    private TextView edEmail;
    private TextView edPassword;

    private FirebaseAuth fbAuth;
    private FirebaseAuthListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        fbAuth = FirebaseAuth.getInstance();
//        authListener = new FirebaseAuthListener(this);
//        setContentView(R.layout.activity_main);

        edEmail = findViewById(R.id.login_email);
        edPassword = findViewById(R.id.login_senha);

        this.fbAuth = FirebaseAuth.getInstance();
        this.authListener = new FirebaseAuthListener(this);

        setContentView(R.layout.activity_main);
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
                    if(task.isSuccessful())
                    {
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
                        mDatabase.child(mAuth.getCurrentUser().getUid()).get()
                                .addOnCompleteListener(
                                        task1 -> {
                                    User user=null;
                                    String msg;
                                    if(task1.isSuccessful())
                                    {
                                        user = task1.getResult().getValue(User.class);
                                        msg="Login efetuado com sucesso!" +
                                                "Usuario: "+user.getName() ;
                                        Intent itent = new Intent(this, NAV.class);
                                        startActivity(itent);
                                    }
                                    else
                                    {
                                        msg="Login inválido!";
                                    }
                                    Toast.makeText(MainActivity.this, msg,
                                            Toast.LENGTH_SHORT).show();
                                });
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Dados Invalidos!",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }


}