package com.ifpe.criptoscan.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifpe.criptoscan.R;
import com.ifpe.criptoscan.databinding.FragmentHomeBinding;
import com.ifpe.criptoscan.model.CriptoMoeda;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoinDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoinDetailsFragment extends Fragment {

    CriptoMoeda moeda;
    private FragmentHomeBinding binding;
    private RequestQueue queue;

    public CoinDetailsFragment() {
        // Required empty public constructor
    }
    public static CoinDetailsFragment newInstance(CriptoMoeda coin) {
        CoinDetailsFragment fragment = new CoinDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("moeda", coin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.moeda =  (CriptoMoeda) getArguments().get("moeda");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentHomeBinding.inflate(inflater, container, false);
        this.queue = Volley.newRequestQueue(getActivity());
        return binding.getRoot();
    }
}