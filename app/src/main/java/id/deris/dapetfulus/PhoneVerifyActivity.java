package id.deris.dapetfulus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.pixplicity.easyprefs.library.Prefs;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PhoneVerifyActivity extends AppCompatActivity {

    SpinKitView spinkit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);

        ImageButton backBtn = findViewById(R.id.back_btn_phone);
        final EditText phoneNumber = findViewById(R.id.phone_number_et);
        final CardView yakin = findViewById(R.id.yakin);

        spinkit = findViewById(R.id.spinkit_phone_verifiy);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        yakin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yakin.setVisibility(View.GONE);
                spinkit.setVisibility(View.VISIBLE);
                final String phone = phoneNumber.getText().toString();
                String userId = Prefs.getString("user_id", null);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_id", userId);
                    jsonObject.put("phone", phone);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
                String url = "https://dapetfulus.000webhostapp.com/api/v1/user/phone_verify";
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().header("Accept", "application/json")
                        .url(url)
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        PhoneVerifyActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) {
                        Log.d("Response data", response.toString());

                        if(response.code()==201){
                            Prefs.putString("phone", phone);
                            PhoneVerifyActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Nomor Ditambahkan", Toast.LENGTH_SHORT).show();
                                    spinkit.setVisibility(View.GONE);
                                    yakin.setVisibility(View.VISIBLE);
                                    onBackPressed();
                                    finish();
                                }
                            });


                        }
                        else {
                            PhoneVerifyActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Terjadi Kesalahan saat menambahan Nomor Telpon", Toast.LENGTH_SHORT).show();
                                    spinkit.setVisibility(View.GONE);
                                    yakin.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                });
            }
        });
    }

}
