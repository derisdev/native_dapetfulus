package id.deris.dapetfulus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.ListViewHolder> {

    private ArrayList<NotifModel> listNotif;
    public NotifAdapter(ArrayList<NotifModel> list) {
        this.listNotif = list;
    }

    @NonNull
    @Override
    public NotifAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_notif, parent, false);
        return new NotifAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifAdapter.ListViewHolder holder, int position) {
        NotifModel notifModel = listNotif.get(position);
        holder.title.setText(notifModel.getTitle());
        holder.time.setText(notifModel.getTime());
    }

    @Override
    public int getItemCount() {
        return listNotif.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView title, time, des;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_notif);
            time =itemView.findViewById(R.id.time_notif);
        }
    }
    public void clear() {
        listNotif.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<NotifModel> list) {
        listNotif.addAll(list);
        notifyDataSetChanged();
    }

}
