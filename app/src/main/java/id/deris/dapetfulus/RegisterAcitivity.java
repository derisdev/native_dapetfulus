package id.deris.dapetfulus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterAcitivity extends AppCompatActivity {

    Button mulai;
    EditText name, refferal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mulai = findViewById(R.id.mulai);
        name = findViewById(R.id.name);
        refferal = findViewById(R.id.refferal);


        mulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(name.getText().toString().length() == 0){
                   name.setError("Nama harus diisi!");
               }
               else {
                   Intent intent = new Intent(RegisterAcitivity.this, MainActivity.class);
                   startActivity(intent);
                   finish();
               }
            }
        });
    }
}
