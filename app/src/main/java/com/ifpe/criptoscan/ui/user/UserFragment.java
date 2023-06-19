package com.ifpe.criptoscan.ui.user;

import static android.app.ProgressDialog.show;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ifpe.criptoscan.R;
import com.ifpe.criptoscan.databinding.FragmentDashboardBinding;
import com.ifpe.criptoscan.databinding.FragmentHomeBinding;
import com.ifpe.criptoscan.databinding.FragmentProfileBinding;

public class UserFragment extends Fragment {

    private FragmentProfileBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();
            TextView emailTextView = binding.emailCliente; // Substitua pelo ID correto do seu TextView
            emailTextView.setText(userEmail);
        }


        return binding.getRoot();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
