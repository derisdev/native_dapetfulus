package id.deris.dapetfulus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

public class NotifActivity extends AppCompatActivity {

    private ArrayList<NotifModel> listData = new ArrayList<>();
    private RecyclerView rvNotif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);



        ImageButton back_btn = findViewById(R.id.back_btn_notif);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        rvNotif = findViewById(R.id.rv_notif);
        rvNotif.setHasFixedSize(true);

        listData.addAll(InputNotifSementara.getListData());
        showRecyclerList();


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
