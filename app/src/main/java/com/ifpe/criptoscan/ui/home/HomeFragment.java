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
    public void onCryptoDataReceived(String coinName1,
                                     String coinFullName1,
                                     String price1,
                                     String variant1,
                                     String MKTCAP1,
                                     String coinName2,
                                     String coinFullName2,
                                     String price2,
                                     String variant2,
                                     String MKTCAP2,
                                     String coinName3,
                                     String coinFullName3,
                                     String price3,
                                     String variant3,
                                     String MKTCAP3,
                                     String coinName4,
                                     String coinFullName4,
                                     String price4,
                                     String variant4,
                                     String MKTCAP4,
                                     String coinName5,
                                     String coinFullName5,
                                     String price5,
                                     String variant5,
                                     String MKTCAP5)
    {


        this.binding.coinName1.setText(coinName1);
        this.binding.price1.setText(price1);

        this.binding.variant1.setText(variant1 + "%");
        if (variant1.contains("-")) {
            this.binding.variant1.setTextColor(Color.parseColor("#FF0000"));
        } else {
            this.binding.variant1.setTextColor(Color.parseColor("#00FF00"));
        }

        this.binding.MKTCAP1.setText(MKTCAP1);


        this.binding.coinName2.setText(coinName2);
        this.binding.price2.setText(price2);

        this.binding.variant2.setText(variant2 + "%");
        if (variant2.contains("-")) {
            this.binding.variant2.setTextColor(Color.parseColor("#FF0000"));
        } else {
            this.binding.variant2.setTextColor(Color.parseColor("#00FF00"));
        }

        this.binding.MKTCAP2.setText(MKTCAP2);

        this.binding.coinName3.setText(coinName3);
        this.binding.price3.setText(price3);

        this.binding.variant3.setText(variant3 + "%");
        if (variant3.contains("-")) {
            this.binding.variant3.setTextColor(Color.parseColor("#FF0000"));
        } else {
            this.binding.variant3.setTextColor(Color.parseColor("#00FF00"));
        }

        this.binding.MKTCAP3.setText(MKTCAP3);


        this.binding.coinName4.setText(coinName4);
        this.binding.price4.setText(price4);

        this.binding.variant4.setText(variant4 + "%");
        if (variant4.contains("-")) {
            this.binding.variant4.setTextColor(Color.parseColor("#FF0000"));
        } else {
            this.binding.variant4.setTextColor(Color.parseColor("#00FF00"));
        }

        this.binding.MKTCAP4.setText(MKTCAP4);


        this.binding.coinName5.setText(coinName5);
        this.binding.price5.setText(price5);

        this.binding.variant5.setText(variant5 + "%");
        if (variant5.contains("-")) {
            this.binding.variant5.setTextColor(Color.parseColor("#FF0000"));
        } else {
            this.binding.variant5.setTextColor(Color.parseColor("#00FF00"));
        }

        this.binding.MKTCAP5.setText(MKTCAP5);



    }


}
