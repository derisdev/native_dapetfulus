package id.deris.dapetfulus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class NotifActivity extends AppCompatActivity {

    private ArrayList<NotifModel> listData = new ArrayList<>();
    private RecyclerView rvNotif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);



        ImageButton back_btn = findViewById(R.id.back_btn_notif);
        rvNotif = findViewById(R.id.rv_notif);
        final SwipeRefreshLayout swipeRv = findViewById(R.id.swipeContainer_notif_rv);
        final SwipeRefreshLayout swipeEmpty = findViewById(R.id.swipeContainer_notif_empty);
        final TextView emptyText = findViewById(R.id.empty_notif);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




        DBHelper dbHelper = DBHelper.getInstance(this);
        listData.addAll(dbHelper.getAllNotif());
        rvNotif.setHasFixedSize(true);
        showRecyclerList();

        swipeRv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                ReadNotif readNotif = new ReadNotif(getApplicationContext());
                readNotif.execute();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());
                        listData.addAll(dbHelper.getAllNotif());
                        NotifAdapter notifAdapter = new NotifAdapter(listData);
                        notifAdapter.clear();
                        notifAdapter.addAll(listData);

                        if(dbHelper.getAllNotif().isEmpty()){
                            rvNotif.setVisibility(View.GONE);
                            emptyText.setVisibility(View.VISIBLE);
                        }
                        else {
                            emptyText.setVisibility(View.GONE);
                            rvNotif.setVisibility(View.VISIBLE);
                        }

                        swipeRv.setRefreshing(false);
                    }
                }, 5000);
            }
        });
        swipeEmpty.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                ReadNotif readNotif = new ReadNotif(getApplicationContext());
                readNotif.execute();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());
                        listData.addAll(dbHelper.getAllNotif());
                        NotifAdapter notifAdapter = new NotifAdapter(listData);
                        notifAdapter.clear();
                        notifAdapter.addAll(listData);

                        if(dbHelper.getAllNotif().isEmpty()){
                            rvNotif.setVisibility(View.GONE);
                            emptyText.setVisibility(View.VISIBLE);
                        }
                        else {
                            emptyText.setVisibility(View.GONE);
                            rvNotif.setVisibility(View.VISIBLE);
                        }

                        swipeEmpty.setRefreshing(false);
                    }
                }, 5000);
            }
        });
        swipeRv.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeEmpty.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        fullscreen();
    }

    private void showRecyclerList(){
        rvNotif.setLayoutManager(new LinearLayoutManager(this));
        NotifAdapter listAdapter = new NotifAdapter(listData);
        rvNotif.setAdapter(listAdapter);
    }

    private void fullscreen() {

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT );
    }
}
