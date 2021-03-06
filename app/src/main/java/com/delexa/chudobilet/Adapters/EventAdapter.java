package com.delexa.chudobilet.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.delexa.chudobilet.MainClasses.Event;
import com.delexa.chudobilet.R;
import com.delexa.chudobilet.SubMenu.EventActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private LayoutInflater inflater;
    List<Event> data = new ArrayList<>();


    public EventAdapter(Context context, List<Event> data) {

        inflater = LayoutInflater.from(context);
        this.data.addAll(data);

    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        EventViewHolder holder = new EventViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {

        Event current = data.get(position);


        Picasso.with(inflater.getContext()) //передаем контекст приложения
                .load(current.getCover())
                .placeholder(R.drawable.no_photo)
                .error(R.drawable.ic_menu_camera)
                .into(holder.cover); //ссылка на ImageView


        holder.name.setText(current.getName());

        holder.genre.setText(current.getGenre());
        holder.time.setText(current.getAmountTime());
        holder.country.setText(current.getCountry());
        holder.forAge.setText(current.getForAge());

        if (current.getYear() != 0)
            holder.year.setText(Integer.toString(current.getYear()));

        if (current.getCountry() == null) holder.comma.setText(null);


    }

    public int getItemCount() {
        return data.size();
    }

    //поиск

    public void animateTo(List<Event> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Event> newModels) {
        for (int i = data.size() - 1; i >= 0; i--) {
            final Event model = data.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Event> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Event model = newModels.get(i);
            if (!data.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Event> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Event model = newModels.get(toPosition);
            final int fromPosition = data.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Event removeItem(int position) {
        final Event model = data.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Event model) {
        data.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Event model = data.remove(fromPosition);
        data.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }


//поиск

    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView cover;
        TextView name;
        TextView genre;
        TextView time;
        TextView country;
        TextView forAge;
        TextView year;
        TextView comma;


        public EventViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            cover = (ImageView) itemView.findViewById(R.id.imageViewEventCover);
            name = (TextView) itemView.findViewById(R.id.textViewEventName);
            genre = (TextView) itemView.findViewById(R.id.textViewEventGenre);
            time = (TextView) itemView.findViewById(R.id.textViewEventAmountTime);
            country = (TextView) itemView.findViewById(R.id.textViewEventCountry);
            forAge = (TextView) itemView.findViewById(R.id.textViewEventForAge);
            year = (TextView) itemView.findViewById(R.id.textViewEventYear);
            comma = (TextView) itemView.findViewById(R.id.textViewEventComma);
        }


        @Override
        public void onClick(View v) {

            int id = data.get(getAdapterPosition()).getId();

            Activity activity = (Activity) v.getContext();
            Intent intent = new Intent(activity, EventActivity.class);

            intent.putExtra("_id", Integer.valueOf(id));
            v.getContext().startActivity(intent);

        }


    }

}
