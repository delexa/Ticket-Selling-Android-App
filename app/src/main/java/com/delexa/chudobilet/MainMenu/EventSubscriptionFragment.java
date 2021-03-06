package com.delexa.chudobilet.MainMenu;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delexa.chudobilet.Adapters.ChudobiletDatabaseHelper;
import com.delexa.chudobilet.Adapters.SubscriptionAdapter;
import com.delexa.chudobilet.MainClasses.Subscription;
import com.delexa.chudobilet.R;

import java.util.List;

public class EventSubscriptionFragment extends Fragment {


    private RecyclerView recyclerView;
    private SubscriptionAdapter subscriptionAdapter;

    public EventSubscriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_my_orders, container, false);



        recyclerView = (RecyclerView) v.findViewById(R.id.OrderList);

        subscriptionAdapter = new SubscriptionAdapter(getActivity(), getSubscription());
        recyclerView.setAdapter(subscriptionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return v;
    }

    public List<Subscription> getSubscription() {

        SQLiteOpenHelper chudobiletDatabaseHelper = ChudobiletDatabaseHelper.getInstance(getContext());
        SQLiteDatabase db = chudobiletDatabaseHelper.getReadableDatabase();
        List<Subscription> data = ChudobiletDatabaseHelper.getSubscription(db);

        return data;
    }

}
