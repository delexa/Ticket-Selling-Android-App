package com.delexa.chudobilet;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delexa.chudobilet.Adapters.ChudobiletDatabaseHelper;
import com.delexa.chudobilet.MainClasses.User;
import com.delexa.chudobilet.MainMenu.AboutFragment;
import com.delexa.chudobilet.MainMenu.AuthorizationFragment;
import com.delexa.chudobilet.MainMenu.MainFragment;
import com.delexa.chudobilet.MainMenu.MyOrdersFragment;
import com.delexa.chudobilet.MainMenu.NewsFragment;
import com.delexa.chudobilet.MainMenu.SetingsFragment;
import com.delexa.chudobilet.MainMenu.SubscriberFragment;
import com.delexa.chudobilet.MainMenu.TabFragment;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = new Intent(this, MyIntentService.class);
        intent.putExtra(MyIntentService.EXTRA_MESSAGE, "Новое событие:");
        startService(intent);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // клик по картинки настроек
        View hView = navigationView.getHeaderView(0);
        ImageView imgSettings = (ImageView) hView.findViewById(R.id.imageViewOptions);
        imgSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment fragment = new SetingsFragment();

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();

                getSupportActionBar().setTitle("Настройки профиля");

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

            }
        });

        LinearLayout imgProfile = (LinearLayout) hView.findViewById(R.id.enter);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment fragment = new AuthorizationFragment();

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();

                getSupportActionBar().setTitle("Авторизация");

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

            }
        });

        View header = navigationView.getHeaderView(0);
        TextView myName = (TextView) header.findViewById(R.id.textViewNameUP);
        TextView myEmail = (TextView) header.findViewById(R.id.textViewEmailUP);
        ImageView myPhoto = (ImageView) header.findViewById(R.id.imageViewPhoto);
        TextView enter = (TextView) header.findViewById(R.id.imageViewAuthorise);

        ChudobiletDatabaseHelper chudobiletDatabaseHelper = ChudobiletDatabaseHelper.getInstance(this);
        SQLiteDatabase db = chudobiletDatabaseHelper.getWritableDatabase();
        if (ChudobiletDatabaseHelper.isAuthorized(db)) {

            db = chudobiletDatabaseHelper.getWritableDatabase();
            User user = ChudobiletDatabaseHelper.getUser(db);

            Picasso.with(this) //передаем контекст приложения
                    .load(user.getImage())
                    .placeholder(R.drawable.no_photo)
                    .error(R.drawable.ic_menu_camera)
                    .transform(new CircleTransform())
                    .into(myPhoto); //ссылка на ImageView


            myName.setText(user.getName());
            myEmail.setText(user.getEmail());
            enter.setVisibility(View.INVISIBLE);

        } else {


            myPhoto.setImageResource(R.drawable.rounded_avatar);
            myName.setText("");
            myEmail.setText("");

        }


        Fragment fragment = new MainFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

    }

    // обработка нажатия на кнопку назад
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//
//        return true;
//    }

    // выбор из action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.today) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    // выбор из бокового меню
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        TabFragment fr = null;
        String title = getString(R.string.app_name);


        switch (id) {
            case R.id.main:
                fragment = new MainFragment();
                title = getString(R.string.main);
                break;

            case R.id.cinema:
                fragment = new TabFragment();
                fr = (TabFragment) fragment;
                title = getString(R.string.cinema);
                fr.setType1("Фильмы");
                fr.setType2("Кинотеатры");
                fr.setItem("Кино");
                fragment = fr;

                break;

            case R.id.concerts:
                title = getString(R.string.concerts);
                fragment = new TabFragment();
                fr = (TabFragment) fragment;
                fr.setType1("События");
                fr.setType2("Места");
                fr.setItem("Концерты");
                fragment = fr;

                break;

            case R.id.theaters:
                title = getString(R.string.theaters);
                fragment = new TabFragment();
                fr = (TabFragment) fragment;
                fr.setType1("Репертуар");
                fr.setType2("Места");
                fr.setItem("Театры");
                fragment = fr;

                break;

            case R.id.for_children:
                fragment = new TabFragment();
                title = getString(R.string.for_children);
                fr = (TabFragment) fragment;
                fr.setType1("События");
                fr.setType2("Места");
                fr.setItem("Детям");
                fragment = fr;

                break;

            case R.id.other:
                fragment = new TabFragment();
                title = getString(R.string.other);

                fr = (TabFragment) fragment;
                fr.setType1("События");
                fr.setType2("Места");
                fr.setItem("Другое");
                fragment = fr;

                break;

            case R.id.masterclass:
                fragment = new TabFragment();
                title = getString(R.string.masterclass);

                fr = (TabFragment) fragment;
                fr.setType1("События");
                fr.setType2("Места");
                fr.setItem("Мастер-класс");
                fragment = fr;

                break;

            case R.id.news:
                fragment = new NewsFragment();
                title = getString(R.string.news);
                break;

            case R.id.my_orders:
                fragment = new MyOrdersFragment();
                title = getString(R.string.my_orders);
                break;

            case R.id.my_subscriptions:
                fragment = new SubscriberFragment();
                title = "Мои подписки";
                break;

            case R.id.other_apps:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://search?q=pub:CHUDOBILET"));
                title = "Чудобилет";
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "Не найдено приложение GooglePlay", Toast.LENGTH_LONG).show();
                }
                break;


            case R.id.about:
                fragment = new AboutFragment();
                title = "О приложении";
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
