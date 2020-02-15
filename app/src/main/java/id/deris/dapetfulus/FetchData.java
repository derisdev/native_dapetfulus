package id.deris.dapetfulus;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pixplicity.easyprefs.library.Prefs;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
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

  class CreateRefferal extends AsyncTask<String, String, Void> {


    @Override
    protected Void doInBackground(String... strings) {

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", strings[1]);
            jsonObject.put("refferal", strings[0]);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);

        String url = "https://dapetfulus.000webhostapp.com/api/v1/refferal?token=" + strings[2];

        OkHttpClient client = new OkHttpClient();


        final Request request = new Request.Builder().header("Accept", "application/json")
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                Log.d("Response data", String.valueOf(response.body()));

                if (response.code()==201) {

                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                        JSONObject refferal = jsonObject.getJSONObject("refferal");
                        String refferalId = refferal.getString("id");
                        Prefs.putString("refferal_id", refferalId);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
                Log.d("refferalId", Prefs.getString("refferal_id", null));

            }
        });
        return null;
    }
}

class CreateRewards extends AsyncTask<String, String, Void> {

    @Override
    protected Void doInBackground(String... strings) {

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", strings[0]);
            jsonObject.put("rewards", "0");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);

        String url = "https://dapetfulus.000webhostapp.com/api/v1/rewards?token=" + strings[1];

        OkHttpClient client = new OkHttpClient();


        final Request request = new Request.Builder().header("Accept", "application/json")
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                Log.d("Response data rewards", String.valueOf(response.body()));

                if (response.code()==201) {

                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                        JSONObject rewards = jsonObject.getJSONObject("rewards");
                        String rewardsId = rewards.getString("id");
                        Prefs.putString("rewards_id", rewardsId);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
                Log.d("rewardsId", Prefs.getString("rewards_id", null));

            }
        });
        return null;
    }
}

class ReadPayment extends AsyncTask<String, String, Void> {

      private DBHelper dbHelper;
    @SuppressLint("StaticFieldLeak")
    private Context context;

       ReadPayment(Context context) {
          this.context = context;
      }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dbHelper = DBHelper.getInstance(context);
    }

    @Override
    protected Void doInBackground(String... strings) {

          String phone = Prefs.getString("phone", null);

        final JSONObject jsonObject = new JSONObject();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);

        String url = "https://dapetfulus.000webhostapp.com/api/v1/user/payment/"+phone;

        OkHttpClient client = new OkHttpClient();


        final Request request = new Request.Builder().header("Accept", "application/json")
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                Log.d("Response read payment", String.valueOf(response));

                if (response.code()==200) {

                    dbHelper.deleteAllPayments();

                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                        JSONArray payments = jsonObject.getJSONArray("payment");

                        for (int i=0; i<payments.length(); i++) {
                            JSONObject payment = payments.getJSONObject(i);
                            String via = payment.getString("via");
                            String amount = payment.getString("amount");
                            String status = payment.getString("status");
                            String time = payment.getString("time");

                            WithdrawModel withdrawModel = new WithdrawModel();
                            withdrawModel.setVia(via);
                            withdrawModel.setAmount(amount);
                            withdrawModel.setStatus(status);
                            withdrawModel.setTime(time);

                            dbHelper.addPayment(withdrawModel);
                        }

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        return null;
    }

}
