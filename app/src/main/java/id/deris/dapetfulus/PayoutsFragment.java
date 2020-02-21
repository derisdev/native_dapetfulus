package id.deris.dapetfulus;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pixplicity.easyprefs.library.Prefs;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PayoutsFragment extends Fragment {


    private CardView tabPayouts1, tabPayouts2, withdraw1, withdraw2, withdraw3, withdraw4, withdraw5, withdraw6;
    private TextView tabPayouts1text, tabPayouts2text;
    private NestedScrollView withdrawLayout;
    private RelativeLayout withdrawHistoryLayout;
    private SpinKitView spinKitView;
    private LinearLayout dataEmpty;
    private SwipeRefreshLayout swipeContainer, swipeContainerEmpty;



    private ArrayList<WithdrawModel> listData = new ArrayList<>();
    private RecyclerView rvWithdraw;

    LinearLayout loadingWithdrawLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payouts, container, false);



        tabPayouts1 = view.findViewById(R.id.tabPayouts1);
        tabPayouts2 = view.findViewById(R.id.tabPayouts2);
        tabPayouts1text = view.findViewById(R.id.tabPayouts1Text);
        tabPayouts2text = view.findViewById(R.id.tabPayouts2Text);
        withdrawLayout = view.findViewById(R.id.withdraw_layout);
        withdrawHistoryLayout = view.findViewById(R.id.withdraw_history_layout);
        spinKitView = view.findViewById(R.id.spinkit_layout);
        dataEmpty = view.findViewById(R.id.data_empty_layout);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainerEmpty = view.findViewById(R.id.swipeContainer_empty);
        withdraw1 =  view.findViewById(R.id.withdraw1);
        withdraw2 =  view.findViewById(R.id.withdraw2);
        withdraw3 =  view.findViewById(R.id.withdraw3);
        withdraw4 =  view.findViewById(R.id.withdraw4);
        withdraw5 =  view.findViewById(R.id.withdraw5);
        withdraw6 =  view.findViewById(R.id.withdraw6);
        loadingWithdrawLayout = view.findViewById(R.id.loading_withdraw_layout);
        TextView coin_total = view.findViewById(R.id.payouts_coin_total);

        coin_total.setText(String.valueOf(Prefs.getInt("coin", 0)));

        ImageButton menuBtn = view.findViewById(R.id.btn_menu);

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivity(intent);
            }
        });


        rvWithdraw = view.findViewById(R.id.rv_withdraw);
        rvWithdraw.setHasFixedSize(true);

        DBHelper dbHelper = DBHelper.getInstance(getContext());
        List<WithdrawModel> payments = dbHelper.getAllPayment();
        listData.addAll(payments);
        showRecyclerList();





        tabPayouts1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabPayouts1.setCardBackgroundColor(Color.WHITE);
                tabPayouts2.setCardBackgroundColor(getResources().getColor(R.color.tabTransparent));
                tabPayouts1text.setTextColor(getResources().getColor(R.color.amber));
                tabPayouts2text.setTextColor(Color.BLACK);
                withdrawLayout.setVisibility(View.VISIBLE);
                withdrawHistoryLayout.setVisibility(View.GONE);
                dataEmpty.setVisibility(View.GONE);
                spinKitView.setVisibility(View.GONE);
            }
        });

        tabPayouts2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                tabPayouts2.setCardBackgroundColor(Color.WHITE);
                tabPayouts1.setCardBackgroundColor(getResources().getColor(R.color.tabTransparent));
                tabPayouts2text.setTextColor(getResources().getColor(R.color.amber));
                tabPayouts1text.setTextColor(Color.BLACK);
                withdrawLayout.setVisibility(View.GONE);
                spinKitView.setVisibility(View.VISIBLE);


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DBHelper dbHelper = DBHelper.getInstance(getContext());
                        List<WithdrawModel> payments = dbHelper.getAllPayment();
                        if (payments.isEmpty()){
                            dataEmpty.setVisibility(View.VISIBLE);
                        }
                        else {
                            withdrawHistoryLayout.setVisibility(View.VISIBLE);
                        }
                        spinKitView.setVisibility(View.GONE);

                    }
                }, 1000);


            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ReadPayment readPayment = new ReadPayment(getContext());
                readPayment.execute();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DBHelper dbHelper = DBHelper.getInstance(getContext());
                        List<WithdrawModel> payments = dbHelper.getAllPayment();
                        listData.addAll(payments);
                       WithdrawAdapter adapter = new WithdrawAdapter(getContext(), listData);
                       adapter.clear();
                       adapter.addAll(listData);
                        if (payments.isEmpty()){
                            dataEmpty.setVisibility(View.VISIBLE);
                        }
                        else {
                            withdrawHistoryLayout.setVisibility(View.VISIBLE);
                        }
                        swipeContainer.setRefreshing(false);
                    }
                }, 5000);
            }
        });


        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainerEmpty.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ReadPayment readPayment = new ReadPayment(getContext());
                readPayment.execute();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DBHelper dbHelper = DBHelper.getInstance(getContext());
                        List<WithdrawModel> payments = dbHelper.getAllPayment();
                        listData.addAll(payments);
                        WithdrawAdapter adapter = new WithdrawAdapter(getContext(), listData);
                        adapter.clear();
                        adapter.addAll(listData);
                        if (payments.isEmpty()){
                            dataEmpty.setVisibility(View.VISIBLE);
                        }
                        else {
                            withdrawHistoryLayout.setVisibility(View.VISIBLE);
                        }
                        swipeContainerEmpty.setRefreshing(false);
                    }
                }, 5000);
            }
        });
        swipeContainerEmpty.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        withdraw1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("5000", "DANA", "IDR 10.000");
            }
        });
        withdraw2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("5000", "Gopay", "IDR 10.000");
            }
        });
        withdraw3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("9000", "OVO", "IDR 20.000");
            }
        });
        withdraw4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("9000", "DANA", "IDR 20.000");
            }
        });
        withdraw5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("11500", "DANA", "IDR 25.000");
            }
        });
        withdraw6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("11500", "Gopay", "IDR 25.000");
            }
        });

        return view;
    }

    private void showRecyclerList(){
        rvWithdraw.setLayoutManager(new LinearLayoutManager(getContext()));
        WithdrawAdapter listAdapter = new WithdrawAdapter(getContext(), listData);
        rvWithdraw.setAdapter(listAdapter);
    }

    private void showDialog(String koin, String via, String amount){

        if(Prefs.getInt("coin", 0)>=Integer.parseInt(koin)) {
            if(Prefs.getString("phone", null)!=null) {
                dialogWithdraw(via, amount, Integer.parseInt(koin));
            }
            else {
                dialogVerify();
            }
        }
        else {
            dialogNotEnough();
        }
    }

    private void dialogNotEnough() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_not_enough);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        FloatingActionButton fab_back = dialog.findViewById(R.id.back_btn_dialog_not_enough);

        fab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void dialogVerify() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_phone_verify);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        CardView verify = dialog.findViewById(R.id.verify_dialog_btn);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(getActivity(), PhoneVerifyActivity.class));
            }
        });

        dialog.show();
    }

    private void dialogWithdraw(final String via, final String amount, final int koin) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_withdraw);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        CardView withdraw = dialog.findViewById(R.id.withdraw_dialog_btn);
        TextView name = dialog.findViewById(R.id.dialog_withdraw_username);
        TextView phone = dialog.findViewById(R.id.dialog_withdraw_phone);
        TextView date = dialog.findViewById(R.id.dialog_withdraw_date);
        TextView coin = dialog.findViewById(R.id.dialog_withdraw_koin);
        TextView via_tv = dialog.findViewById(R.id.dialog_withdraw_via);
        TextView amount_tv = dialog.findViewById(R.id.dialog_withdraw_jumlah);


        final String currentDate = new SimpleDateFormat("d MMM yyyy, h:mm", Locale.getDefault()).format(new Date());



        name.setText(Prefs.getString("name", "Tidak ada nama"));
        phone.setText(Prefs.getString("phone", "Nomor Belum ditambahkan"));
        date.setText(currentDate);
        coin.setText(String.valueOf(koin));
        via_tv.setText(via);
        amount_tv.setText(amount);

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                loadingWithdrawLayout.setVisibility(View.VISIBLE);
                createPayment(via, amount, koin);
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void dialogSuccess() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_withdraw_success);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        FloatingActionButton fab_back = dialog.findViewById(R.id.back_btn_dialog_withdraw_success);

        fab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void createPayment(String via, String amount, final int koin) {
        JSONObject jsonObject = new JSONObject();
        final String currentDate = new SimpleDateFormat("d MMM yyyy, h:mm", Locale.getDefault()).format(new Date());
        try {
            jsonObject.put("user_id", Prefs.getString("user_id", null));
            jsonObject.put("phone", Prefs.getString("phone", null));
            jsonObject.put("via", via);
            jsonObject.put("amount", amount);
            jsonObject.put("status", "Pending");
            jsonObject.put("time", currentDate);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);

        String token = Prefs.getString("token", null);

        String url = "https://dapetfulus.000webhostapp.com/api/v1/payment?token="+token;

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder().header("Accept", "application/json")
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if(response.code()==201) {
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingWithdrawLayout.setVisibility(View.GONE);
                            dialogSuccess();
                            Prefs.putInt("coin", Prefs.getInt("coin", 0)-koin);
                            DBHelper dbHelper = DBHelper.getInstance(getContext());
                            HistoryModel historyModel = new HistoryModel();
                            historyModel.setTitle("Withdraw");
                            historyModel.setCoin("-"+koin);
                            historyModel.setTime(currentDate);
                            dbHelper.addHistory(historyModel);

                            UpdateRewards updateRewards = new UpdateRewards();
                            updateRewards.execute("-"+ koin, "Minus Withdraw");
                        }

                    });
                }
                else {
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingWithdrawLayout.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Terjadi Kesalahan. Coba Lagi nanti", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}
