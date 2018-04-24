package helpadya.com.quiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import helpadya.com.quiz.R;
import helpadya.com.quiz.adapter.ScoreListAdapter;
import helpadya.com.quiz.utils.SimpleDividerItemDecoration;
import helpadya.com.quiz.utils.UserDetailsPref;

/**
 * Created by sud on 1/2/18.
 */

public class ScoreListActivity extends AppCompatActivity {
    ImageView ivBack;
    RecyclerView rvScoreList;
    ScoreListAdapter scoreListAdapter;
    UserDetailsPref userDetailsPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorelist);
        initView();
        initData();
        initListener();
        initAdapter();
    }

    private void initView() {
        ivBack = (ImageView)findViewById(R.id.ivBack);
        rvScoreList=(RecyclerView) findViewById(R.id.rvScoreList);
    }

    private void initData() {
        userDetailsPref = UserDetailsPref.getInstance();
    }

    private void initAdapter() {
        scoreListAdapter = new ScoreListAdapter(ScoreListActivity.this, MainActivityNew.scoreList);
        rvScoreList.setAdapter(scoreListAdapter);
        rvScoreList.setHasFixedSize(true);
        rvScoreList.setLayoutManager (new LinearLayoutManager(ScoreListActivity.this, LinearLayoutManager.VERTICAL, false));
        rvScoreList.addItemDecoration (new SimpleDividerItemDecoration(ScoreListActivity.this));
        rvScoreList.setItemAnimator (new DefaultItemAnimator());

    }

    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreListActivity.this, MainActivityNew.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ScoreListActivity.this, MainActivityNew.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

}
