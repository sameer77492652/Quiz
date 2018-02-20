package helpadya.com.quiz.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import helpadya.com.quiz.R;
import helpadya.com.quiz.model.ScoreList;
import helpadya.com.quiz.utils.Utils;


public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;

    private Activity activity;
    private List<ScoreList> scoreList = new ArrayList<ScoreList>();
    ProgressBar progressDialog;

    public ScoreListAdapter(Activity activity, List<ScoreList> scoreList) {
        this.activity = activity;
        this.scoreList = scoreList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.list_item_scorelist, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {//        runEnterAnimation (holder.itemView);
        final ScoreList scoreListobj = scoreList.get(position);
        progressDialog = new ProgressBar(activity);
        Utils.setTypefaceToAllViews(activity, holder.tvWinnerName);
        holder.tvWinnerName.setText(scoreListobj.getName());
        if(position != 0){
            holder.ivTopper.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivTopper;
        TextView tvWinnerName;

        public RelativeLayout rlMain;

        ProgressBar progressBar;

        public ViewHolder(View view) {
            super(view);
            ivTopper = (ImageView) view.findViewById(R.id.ivTopper);
            tvWinnerName = (TextView) view.findViewById(R.id.tvWinnerName);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // final Jobs jobDescription = bookingList.get (getLayoutPosition ());
            // activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
          //  mItemClickListener.onItemClick(v, getLayoutPosition());

        }
    }
}