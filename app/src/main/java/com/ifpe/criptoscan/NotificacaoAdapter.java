package com.ifpe.criptoscan;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ifpe.criptoscan.model.Alerta;
import com.ifpe.criptoscan.model.CriptoMoeda;
import com.ifpe.criptoscan.model.Notificacao;

import java.util.List;

public class NotificacaoAdapter extends ArrayAdapter<Notificacao> {
    static class ViewHolder {
        TextView valor;
        TextView criptoName;
        NetworkImageView imageView;
        ImageView ico;
        TextView mensagem;
    }
    private ImageLoader loader;

    public NotificacaoAdapter(@NonNull Context context, int resource, List<Notificacao> notificacaos,
                          RequestQueue queue) {
        super(context, resource, notificacaos);
        this.loader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<>(10);
            public void putBitmap(String url, Bitmap bitmap) { mCache.put(url, bitmap); }
            public Bitmap getBitmap(String url) { return mCache.get(url); }
        });
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem=null;
        ViewHolder holder = null;
        Notificacao notificacoes = getItem(position);
        if (notificacoes.getAlerta().getCoin().getImageURL() != null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            listItem = inflater.inflate(R.layout.listnotificacoes, null, true);
            holder = new ViewHolder();
            holder.mensagem = listItem.findViewById(R.id.notificacao_mensagem);
            holder.criptoName = listItem.findViewById(R.id.alertas_name);
            holder.imageView = listItem.findViewById(R.id.alertas_image);
            holder.valor = listItem.findViewById(R.id.alertas_preço);
            holder.ico = listItem.findViewById(R.id.alerta_icone);
            listItem.setTag(holder);
        } else {
            listItem = convertView;
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mensagem.setText(notificacoes.getMensagem());
        holder.criptoName.setText(notificacoes.getAlerta().getCoin().getName());
        holder.imageView.setImageUrl(notificacoes.getAlerta().getCoin().getImageURL(), this.loader);
        holder.valor.setText("R$ "+String.valueOf(notificacoes.getAlerta().getValor()));
        Resources res = getContext().getResources();
        Drawable drawable = null;
        if(notificacoes.getAlerta().isDownPrices()) {
            drawable= res.getDrawable(R.drawable.seta_descendo, getContext().getTheme());
        }
        else {
            drawable = res.getDrawable(R.drawable.seta_subindo, getContext().getTheme());
        }
        holder.ico.setImageDrawable(drawable);
        return listItem;
    }
}
