package helpadya.com.quiz.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import helpadya.com.quiz.R;
import helpadya.com.quiz.utils.AppConfigTags;
import helpadya.com.quiz.utils.AppConfigURL;
import helpadya.com.quiz.utils.Constants;
import helpadya.com.quiz.utils.NetworkConnection;
import helpadya.com.quiz.utils.UserDetailsPref;
import helpadya.com.quiz.utils.Utils;

public class ThankuActivity extends AppCompatActivity{
    UserDetailsPref userDetailsPref;
    TextView tvThanku;
    TextView tvDashboard;
    CoordinatorLayout clMain;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thanku);
        initView();
        initData();
        initListener();

     //   sendUpdateDetailsToServer();

    }

    private void initListener() {
        tvDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish ();
                sendUpdateDetailsToServer();
                Intent intent = new Intent(ThankuActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
    }

    public void initView(){
        tvThanku = (TextView) findViewById(R.id.tvThanku);
        tvDashboard = (TextView) findViewById(R.id.tvDashboard);
        clMain = (CoordinatorLayout) findViewById(R.id.clMain);
    }

    public void initData(){
        userDetailsPref = UserDetailsPref.getInstance();
        progressDialog = new ProgressDialog(this);
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale(userDetailsPref.getStringPref(ThankuActivity.this, UserDetailsPref.LANGUAGE_TYPE));
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        tvThanku.setText(getResources().getText(R.string.activity_earned_points)+" : " + userDetailsPref.getIntPref(ThankuActivity.this, UserDetailsPref.USER_TOTAL_AMOUNT));
    }

    @Override
    public void onBackPressed () {
        MaterialDialog dialog = new MaterialDialog.Builder (this)
                .content (getResources().getText(R.string.dialog_text_quit_application))
                .positiveColor (getResources ().getColor (R.color.primary_text2))
                .contentColor (getResources ().getColor (R.color.primary_text2))
                .negativeColor (getResources ().getColor (R.color.primary_text2))
                .canceledOnTouchOutside (true)
                .cancelable (true)
                .positiveText (R.string.dialog_action_yes)
                .negativeText (R.string.dialog_action_no)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish ();
                        sendUpdateDetailsToServer();
                        Intent intent = new Intent(ThankuActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);

                    }
                })
                .onPositive (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        sendUpdateDetailsToServer();
                        finish ();
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }).build ();

        dialog.show ();
    }


    private void sendUpdateDetailsToServer () {
        if (NetworkConnection.isNetworkAvailable (ThankuActivity.this)) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_UPDATE_POINTS, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_UPDATE_POINTS,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        
                                    } else {
                                        Utils.showSnackBar (ThankuActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (ThankuActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (ThankuActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            progressDialog.dismiss ();
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            Utils.showSnackBar (ThankuActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put(AppConfigTags.USER_ID, String.valueOf(userDetailsPref.getIntPref(ThankuActivity.this, UserDetailsPref.USER_ID)));
                    params.put (AppConfigTags.EARN_POINTS, String.valueOf(userDetailsPref.getIntPref(ThankuActivity.this, UserDetailsPref.USER_TOTAL_AMOUNT)));
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (ThankuActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }

}
