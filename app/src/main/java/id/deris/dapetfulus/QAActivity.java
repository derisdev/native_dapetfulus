package id.deris.dapetfulus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

public class QAActivity extends AppCompatActivity {

    private ArrayList<QAModel> listData = new ArrayList<>();
    private RecyclerView rvQA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);


        ImageButton back_btn = findViewById(R.id.back_btn_qa);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rvQA = findViewById(R.id.rv_qa);
        rvQA.setHasFixedSize(true);

        listData.addAll(InputQASementara.getListData());
        showRecyclerList();

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
