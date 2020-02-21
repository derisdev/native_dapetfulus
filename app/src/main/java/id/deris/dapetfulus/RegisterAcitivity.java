package id.deris.dapetfulus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.pixplicity.easyprefs.library.Prefs;
import com.slmyldz.random.Randoms;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static android.content.ContentValues.TAG;

public class RegisterAcitivity extends AppCompatActivity {

    Button mulai;
    EditText name, refferal;
    SpinKitView spinKitView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        mulai = findViewById(R.id.mulai);
        name = findViewById(R.id.username);
        refferal = findViewById(R.id.refferal);
        spinKitView = findViewById(R.id.spin_kit);

        getdynamicLink();



        mulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(name.getText().toString().length() == 0){
                   name.setError("Nama harus diisi!");
               }
               else {

                   mulai.setVisibility(View.GONE);
                   spinKitView.setVisibility(View.VISIBLE);

                   String refferalOwner = Randoms.alphaNumericString(6);

                   saveData(refferalOwner);

               }
            }
        });

    }

    private void getdynamicLink() {


        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            assert deepLink != null;
                            String refferal_refferer = deepLink.getQueryParameter("invitedby");
                            refferal.setText(refferal_refferer);
                        }
                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });
    }

    private void createDynamicLink(String refferal) {

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://app.dapetfulus.id/?invitedby="+refferal))
                .setDomainUriPrefix("https://dapetfulus.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("id.deris.dapetfulus").setMinimumVersion(1).build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();


        Prefs.putString("link_refferal", dynamicLinkUri.toString());

    }

    private void saveData(final String refferalOwner) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name.getText().toString());
            jsonObject.put("refferal", refferal.getText().toString());
            jsonObject.put("email", refferalOwner);
            jsonObject.put("password", refferalOwner);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);

        String url = "https://dapetfulus.000webhostapp.com/api/v1/user/register";

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder().header("Accept", "application/json")
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                RegisterAcitivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Gagal Terhubung", Toast.LENGTH_SHORT).show();
                        spinKitView.setVisibility(View.GONE);
                        mulai.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                Log.d("Response data", response.toString());

                if (response.code()==201) {

                    Prefs.putBoolean("login", true);
                    if(!refferal.getText().toString().isEmpty()) {
                        Prefs.putString("refferal_code_refferer", refferal.getText().toString());
                    }

                    if (!refferal.getText().toString().equals("")) {
                        Prefs.putBoolean("haveRefferal", true);
                    }

                    Prefs.putString("refferal_code_owner", refferalOwner);

                    createDynamicLink(refferalOwner);

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject user = null;
                    try {
                        assert jsonObject != null;
                        user = jsonObject.getJSONObject("user");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String username;
                    try {
                        assert user != null;
                        username = user.getString("name");
                        Prefs.putString("name", username);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    int userId = 0;
                    try {
                        userId = user.getInt("id");
                        Prefs.putString("user_id", Integer.toString(userId));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String token = null;
                    try {
                        token = jsonObject.getString("token");
                        Prefs.putString("token", token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    CreateRefferal createRefferal = new CreateRefferal();
                    createRefferal.execute(refferalOwner, String.valueOf(userId), token);
                    CreateRewards createRewards = new CreateRewards();
                    createRewards.execute(String.valueOf(userId), token);


                    Intent intent = new Intent(RegisterAcitivity.this, MainActivity.class);
                    startActivity(intent);

                    finish();

                    RegisterAcitivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            spinKitView.setVisibility(View.GONE);
                            mulai.setVisibility(View.VISIBLE);
                        }
                    });
                }
                else if(response.code()==500){
                    RegisterAcitivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Username sudah ada yang menggunakan",Toast.LENGTH_SHORT).show();
                            spinKitView.setVisibility(View.GONE);
                            mulai.setVisibility(View.VISIBLE);
                        }
                    });

                }
                else if(response.code()==504) {
                    RegisterAcitivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Server sedang ada gangguan. Coba lagi nanti",Toast.LENGTH_SHORT).show();
                            spinKitView.setVisibility(View.GONE);
                            mulai.setVisibility(View.VISIBLE);
                        }
                    });
                }
                else if(response.code()==404) {
                    RegisterAcitivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Kode refferal todak valid",Toast.LENGTH_SHORT).show();
                            spinKitView.setVisibility(View.GONE);
                            mulai.setVisibility(View.VISIBLE);
                        }
                    });
                }
                else {
                        RegisterAcitivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Gagal terhubung ke server",Toast.LENGTH_SHORT).show();
                                spinKitView.setVisibility(View.GONE);
                                mulai.setVisibility(View.VISIBLE);
                            }
                        });
                }
            }
        });

    }
}
