package com.example.frag;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AdapterDepozite extends  RecyclerView.Adapter<MyViewHolder>{

    Context context;
    ArrayList<Depozit_name> list;
    Dialog tran;

    public Context getContext() {
        return context;
    }

    public AdapterDepozite(Context context, ArrayList<Depozit_name> datalist) {
        this.context = context;
        this.list = datalist;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.depozit_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Depozit_name depo=list.get(position);
        String idDepo= list.get(position).getId();
        String nr=list.get(position).getNum();
        holder.titlu.setText(depo.getNume());
        String nume=depo.getNume();
        int sum=depo.getSold();
        int per=depo.getPerioada();
        int rata=depo.getRatad();
        String data= depo.getData();




        holder.depoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tran=new Dialog(context);

                tran.setContentView(R.layout.depozit_detail);
                tran.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                tran.show();
                View dialogView = tran.findViewById(android.R.id.content);
                TextView num=dialogView.findViewById(R.id.textView3);
                TextView suma=dialogView.findViewById(R.id.textView24);
                TextView ratad=dialogView.findViewById(R.id.textView25);
                TextView timpramas=dialogView.findViewById(R.id.textView26);
                ImageView bin=dialogView.findViewById(R.id.stergere);
                num.setText(nume);
                suma.setText(Integer.toString(sum));
                ratad.setText(Integer.toString(rata));


                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String acum = dateFormat.format(calendar.getTime());

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    LocalDate date1 = LocalDate.parse(data, formatter);
                    LocalDate date2 = LocalDate.parse(acum, formatter);
                    long nrRam= ChronoUnit.DAYS.between(date1,date2);
                    int numar2 = (int) nrRam;
                    timpramas.setText(Integer.toString(per-numar2));

                }
                else{
                    timpramas.setText(Integer.toString(210));
                }

                bin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("users").child(nr).child("depozite").child(idDepo);
                        reference2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Ștergerea a fost realizată cu succes
                                    Toast.makeText(getContext(), "Finalizare operatiune cu succes", Toast.LENGTH_SHORT).show();
                                    DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("users").child(nr);
                                    reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int cont=snapshot.child("sold").getValue(Integer.class);
                                            reference3.child("sold").setValue(cont+sum);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    tran.dismiss();
                                } else {
                                    // A apărut o eroare la ștergerea elementului
                                    Toast.makeText(getContext(), "A parut o eroare", Toast.LENGTH_SHORT).show();
                                    tran.dismiss();
                                }
                            }
                        });
                    }
                });



            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    TextView titlu;
    CardView depoCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        titlu= itemView.findViewById(R.id.textView20);
        depoCard=itemView.findViewById(R.id.depozitCard);
    }
}