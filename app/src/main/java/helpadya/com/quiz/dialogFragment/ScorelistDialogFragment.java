package helpadya.com.quiz.dialogFragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import helpadya.com.quiz.R;
import helpadya.com.quiz.activity.MainActivityNew;
import helpadya.com.quiz.adapter.ScoreListAdapter;
import helpadya.com.quiz.utils.SimpleDividerItemDecoration;
import helpadya.com.quiz.utils.UserDetailsPref;

public class ScorelistDialogFragment extends DialogFragment {
    // ImageView ivCancel;
    ImageView ivCancel;
    RecyclerView rvScoreList;
    ScoreListAdapter scoreListAdapter;
    UserDetailsPref userDetailsPref;

    public ScorelistDialogFragment newInstance() {
        ScorelistDialogFragment f = new ScorelistDialogFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }
    /*@Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        Window window = getDialog().getWindow();
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
    }*/
    @Override
    public void onResume() {
        super.onResume();
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction() != KeyEvent.ACTION_UP)
                        return true;
                    else {
                        getDialog().dismiss();
                        //Hide your keyboard here!!!!!!
                        return true; // pretend we've processed it
                    }
                } else
                    return false; // pass on to be processed as normal
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_fragment_scorelist, container, false);
        initView(root);
        initData();
        initListener();
        initAdapter();
        return root;
    }
    private void initView(View root) {
        //ivCancel = (ImageView)root.findViewById(R.id.ivCancel);
        rvScoreList=(RecyclerView) root.findViewById(R.id.rvScoreList);
    }

    private void initData() {
        userDetailsPref = UserDetailsPref.getInstance();
    }

    private void initAdapter() {
       scoreListAdapter = new ScoreListAdapter(getActivity(), MainActivityNew.scoreList);
        rvScoreList.setAdapter(scoreListAdapter);
        rvScoreList.setHasFixedSize(true);
        rvScoreList.setLayoutManager (new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvScoreList.addItemDecoration (new SimpleDividerItemDecoration(getActivity()));
        rvScoreList.setItemAnimator (new DefaultItemAnimator());

    }

    private void initListener() {
        /*ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });*/
    }

}