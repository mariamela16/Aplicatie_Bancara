package com.example.frag;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterTranzactii extends RecyclerView.Adapter<AdapterTranzactii.MyViewHolder> {
    Context context;
    ArrayList<Tranzactii> list;

    public AdapterTranzactii(Context context, ArrayList<Tranzactii> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.recycleitem,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Tranzactii tranzactii=list.get(position);
        Integer semn=Integer.parseInt(tranzactii.getSold());
        if(semn>0){
            holder.recLang.setText("+ "+tranzactii.getSold());
        }else{
            holder.recLang.setTextColor(Color.parseColor("#FF0000"));
            holder.recLang.setText(tranzactii.getSold());
        }
        holder.recTitle.setText(tranzactii.getNume());
        holder.recDesc.setText(tranzactii.getDescriere());
        holder.recData.setText(tranzactii.getData());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  void SearchDataList(ArrayList<Tranzactii> searchList){
        list=searchList;
        notifyDataSetChanged();

    }


    public  void sortAfterSuma(ArrayList<Tranzactii> crescatorList){
        list=crescatorList;
        notifyDataSetChanged();

    }

    public  void sortAfterVenituri(ArrayList<Tranzactii> venituriList){
        list=venituriList;
        notifyDataSetChanged();

    }

    public  void sortAfterFacturi(ArrayList<Tranzactii> facturiList){
        list=facturiList;
        notifyDataSetChanged();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView recTitle,recLang,recDesc, recData;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recTitle=itemView.findViewById(R.id.recTitle);
            recLang=itemView.findViewById(R.id.recLang);
            recDesc=itemView.findViewById(R.id.recDesc);
            recData=itemView.findViewById(R.id.recData);
        }
    }
}
