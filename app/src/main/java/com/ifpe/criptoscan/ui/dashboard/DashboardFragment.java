package com.ifpe.criptoscan.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ifpe.criptoscan.AlertasAdapter;
import com.ifpe.criptoscan.CoinDetails;
import com.ifpe.criptoscan.FavoritosAdapter;
import com.ifpe.criptoscan.R;
import com.ifpe.criptoscan.databinding.FragmentDashboardBinding;
import com.ifpe.criptoscan.model.Alerta;
import com.ifpe.criptoscan.model.CriptoMoeda;
import com.ifpe.criptoscan.model.User;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private User user;
    private RequestQueue queue;
    private AlertasAdapter adapter;
    private ListView alertasView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        queue = Volley.newRequestQueue(getContext());
        alertasView = binding.listViewAlertas;
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
                                Alerta[] alertas = new Alerta[user.getAlertas().size()];
                                user.getAlertas().toArray(alertas);
                                adapter =new AlertasAdapter(getActivity(),
                                        R.layout.listalertas, alertas, queue);
                                alertasView.setAdapter(adapter);
                                alertasView.setOnItemClickListener((parent, view, position, id) -> {
                                    /*Intent intent = new Intent(getContext(), CoinDetails.class);
                                    intent.putExtra("classe", moedas[position]);
                                    intent.putExtra("moeda", moedas[position].getName());
                                    startActivity(intent);*/
                                    //return;
                                });
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