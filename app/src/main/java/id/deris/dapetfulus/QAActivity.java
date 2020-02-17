package id.deris.dapetfulus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;

public class QAActivity extends AppCompatActivity {

    private ArrayList<QAModel> listData = new ArrayList<>();
    private RecyclerView rvQA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);


        final SpinKitView spinkit = findViewById(R.id.spinkit_qa);
        ImageButton back_btn = findViewById(R.id.back_btn_qa);
        rvQA = findViewById(R.id.rv_qa);
        final TextView dataEmpty = findViewById(R.id.empty_qa);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ReadQA readQA = new ReadQA(this);
        readQA.execute();

        rvQA.setVisibility(View.GONE);
        dataEmpty.setVisibility(View.GONE);
        spinkit.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());
                listData.addAll(dbHelper.getAllQA());
                spinkit.setVisibility(View.GONE);

                if (dbHelper.getAllQA().isEmpty()){
                    dataEmpty.setVisibility(View.VISIBLE);
                }
                else {
                    rvQA.setVisibility(View.VISIBLE);
                }
                rvQA.setHasFixedSize(true);
                showRecyclerList();
            }
        }, 5000);

        fullscreen();

    }

    private void showRecyclerList(){
        rvQA.setLayoutManager(new LinearLayoutManager(this));
        QAAdapter listAdapter = new QAAdapter(listData);
        rvQA.setAdapter(listAdapter);
    }

    private void fullscreen() {

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT );
    }
}
