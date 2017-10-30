package ahmedali.androidtask.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ahmedali.androidtask.R;
import ahmedali.androidtask.model.RepoModel;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.MyViewHolder> {
    private Context context;
    private List<RepoModel> models;
    private LayoutInflater inflater;
    private final OnItemLongClickListener listener;

    public interface OnItemLongClickListener {
        void onItemLongClick(RepoModel item);
    }

    public RepoAdapter(Context context, List<RepoModel> models, OnItemLongClickListener listener) {
        this.context = context;
        this.models = models;
        this.listener = listener;

        if (context != null)
            this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RepoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_model, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RepoAdapter.MyViewHolder holder, int position) {
        RepoModel currentobj = models.get(position);
        holder.setData(currentobj, position);
        holder.bind(models.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView repoCard;
        private TextView repoName, ownerName, description;

        public MyViewHolder(View itemView) {
            super(itemView);
            repoCard = (CardView) itemView.findViewById(R.id.repoCard);
            repoName = (TextView) itemView.findViewById(R.id.repoName);
            ownerName = (TextView) itemView.findViewById(R.id.ownerName);
            description = (TextView) itemView.findViewById(R.id.description);
        }

        public void setData(RepoModel currentobj, int position) {
            repoName.setText(currentobj.repoName);
            ownerName.setText(currentobj.ownerName);
            description.setText(currentobj.repoDesc);

            currentobj.setForkMissing(!currentobj.fork);
            repoCard.setCardBackgroundColor(currentobj.isForkMissing() ? ContextCompat.getColor(context, R.color.lightGreen):ContextCompat.getColor(context, R.color.whiteBG));
        }

        public void bind(final RepoModel repoModel, final OnItemLongClickListener listener) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(repoModel);
                    return true;
                }
            });
        }
    }
}
