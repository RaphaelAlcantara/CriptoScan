package com.ifpe.criptoscan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ifpe.criptoscan.model.CriptoMoeda;

public class FavoritosAdapter extends ArrayAdapter<CriptoMoeda> {
    static class ViewHolder {

        TextView criptoName;
        NetworkImageView imageView;
    }
    private CriptoMoeda[] moedas;
    private ImageLoader loader;

    public FavoritosAdapter(@NonNull Context context, int resource, CriptoMoeda [] moedas,
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
        CryptoAdapter.ViewHolder holder = null;
        if (moedas[position].getImageURL() != null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            listItem = inflater.inflate(R.layout.listfavoritos, null, true);
            holder = new CryptoAdapter.ViewHolder();
            holder.criptoName= listItem.findViewById(R.id.favoritos_name);
            holder.imageView = listItem.findViewById(R.id.favoritos_image);
            listItem.setTag(holder);
        } else {
            listItem = convertView;
            holder =(CryptoAdapter.ViewHolder)convertView.getTag();
        }
        holder.criptoName.setText(moedas[position].getName());
        holder.imageView.setImageUrl(moedas[position].getImageURL(), this.loader);
        return listItem;
    }
}
