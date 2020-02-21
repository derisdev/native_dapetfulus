package id.deris.dapetfulus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;

public class DetailWithdrawHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_withdraw_history);

        String newDate = getIntent().getStringExtra("date");
        String newStatus = getIntent().getStringExtra("status");
        String newVia = getIntent().getStringExtra("via");
        String newamount = getIntent().getStringExtra("amount");


        TextView name = findViewById(R.id.dialog_withdraw_username);
        TextView phone = findViewById(R.id.dialog_withdraw_phone);
        TextView date = findViewById(R.id.dialog_withdraw_date);
        TextView status = findViewById(R.id.dialog_withdraw_status);
        TextView via = findViewById(R.id.dialog_withdraw_via);
        TextView amount = findViewById(R.id.dialog_withdraw_jumlah);
        ImageButton back_btn = findViewById(R.id.back_btn_detail_history);

        name.setText(Prefs.getString("name", "tidak ada data"));
        phone.setText(Prefs.getString("phone", "tidak ada data"));
        date.setText(newDate);
        status.setText(newStatus);
        via.setText(newVia);
        amount.setText(newamount);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT );
    }
}
