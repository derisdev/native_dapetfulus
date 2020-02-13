package id.deris.dapetfulus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QAAdapter extends RecyclerView.Adapter<QAAdapter.ListViewHolder> {

    private ArrayList<QAModel> listQA;
    public QAAdapter(ArrayList<QAModel> list) {
        this.listQA = list;
    }

    @NonNull
    @Override
    public QAAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_qa, parent, false);
        return new QAAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QAAdapter.ListViewHolder holder, int position) {
        QAModel model = listQA.get(position);
        holder.question.setText(model.getQuestion());
        holder.answer.setText(model.getAnswer());
    }

    @Override
    public int getItemCount() {
        return listQA.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView question, answer;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.answer);
        }
    }
}
