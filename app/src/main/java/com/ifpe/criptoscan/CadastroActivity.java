package com.ifpe.criptoscan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ifpe.criptoscan.model.User;

public class CadastroActivity extends AppCompatActivity {

    EditText edEmail;
    EditText edPassword;
    EditText edName;
    private FirebaseAuth fbAuth;
    private FirebaseAuthListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        edEmail = findViewById(R.id.edit_email);
        edPassword = findViewById(R.id.edit_password);
        edName = findViewById(R.id.edit_name);
        this.fbAuth = FirebaseAuth.getInstance();
        this.authListener = new FirebaseAuthListener(this);
    }

    public void buttonSignUpClick(View view)
    {
        String name = edName.getText().toString();
        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    String msg = task.isSuccessful() ? "SIGN UP OK!":
                            "SIGN UP ERROR!";
                    Toast.makeText(CadastroActivity.this, msg,
                            Toast.LENGTH_SHORT).show();
                    if (task.isSuccessful()) {
                        User tempUser = new User(name, email);
                        DatabaseReference drUsers = FirebaseDatabase.
                                getInstance().getReference("users");
                        drUsers.child(mAuth.getCurrentUser().getUid()).
                                setValue(tempUser);
                    }
                });


    }
    @Override
    public void onStart() {
        super.onStart();
        fbAuth.addAuthStateListener(authListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        fbAuth.removeAuthStateListener(authListener);
    }

}