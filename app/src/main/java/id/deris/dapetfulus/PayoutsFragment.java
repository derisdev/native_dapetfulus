package id.deris.dapetfulus;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PayoutsFragment extends Fragment {


    private CardView tabPayouts1, tabPayouts2;
    private TextView tabPayouts1text, tabPayouts2text;
    private NestedScrollView withdrawLayout;
    private RelativeLayout withdrawHistoryLayout;



    private ArrayList<WithdrawModel> listData = new ArrayList<>();
    private RecyclerView rvWithdraw;



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

        listData.addAll(InputWithdrawSementara.getListData());
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
            }
        });

        tabPayouts2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabPayouts2.setCardBackgroundColor(Color.WHITE);
                tabPayouts1.setCardBackgroundColor(getResources().getColor(R.color.tabTransparent));
                tabPayouts2text.setTextColor(getResources().getColor(R.color.amber));
                tabPayouts1text.setTextColor(Color.BLACK);
                withdrawHistoryLayout.setVisibility(View.VISIBLE);
                withdrawLayout.setVisibility(View.GONE);
            }
        });



        return view;
    }

    private void showRecyclerList(){
        rvWithdraw.setLayoutManager(new LinearLayoutManager(getContext()));
        WithdrawAdapter listAdapter = new WithdrawAdapter(listData);
        rvWithdraw.setAdapter(listAdapter);
    }

}
