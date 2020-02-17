package id.deris.dapetfulus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<HistoryModel> listData = new ArrayList<>();
    private RecyclerView rvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ImageButton back_btn = findViewById(R.id.back_btn_history);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        rvHistory = findViewById(R.id.rv_history);
        rvHistory.setHasFixedSize(true);

        DBHelper dbHelper =  DBHelper.getInstance(this);
        List<HistoryModel> history = dbHelper.getAllHistory();
        listData.addAll(history);
        showRecyclerList();

        fullscreen();
    }

    private void showRecyclerList(){
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        HistoryAdapter listAdapter = new HistoryAdapter(listData);
        rvHistory.setAdapter(listAdapter);
    }

    private void fullscreen() {

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT );
    }
}
