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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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


class ReadRefferal extends AsyncTask<String, String, Void> {


    @Override
    protected Void doInBackground(String... strings) {

        String refferalId = Prefs.getString("refferal_id", null);

        String url = "https://dapetfulus.000webhostapp.com/api/v1/refferal/"+refferalId;

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
                Log.d("Response data rewards", String.valueOf(response.body()));

                if (response.code()==200) {
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                        JSONObject refferal = jsonObject.getJSONObject("refferal");
                        String invited = refferal.getString("invited");

                        Prefs.putString("invited", invited);

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
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
                        Prefs.putInt("fromrefferal", 0);
                        Prefs.putInt("fromanother", 0);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        return null;
    }
}

class UpdateRewards extends AsyncTask<String, String, Void> {



    @Override
    protected Void doInBackground(String... strings) {

        String refferalCodeRefferer = Prefs.getString("refferal_code_refferer", "norefferer");

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rewards", strings[0]);
            jsonObject.put("from", strings[1]);
            jsonObject.put("refferal", refferalCodeRefferer);
            jsonObject.put("_method", "PATCH");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);

        String rewardsId = Prefs.getString("rewards_id", null);
        String token = Prefs.getString("token", null);

        String url = "https://dapetfulus.000webhostapp.com/api/v1/rewards/"+rewardsId+"?token="+token;

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
                if (response.code()==200) {
                    Log.d("Response data rewards", String.valueOf(response));
                }
            }
        });
        return null;
    }
}

class ReadRewards extends AsyncTask<String, String, Void> {

    private DBHelper dbHelper;
    @SuppressLint("StaticFieldLeak")
    private Context context;

    ReadRewards(Context context) {
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dbHelper = DBHelper.getInstance(context);
    }



    @Override
    protected Void doInBackground(String... strings) {

        String rewardsId = Prefs.getString("rewards_id", null);


        String url = "https://dapetfulus.000webhostapp.com/api/v1/rewards/" + rewardsId;

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
                Log.d("Response data rewards", String.valueOf(response.body()));
                int currentCoin = Prefs.getInt("'coin'",  0);
                int fromRefferal = Prefs.getInt( "'fromrefferal'", 0);
                int fromAnother = Prefs.getInt("fromanother", 0);


                if (response.code()==200) {

                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                        JSONObject rewards = jsonObject.getJSONObject("rewards");
                        String rewardsFRefferal = rewards.getString("fromrefferal");
                        String rewardsFAnother = rewards.getString("fromanother");

                        int rewardsFrefferal = Integer.parseInt(rewardsFRefferal);
                        int rewardsFanother = Integer.parseInt(rewardsFAnother);

                        String currentDate = new SimpleDateFormat("d MMM yyyy, h:mm", Locale.getDefault()).format(new Date());

                        if (rewardsFrefferal>fromRefferal) {
                            Prefs.putInt("coin", currentCoin+rewardsFrefferal);
                            Prefs.putInt("fromrefferal", rewardsFrefferal);

                            HistoryModel historyModel = new HistoryModel();
                            historyModel.setTitle("Misi refferal");
                            historyModel.setTime(currentDate);
                            historyModel.setCoin(rewardsFRefferal);
                            dbHelper.addHistory(historyModel);
                        }

                        if (rewardsFanother>fromAnother) {
                            Prefs.putInt("coin", currentCoin+rewardsFanother);
                            Prefs.putInt("fromanother", rewardsFanother);
                            HistoryModel historyModel = new HistoryModel();
                            historyModel.setTitle("Misi Lainnya");
                            historyModel.setTime(currentDate);
                            historyModel.setCoin(rewardsFAnother);
                            dbHelper.addHistory(historyModel);
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


class ReadQA extends AsyncTask<String, String, Void> {

    private DBHelper dbHelper;
    @SuppressLint("StaticFieldLeak")
    private Context context;

    ReadQA(Context context) {
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dbHelper = DBHelper.getInstance(context);
    }



    @Override
    protected Void doInBackground(String... strings) {


        String url = "https://dapetfulus.000webhostapp.com/api/v1/feedback";

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
                Log.d("Response data QA", String.valueOf(response.body()));

                if (response.code()==200) {

                    dbHelper.deleteAllQA();
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                        JSONArray feedback = jsonObject.getJSONArray("feedback");

                        for (int i=0; i<feedback.length(); i++) {
                            JSONObject feedbackob = feedback.getJSONObject(i);
                            String question = feedbackob.getString("question");
                            String answer = feedbackob.getString("answer");

                            QAModel qaModel = new QAModel();
                            qaModel.setQuestion(question);
                            qaModel.setAnswer(answer);

                            dbHelper.addQA(qaModel);
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

class ReadNotif extends AsyncTask<String, String, Void> {

    private DBHelper dbHelper;
    @SuppressLint("StaticFieldLeak")
    private Context context;

    ReadNotif(Context context) {
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dbHelper = DBHelper.getInstance(context);
    }



    @Override
    protected Void doInBackground(String... strings) {


        String url = "https://dapetfulus.000webhostapp.com/api/v1/notif";

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
                Log.d("Response data Notif", String.valueOf(response.body()));

                if (response.code()==200) {

                    dbHelper.deleteAllNotif();
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                        JSONArray feedback = jsonObject.getJSONArray("notif");

                        for (int i=0; i<feedback.length(); i++) {
                            JSONObject notif = feedback.getJSONObject(i);
                            String title = notif.getString("title");
                            String time = notif.getString("time");
                            String description = notif.getString("description");

                            NotifModel notifModel = new NotifModel();
                            notifModel.setTitle(title);
                            notifModel.setDes(description);
                            notifModel.setTime(time);

                            dbHelper.addNotif(notifModel);
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
