package com.example.exemploapi;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ListaViewHolder> {
    private List<Raca> racas;
    private ItemClickListener itemClickListener;


    public ListaAdapter(List<Raca> racas) {
        this.racas = racas;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ListaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new ListaViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return (racas != null && racas.size() > 0) ? racas.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull ListaViewHolder holder, int position) {
        holder.textNome.setText(racas.get(position).getNome());
    }

    public class ListaViewHolder extends RecyclerView.ViewHolder {

        private TextView textNome;

        public ListaViewHolder(@NonNull final View itemView) {

            super(itemView);
            textNome = itemView.findViewById(R.id.nome);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(getAdapterPosition());
                }

            });
        }
    }
}


