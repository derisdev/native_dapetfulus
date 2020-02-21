package id.deris.dapetfulus;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pixplicity.easyprefs.library.Prefs;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Seconds;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MoreTaskFragment extends Fragment {

    private CardView misiIgCard;
    private CardView misiHarian;
    private TextView counterTv;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moretask, container, false);

        ImageButton menuBtn = view.findViewById(R.id.btn_menu);
        LinearLayout historyActivityBtn = view.findViewById(R.id.history_activity_btn);
        CardView misiIg = view.findViewById(R.id.misi_instagram);
        misiHarian = view.findViewById(R.id.misi_harian);
        misiIgCard = view.findViewById(R.id.misi_instagram_card);
        CardView misiInvite = view.findViewById(R.id.misiinvite);
        counterTv = view.findViewById(R.id.counter_tv);
        TextView currentCoin = view.findViewById(R.id.current_coin);

        currentCoin.setText(String.valueOf(Prefs.getInt("coin", 0)));



        if (Prefs.getBoolean("instagram", false)) {
            misiIgCard.setVisibility(View.GONE);
        }

        dailyTask();


        misiIg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_instagram);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


                CardView follow = dialog.findViewById(R.id.instagram_dialog_btn);

                follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Prefs.putBoolean("instagram", true);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/dapetduit.app/")));

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                final String currentDate = new SimpleDateFormat("d MMM yyyy, h:mm", Locale.getDefault()).format(new Date());
                                DBHelper dbHelper = DBHelper.getInstance(getContext());
                                HistoryModel historyModel = new HistoryModel();
                                historyModel.setTitle("Misi Instagram");
                                historyModel.setTime(currentDate);
                                historyModel.setCoin("+500");
                                dbHelper.addHistory(historyModel);

                                showDialogRewards("+500");

                            }
                        }, 7000);
                    }
                });

                dialog.show();
            }
        });


        historyActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HistoryActivity.class));
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivity(intent);
            }
        });


        misiInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    InviteFragment fragment2=new InviteFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_moretask, fragment2,"tag");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

            }
        });

        return view;
    }

    private void showDialogRewards(String newcoin) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rewards);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        TextView coin = dialog.findViewById(R.id.dialog_rewards_coin);
        FloatingActionButton back_btn = dialog.findViewById(R.id.back_btn_dialog_rewards);

       coin.setText(newcoin);

       back_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               dialog.dismiss();
           }
       });

        dialog.show();
    }

    private void dailyTask() {

        boolean isChecked = Prefs.getBoolean("ischecked", false);

        if (isChecked) {
            String timeRemaining = Prefs.getString("datefordaily", null);

            String[] listlastTime = timeRemaining.split("T");
            String[] listDate = listlastTime[0].split("-");
            String[] listTime = listlastTime[1].split(":");

            int years = Integer.parseInt(listDate[0]);
            int month = Integer.parseInt(listDate[1]);
            int date = Integer.parseInt(listDate[2]);

            int hour = Integer.parseInt(listTime[0]);
            int minute = Integer.parseInt(listTime[1]);
            Log.d("listime 2", listTime[2]);
            String[] second = (listTime[2].split("\\."));
            int newSecond = Integer.parseInt(second[0]);

            DateTime now = DateTime.now();
            DateTime last = new DateTime(years, month, date, hour, minute, newSecond);

            int count = 86400 - Seconds.secondsBetween(last.toLocalTime(), now.toLocalTime()).getSeconds();

            if (count<=0) {
                Prefs.putBoolean("ischecked", false);
            }

            Log.d("countttt", String.valueOf(count));


            new CountDownTimer(count * 1000, 1000) {
                public void onTick(long millisUntilFinished) {

                    counterTv.setText(splitToComponentTimes(millisUntilFinished/1000));
                    misiHarian.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }
                @SuppressLint("SetTextI18n")
                public void onFinish() {
                    counterTv.setText("+120");
                    misiHarian.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dailyTask();
                        }
                    });
                    Prefs.putBoolean("ischecked", false);
                }

            }.start();
        }

        else {
            misiHarian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Prefs.putBoolean("ischecked", true);
                    DateTime now = DateTime.now();
                    Prefs.putString("datefordaily", now.toString());
                    new CountDownTimer( 86400*1000, 1000) {
                        public void onTick(long millisUntilFinished) {

                            counterTv.setText(splitToComponentTimes(millisUntilFinished/1000));
                            misiHarian.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                        }
                        @SuppressLint("SetTextI18n")
                        public void onFinish() {
                            counterTv.setText("+120");
                            misiHarian.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dailyTask();
                                }
                            });
                            Prefs.putBoolean("ischecked", false);
                        }

                    }.start();
                }
            });
        }
    }

    public static String splitToComponentTimes(long biggy)
    {
        long longVal = biggy;
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        int[] ints = {hours , mins , secs};
        return ints[0]+":"+ints[1]+":"+ints[2];
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Prefs.getBoolean("instagram", false)){
            misiIgCard.setVisibility(View.GONE);
        }
    }
}
