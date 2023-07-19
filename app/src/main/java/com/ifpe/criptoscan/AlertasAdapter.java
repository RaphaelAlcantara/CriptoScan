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

public class AlertasAdapter extends ArrayAdapter<Alerta> {
    static class ViewHolder {
        TextView valor;
        TextView criptoName;
        NetworkImageView imageView;
        ImageView ico;
    }
    private Alerta[] moedas;
    private ImageLoader loader;

    public AlertasAdapter(@NonNull Context context, int resource, Alerta[] moedas,
                          RequestQueue queue) {
        super(context, resource, moedas);
        this.moedas=moedas;
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
        if (moedas[position].getCoin().getImageURL() != null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            listItem = inflater.inflate(R.layout.listalertas, null, true);
            holder = new ViewHolder();
            holder.criptoName = listItem.findViewById(R.id.alertas_name);
            holder.imageView = listItem.findViewById(R.id.alertas_image);
            holder.valor = listItem.findViewById(R.id.alertas_preço);
            holder.ico = listItem.findViewById(R.id.alerta_icone);
            listItem.setTag(holder);
        } else {
            listItem = convertView;
            holder = (ViewHolder) convertView.getTag();
        }
        holder.criptoName.setText(moedas[position].getCoin().getName());
        holder.imageView.setImageUrl(moedas[position].getCoin().getImageURL(), this.loader);
        holder.valor.setText("R$ "+String.valueOf(moedas[position].getValor()));
        Resources res = getContext().getResources();
        Drawable drawable = null;
        if(moedas[position].isDownPrices()) {
           drawable= res.getDrawable(R.drawable.seta_descendo, getContext().getTheme());
        }
        else {
            drawable = res.getDrawable(R.drawable.seta_subindo, getContext().getTheme());
        }
        holder.ico.setImageDrawable(drawable);
        return listItem;
    }
}
