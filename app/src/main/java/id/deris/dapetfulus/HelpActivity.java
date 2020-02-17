package id.deris.dapetfulus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


        ImageButton backBtn = findViewById(R.id.back_btn_help);
        CardView whatsapp = findViewById(R.id.help_whatsapp);
        CardView mail = findViewById(R.id.help_mail);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=6285719632945&text=Phone:%0ASubject:%0AKategori%20Masalah:%0ARincian%20Masalah:%0A%0A%0AMohon%20Sertakan%20Screenshot.%0A%0A*Pilihan%20Kategori:Login,%20Misi,%20Aktivitas,%20Kesalahan/Bug,%20Koin/Uang,%20Masukan,%20Lain")));
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:deris.dev7@gmail.com?subject=Feedback-DapetDuit&body=Phone:\nKategori Masalah: \nRincian Masalah: \nMohon sertakan screenshot")));
            }
        });




        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT );


    }
}
