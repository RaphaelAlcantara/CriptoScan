package com.ifpe.criptoscan.ui.home;

import static android.app.ProgressDialog.show;

import android.app.ActionBar;
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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ifpe.criptoscan.CryptoAdapter;
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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
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
        this.queue = Volley.newRequestQueue(getActivity());
        ListView listView = binding.listView;
        listView.setAdapter(new CryptoAdapter(getActivity(),
                R.layout.listcripto, moedas, queue));
    }


}
