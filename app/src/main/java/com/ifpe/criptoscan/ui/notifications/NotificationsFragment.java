package com.ifpe.criptoscan.ui.notifications;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.ifpe.criptoscan.NotificacaoAdapter;
import com.ifpe.criptoscan.R;
import com.ifpe.criptoscan.databinding.FragmentNotificationsBinding;
import com.ifpe.criptoscan.model.Alerta;
import com.ifpe.criptoscan.model.Notificacao;
import com.ifpe.criptoscan.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private ListView listNotificacoes;
    private User user;
    private NotificacaoAdapter adapter;
    private RequestQueue queue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        queue = Volley.newRequestQueue(getContext());
        listNotificacoes = binding.listviewNotificacoes;
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
                                List<Notificacao> notificacoes = user.getNotificacoes();
                                Notificacao[] array = new Notificacao[notificacoes.size()];
                                notificacoes.toArray(array);
                                adapter =new NotificacaoAdapter(getActivity(),
                                        R.layout.listnotificacoes, user.getNotificacoes(), queue);
                                adapter.setNotifyOnChange(true);
                                listNotificacoes.setAdapter(adapter);
                                listNotificacoes.setOnItemLongClickListener((parent, view, position, id) ->{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setMessage("Apagar Notifcação?")
                                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    String msgSaida;
                                                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                                    DatabaseReference drUsers = FirebaseDatabase.
                                                            getInstance().getReference("users");
                                                    user.getNotificacoes().remove(array[position]);
                                                    drUsers.child(mAuth.getCurrentUser().getUid()).setValue(user)
                                                            .addOnCompleteListener(task -> {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(getContext(), "Notificação Apagada",
                                                                            Toast.LENGTH_SHORT).show();
                                                                    adapter.notifyDataSetChanged();
                                                                }
                                                            });
                                                }
                                            })
                                            .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // User cancelled the dialog
                                                }
                                            });
                                    // Create the AlertDialog object and return it
                                    builder.create().show();

                                    return true;
                                });
                            }
                        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        queue.stop();
        binding = null;
    }
}