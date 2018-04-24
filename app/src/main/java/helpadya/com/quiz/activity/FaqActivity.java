package helpadya.com.quiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import helpadya.com.quiz.R;
import helpadya.com.quiz.utils.AppConfigTags;
import helpadya.com.quiz.utils.UserDetailsPref;

/**
 * Created by sud on 1/2/18.
 */

public class FaqActivity extends AppCompatActivity {
    ImageView ivBack;
    ListView lvFaq;
    UserDetailsPref userDetailsPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        initView();
        initData();
        initListener();
        initAdapter();
    }

    private void initView() {
        ivBack = (ImageView)findViewById(R.id.ivBack);
        lvFaq=(ListView) findViewById(R.id.lvFaq);
    }

    private void initData() {
        userDetailsPref = UserDetailsPref.getInstance();
        try {
            JSONObject jsonObj = new JSONObject(userDetailsPref.getStringPref(this, UserDetailsPref.RESPONSE));
            boolean error = jsonObj.getBoolean(AppConfigTags.ERROR);
            String message = jsonObj.getString(AppConfigTags.MESSAGE);
            if (!error) {
                JSONArray jsonArrayFaq = jsonObj.getJSONArray(AppConfigTags.FAQLIST);
                String[] faq = new String[jsonArrayFaq.length()];
                for (int i = 0; i < jsonArrayFaq.length(); i++) {
                    JSONObject jsonObjFaq = jsonArrayFaq.getJSONObject(i);
                    faq[i] = jsonObjFaq.getString("faq");
                }


                ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.simple_list,faq);
                //Setting adapter to listview
                lvFaq.setAdapter (adapter);
            } else {
                //Utils.showSnackBar(this, clMain, message, Snackbar.LENGTH_LONG, null, null);
            }

        } catch (Exception e) {
            //Utils.showSnackBar(MainActivityNew.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
            e.printStackTrace();
        }
    }


    private void initAdapter() {


    }

    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FaqActivity.this, MainActivityNew.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FaqActivity.this, MainActivityNew.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
