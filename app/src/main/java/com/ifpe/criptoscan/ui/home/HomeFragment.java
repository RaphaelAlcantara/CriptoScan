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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ifpe.criptoscan.api.CryptoDataListener;
import com.ifpe.criptoscan.api.TopCripto;
import com.ifpe.criptoscan.databinding.FragmentHomeBinding;
import com.ifpe.criptoscan.model.CriptoMoeda;

import java.util.List;

public class HomeFragment extends Fragment implements CryptoDataListener {

    private FragmentHomeBinding binding;

    private TextView coinName1, coinName2, coinName3, coinName4, coinName5;

    private TextView coinFullName1, coinFullName2, coinFullName3, coinFullName4, coinFullName5;
    private TextView price1, price2, price3, price4, price5;

    private TextView variant1, variant2, variant3, variant4, variant5;

    private TextView MKTCAP1, MKTCAP2, MKTCAP3, MKTCAP4, MKTCAP5;




    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);


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
    public void onCryptoDataReceived(List<CriptoMoeda> crypto)
    {
        TableLayout tableLayout = this.binding.tableCriptoPopulares;
        for(CriptoMoeda c : crypto)
        {
            TableRow row = new TableRow(getActivity());
            TextView coinName = new TextView(getActivity());
            coinName.setText(c.getName());
            TextView price = new TextView(getActivity());
            price.setText(c.getPrice());
            TextView variant = new TextView(getActivity());
            if (c.getVariant().contains("-")) {
                variant.setTextColor(Color.parseColor("#FF0000"));
            } else {
                variant.setTextColor(Color.parseColor("#00FF00"));
            }
            variant.setText(c.getVariant());
            TextView MKTCAP = new TextView(getActivity());
            MKTCAP.setText(c.getMKTCAP());
            row.addView(coinName);
            row.addView(price);
            row.addView(variant);
            row.addView(MKTCAP);
            tableLayout.addView(row);
        }
    }


}
