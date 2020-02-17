package id.deris.dapetfulus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WithdrawAdapter extends RecyclerView.Adapter<WithdrawAdapter.ListViewHolder>{

    private ArrayList<WithdrawModel> listWithdraw;
    public WithdrawAdapter(ArrayList<WithdrawModel> list) {
        this.listWithdraw = list;
    }


    @NonNull
    @Override
    public WithdrawAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_withdraw, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WithdrawAdapter.ListViewHolder holder, int position) {
        WithdrawModel withdraw = listWithdraw.get(position);
        holder.amount.setText(withdraw.getAmount());
        holder.time.setText(withdraw.getTime());
        holder.status.setText(withdraw.getStatus());

        String via = withdraw.getVia();

        if (via.equals("DANA")){
            holder.icon.setImageResource(R.drawable.dana);
        }
        else if(via.equals("OVO")) {
            holder.icon.setImageResource(R.drawable.ovo);
        }
        else {
            holder.icon.setImageResource(R.drawable.gopay);
        }
    }

    @Override
    public int getItemCount() {
        return listWithdraw.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView amount, time, status;
        ImageView icon;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon_withdraw);
            amount = itemView.findViewById(R.id.amount_withdraw);
            time = itemView.findViewById(R.id.time_withdraw);
            status = itemView.findViewById(R.id.status_withdraw);
        }
    }

    public void clear() {
        listWithdraw.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<WithdrawModel> list) {
        listWithdraw.addAll(list);
        notifyDataSetChanged();
    }
}
