package id.deris.dapetfulus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;

public class MenuActivity extends AppCompatActivity {

    ImageView backButton;
    TextView username ,phoneVerifyBtn;
    LinearLayout qaMenu, notifMenu, helpMenu;
    TextView phoneNumberTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        backButton = findViewById(R.id.back_btn);
        username = findViewById(R.id.username_menu);
        phoneVerifyBtn = findViewById(R.id.phone_verify_btn);
        qaMenu = findViewById(R.id.qa_menu);
        notifMenu = findViewById(R.id.notif_menu);
        helpMenu = findViewById(R.id.help_menu);
        phoneNumberTv = findViewById(R.id.phone_number_tv);



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        String name = Prefs.getString("name", "Data tidak ada");
        username.setText(name);

        String phoneNumber = Prefs.getString("phone", null);
        if(phoneNumber!=null) {
            phoneNumberTv.setText(phoneNumber);
            phoneNumberTv.setVisibility(View.VISIBLE);
            phoneVerifyBtn.setVisibility(View.GONE);
        }


        phoneVerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, PhoneVerifyActivity.class);
                startActivity(intent);
            }
        });

        qaMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, QAActivity.class);
                startActivity(intent);
            }
        });

        notifMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, NotifActivity.class);
                startActivity(intent);
            }
        });

        helpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });







        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT );


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

}
