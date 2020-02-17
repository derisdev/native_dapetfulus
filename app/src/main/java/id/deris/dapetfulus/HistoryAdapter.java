package id.deris.dapetfulus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ListViewHolder> {

    private ArrayList<HistoryModel> listHistory;
    public HistoryAdapter(ArrayList<HistoryModel> list) {
        this.listHistory = list;
    }
    @NonNull
    @Override
    public HistoryAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_history, parent, false);
        return new HistoryAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ListViewHolder holder, int position) {
        HistoryModel historyModel = listHistory.get(position);
        holder.title.setText(historyModel.getTitle());
        holder.time.setText(historyModel.getTime());
        holder.coin.setText(historyModel.getCoin());
    }

    @Override
    public int getItemCount() {
        return listHistory.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView title, time, coin;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_history);
            time = itemView.findViewById(R.id.time_history);
            coin = itemView.findViewById(R.id.koin_history);
        }
    }
}
