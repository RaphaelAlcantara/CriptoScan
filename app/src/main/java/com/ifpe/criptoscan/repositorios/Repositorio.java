package com.ifpe.criptoscan.repositorios;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ifpe.criptoscan.model.User;

import java.util.concurrent.atomic.AtomicReference;

public class Repositorio{
    private static  Repositorio mySelf;


    private FirebaseUser user;
    private Repositorio()
    {
       this.user = FirebaseAuth.getInstance().getCurrentUser();
    }
    public static Repositorio getInstance()
    {
        if(mySelf==null)
        {
            mySelf=new Repositorio();
        }
        return mySelf;
    }
    public void create(String name, String email) throws FirebaseAuthException
    {
        if(user==null)
            throw new FirebaseAuthException("1","Usuário não logado");
        User tempUser = new User(name, email);
        DatabaseReference drUsers = FirebaseDatabase.
                getInstance().getReference("users");
        drUsers.child(user.getUid()).
                setValue(tempUser);
    }
    public Task<DataSnapshot> read() throws FirebaseAuthException
    {
        AtomicReference<User> uRead = null;
        if(user==null)
            throw new FirebaseAuthException("1","Usuário não logado");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        return mDatabase.child(user.getUid()).get();
    }
    public void update(User u) throws FirebaseAuthException
    {
        if(user==null)
            throw new FirebaseAuthException("1","Usuário não logado");
        DatabaseReference drUsers = FirebaseDatabase.
                getInstance().getReference("users");
        drUsers.child(user.getUid()).
                setValue(u);
    }

}
