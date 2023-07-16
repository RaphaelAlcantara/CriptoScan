package com.ifpe.criptoscan.ui.user;

import static android.app.ProgressDialog.show;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ifpe.criptoscan.MainActivity;
import com.ifpe.criptoscan.NAV;
import com.ifpe.criptoscan.R;
import com.ifpe.criptoscan.databinding.FragmentDashboardBinding;
import com.ifpe.criptoscan.databinding.FragmentHomeBinding;
import com.ifpe.criptoscan.databinding.FragmentProfileBinding;
import com.ifpe.criptoscan.model.User;

public class UserFragment extends Fragment {

    private FragmentProfileBinding binding;
    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = firebaseAuth.getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(mUser.getUid()).get()
                .addOnCompleteListener(
                        task1 -> {
                            String msg;
                            if(task1.isSuccessful())
                            {
                                user = task1.getResult().getValue(User.class);
                                TextView emailTextView = binding.emailCliente; // Substitua pelo ID correto do seu TextView
                                emailTextView.setText(user.getName());
                            }
                        });
        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
