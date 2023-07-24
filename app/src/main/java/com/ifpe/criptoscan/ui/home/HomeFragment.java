package com.ifpe.criptoscan.ui.home;

import static android.app.ProgressDialog.show;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ifpe.criptoscan.CoinDetails;
import com.ifpe.criptoscan.CryptoAdapter;
import com.ifpe.criptoscan.FavoritosAdapter;
import com.ifpe.criptoscan.NAV;
import com.ifpe.criptoscan.R;
import com.ifpe.criptoscan.api.CryptoDataListener;
import com.ifpe.criptoscan.api.ListCripto;
import com.ifpe.criptoscan.api.TopCripto;
import com.ifpe.criptoscan.databinding.FragmentHomeBinding;
import com.ifpe.criptoscan.model.CriptoMoeda;
import com.ifpe.criptoscan.model.Notificacao;
import com.ifpe.criptoscan.model.User;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements CryptoDataListener {

    private FragmentHomeBinding binding;
    private RequestQueue queue;
    ListView listView;
    private User user;
    private ListView favoritesView;
    private RecyclerView recyclerView;
    private FavoritosAdapter adapter;
    private List<CriptoMoeda> listMoedas;
    private SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        searchView = binding.searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
        favoritesView = binding.listViewFavoritos;
        listView= binding.listView;
        this.queue = Volley.newRequestQueue(getActivity());
        ListCripto listCripto = new ListCripto(getContext());
        listCripto.setCryptoDataListener(this);
        listCripto.fetchCryptoData();
        TopCripto topCripto = new TopCripto(getContext());
        topCripto.setCryptoDataListener(this);
        topCripto.fetchCryptoData();
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
                                if(user.getFavoritos()!=null) {
                                    CriptoMoeda[] moedas = new CriptoMoeda[user.getFavoritos().size()];
                                    user.getFavoritos().toArray(moedas);
                                    adapter = new FavoritosAdapter(getActivity(),
                                            R.layout.listfavoritos, moedas, queue);
                                    favoritesView.setAdapter(adapter);
                                    favoritesView.setOnItemClickListener((parent, view, position, id) -> {
                                        Intent intent = new Intent(getContext(), CoinDetails.class);
                                        intent.putExtra("classe", moedas[position]);
                                        intent.putExtra("moeda", moedas[position].getName());
                                        startActivity(intent);
                                        return;
                                    });
                                }
                            }
                        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Volley.newRequestQueue(getContext()).stop();
        binding = null;
    }

    @Override
    public void onCryptoTopDataReceived(List<CriptoMoeda> crypto)
    {
        TableLayout tableLayout = this.binding.tableCriptoPopulares;
        for(CriptoMoeda c : crypto)
        {
            TableRow row = new TableRow(getActivity());
            TextView coinName = new TextView(getActivity());
            coinName.setText(c.getName());
            TextView price = new TextView(getActivity());
            price.setText(c.getPrice());
            price.setPadding(20,0,0,0);
            TextView variant = new TextView(getActivity());
            if (c.getVariant().contains("-")) {
                variant.setTextColor(Color.parseColor("#FF0000"));
            } else {
                variant.setTextColor(Color.parseColor("#00FF00"));
            }
            variant.setText(c.getVariant());
            variant.setPadding(20,0,0,0);
            TextView MKTCAP = new TextView(getActivity());
            MKTCAP.setText(c.getMKTCAP());
            MKTCAP.setPadding(20,0,0,0);
            row.addView(coinName);
            row.addView(price);
            row.addView(variant);
            row.addView(MKTCAP);
            tableLayout.addView(row);
        }
    }

    @Override
    public void onCryptoListDataReceived(List<CriptoMoeda> newCrypto) {
        this.listMoedas = newCrypto;
        CriptoMoeda[] moedas = new CriptoMoeda[newCrypto.size()];
        newCrypto.toArray(moedas);
        listView.setAdapter(new CryptoAdapter(getActivity(),
                R.layout.listcripto, moedas, queue));
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getContext(), CoinDetails.class);
            intent.putExtra("classe", moedas[position]);
            intent.putExtra("moeda", moedas[position].getName());
            startActivity(intent);
            return;
        });
    }
    private void filter(String query) {
        List<CriptoMoeda> filteredList = new ArrayList<>();
        if (listMoedas != null) {
            for (CriptoMoeda item : listMoedas) {
                if (item.getName().contains(query.toUpperCase()) ||
                        item.getFullName().contains(query.toUpperCase())) {
                    filteredList.add(item);
                }
            }
            CriptoMoeda[] moedas = new CriptoMoeda[filteredList.size()];
            filteredList.toArray(moedas);
            listView.setAdapter(new CryptoAdapter(getActivity(),
                    R.layout.listcripto, moedas, queue));
            listView.setOnItemClickListener((parent, view, position, id) -> {
                Intent intent = new Intent(getContext(), CoinDetails.class);
                intent.putExtra("classe", moedas[position]);
                intent.putExtra("moeda", moedas[position].getName());
                startActivity(intent);
                return;
            });
        }
    }

    @Override
    public void onCryptoChartDataReceived(List<String[]> cryptoData) {

    }

    @Override
    public void onCryptoOneSearch(Notificacao notificacao) {

    }


    @Override
    public void onResume() {
        super.onResume();
        if(user!=null)
        {
            if(adapter!=null)
            {
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
                                        if(user.getFavoritos()!=null) {
                                            CriptoMoeda[] moedas = new CriptoMoeda[user.getFavoritos().size()];
                                            user.getFavoritos().toArray(moedas);
                                            adapter = new FavoritosAdapter(getActivity(),
                                                    R.layout.listfavoritos, moedas, queue);
                                            favoritesView.setAdapter(adapter);
                                            favoritesView.setOnItemClickListener((parent, view, position, id) -> {
                                                Intent intent = new Intent(getContext(), CoinDetails.class);
                                                intent.putExtra("classe", moedas[position]);
                                                intent.putExtra("moeda", moedas[position].getName());
                                                startActivity(intent);
                                                return;
                                            });
                                        }
                                    }
                                });
            }
        }
    }
}
