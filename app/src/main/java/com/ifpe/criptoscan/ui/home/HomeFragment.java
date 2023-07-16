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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifpe.criptoscan.CoinDetails;
import com.ifpe.criptoscan.CryptoAdapter;
import com.ifpe.criptoscan.NAV;
import com.ifpe.criptoscan.R;
import com.ifpe.criptoscan.api.CryptoDataListener;
import com.ifpe.criptoscan.api.ListCripto;
import com.ifpe.criptoscan.api.TopCripto;
import com.ifpe.criptoscan.databinding.FragmentHomeBinding;
import com.ifpe.criptoscan.model.CriptoMoeda;

import java.util.List;

public class HomeFragment extends Fragment implements CryptoDataListener {

    private FragmentHomeBinding binding;
    private RequestQueue queue;
    ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        listView= binding.listView;
        this.queue = Volley.newRequestQueue(getActivity());
        ListCripto listCripto = new ListCripto(getContext());
        listCripto.setCryptoDataListener(this);
        listCripto.fetchCryptoData();
        TopCripto topCripto = new TopCripto(getContext());
        topCripto.setCryptoDataListener(this);
        topCripto.fetchCryptoData();
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
        CriptoMoeda[] moedas = new CriptoMoeda[newCrypto.size()];
        newCrypto.toArray(moedas);
        listView.setAdapter(new CryptoAdapter(getActivity(),
                R.layout.listcripto, moedas, queue));
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Bundle setClass = new Bundle();
            CoinDetailsFragment coinDetailsFragment = CoinDetailsFragment.newInstance(moedas[position]);
            setClass.putSerializable("moeda", moedas[position]);
            /*NavController navController = Navigation.
                    findNavController(requireActivity(), R.id.nav_host_fragment_activity_nav);
            navController.navigate(R.id.action_navigation_home_to_navigation_coin_details);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .show(coinDetailsFragment).commit();*/
            Intent intent = new Intent(getContext(), CoinDetails.class);
            intent.putExtra("moeda", moedas[position].getName());
            startActivity(intent);
            return;
        });
    }

    @Override
    public void onCryptoChartDataReceived(List<String[]> cryptoData) {

    }


}
