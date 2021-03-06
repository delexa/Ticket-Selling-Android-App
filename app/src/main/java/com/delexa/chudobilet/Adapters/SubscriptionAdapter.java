package com.delexa.chudobilet.Adapters;


import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.delexa.chudobilet.MainClasses.Subscription;
import com.delexa.chudobilet.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.SubscriptionViewHolder> {

    private LayoutInflater inflater;
    List<Subscription> data = Collections.emptyList();
    View view;


    public SubscriptionAdapter(Context context, List<Subscription> data) {

        inflater = LayoutInflater.from(context);
        this.data = data;

    }

    @Override
    public SubscriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_item, parent, false);
        SubscriptionViewHolder holder = new SubscriptionViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(SubscriptionViewHolder holder, int position) {

        Subscription current = data.get(position);

        Picasso.with(inflater.getContext()) //передаем контекст приложения
                .load(current.getEvent().getCover())
                .placeholder(R.drawable.no_photo)
                .error(R.drawable.ic_menu_camera)
                .into(holder.cover); //ссылка на ImageView


        holder.name.setText(current.getEvent().getName());
        holder.genre.setText(current.getEvent().getGenre());
        holder.forAge.setText(current.getEvent().getForAge());
        holder.amountSeats.setText(Integer.toString(current.getAmountSeats()));

        if (current.getEvent().getYear() != 0)
            holder.year.setText(Integer.toString(current.getEvent().getYear()));


    }

    public int getItemCount() {
        return data.size();
    }


    class SubscriptionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView cover;
        TextView name;
        TextView genre;
        TextView amountSeats;
        TextView forAge;
        TextView year;

        public SubscriptionViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            cover = (ImageView) itemView.findViewById(R.id.imageViewSubscriptionCover);
            name = (TextView) itemView.findViewById(R.id.textViewSubscriptionName);
            genre = (TextView) itemView.findViewById(R.id.textViewSubscriptionGenre);
            amountSeats = (TextView) itemView.findViewById(R.id.textViewSubscriptionAmountSeats);
            forAge = (TextView) itemView.findViewById(R.id.textViewSubscriptionForAge);
            year = (TextView) itemView.findViewById(R.id.textViewSubscriptionYear);

        }


        @Override
        public void onClick(View v) {

            final int id = data.get(getAdapterPosition()).getId();

            SQLiteOpenHelper chudobiletDatabaseHelper = new ChudobiletDatabaseHelper(view.getContext());
            SQLiteDatabase db = chudobiletDatabaseHelper.getWritableDatabase();
            int amountSeats = ChudobiletDatabaseHelper.getSubscriptionById(db, id).getAmountSeats();

            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());

            alert.setTitle("Сколько мест необходимо?");
            alert.setMessage("Введите количество мест");

            // Set an EditText view to get user input
            final EditText input = new EditText(v.getContext());
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setText(Integer.toString(amountSeats));
            alert.setView(input);


            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    SQLiteOpenHelper chudobiletDatabaseHelper = ChudobiletDatabaseHelper.getInstance(view.getContext());
                    SQLiteDatabase db = chudobiletDatabaseHelper.getWritableDatabase();
                    ChudobiletDatabaseHelper.setAmountSeats(db, id, Integer.parseInt(input.getText().toString()));
                    db = chudobiletDatabaseHelper.getReadableDatabase();
                    data = ChudobiletDatabaseHelper.getSubscription(db);
                    notifyDataSetChanged();

                }
            });

            alert.setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    SQLiteOpenHelper chudobiletDatabaseHelper = ChudobiletDatabaseHelper.getInstance(view.getContext());
                    SQLiteDatabase db = chudobiletDatabaseHelper.getWritableDatabase();
                    ChudobiletDatabaseHelper.removeSubscriptionById(db, id);
                    db = chudobiletDatabaseHelper.getReadableDatabase();
                    data = ChudobiletDatabaseHelper.getSubscription(db);
                    notifyDataSetChanged();
                }
            });

            alert.show();

        }


    }

}
