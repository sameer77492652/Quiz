package helpadya.com.quiz.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import helpadya.com.quiz.R;
import helpadya.com.quiz.utils.AppConfigTags;
import helpadya.com.quiz.utils.AppConfigURL;
import helpadya.com.quiz.utils.Constants;
import helpadya.com.quiz.utils.NetworkConnection;
import helpadya.com.quiz.utils.Utils;

/**
 * Created by actiknow on 9/21/17.
 */

public class EnquiryActivity extends AppCompatActivity {
    EditText etName;
    EditText etPhone;
    EditText etEmail;
    EditText etMessage;
    CoordinatorLayout clMain;
    TextView tvSubmit;
    TextView tvEnquiryCall;
    ImageView ivBack;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry2);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        etName = (EditText)findViewById(R.id.etName);
        etPhone = (EditText)findViewById(R.id.etPhone);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etMessage = (EditText)findViewById(R.id.etMessage);
        clMain = (CoordinatorLayout)findViewById(R.id.clMain);
        ivBack = (ImageView)findViewById(R.id.ivBack);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        tvEnquiryCall = (TextView) findViewById(R.id.tvEnquiryCall);
    }

    private void initListener() {
        ivBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(EnquiryActivity.this, MainActivityNew.class);
                startActivity(intent);
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        tvEnquiryCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPermissionGranted()) {
                    call_action();
                }
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText ().toString ();
                String contact = etPhone.getText ().toString ();
                String email = etEmail.getText ().toString ();
                String message = etMessage.getText ().toString ();
                if (name.equalsIgnoreCase ("")) {
                    Utils.showToast (EnquiryActivity.this, "Please Enter Your Name", true);
                } else if (contact.equalsIgnoreCase ("")) {
                    Utils.showToast (EnquiryActivity.this, "Please Enter Your Mobile Number", true);
                }else if (email.equalsIgnoreCase ("")) {
                    Utils.showToast (EnquiryActivity.this, "Please Enter Your Email", true);
                }else if (message.equalsIgnoreCase ("")) {
                    Utils.showToast (EnquiryActivity.this, "Please Enter the  Message", true);
                } else {
                    sendQueryToServer(etName.getText().toString().trim(), etEmail.getText().toString().trim(), etPhone.getText().toString().trim(), etMessage.getText().toString().trim());
                }
            }
        });

    }

    private void initData(){
        progressDialog = new ProgressDialog(this);
    }


    /*private void sendContactUsToServer(){
        if(NetworkConnection.isNetworkAvailable(EnquiryActivity.this)){
            Utils.showProgressDialog(progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait),true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_CATEGORY,true);
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.URL_CATEGORY,
                    new com.android.volley.Response.Listener<String>(){
                        
                    }
                    )
        }
    }*/


    private void sendQueryToServer (final String name, final String email, final String number,final String message) {
        if (NetworkConnection.isNetworkAvailable (EnquiryActivity.this)) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_QUERY, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_QUERY,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        Utils.showToast(EnquiryActivity.this, message, true);
                                        Intent intent = new Intent (EnquiryActivity.this, MainActivityNew.class);
                                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity (intent);
                                        overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else {
                                        Utils.showSnackBar (EnquiryActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (EnquiryActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (EnquiryActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (EnquiryActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put (AppConfigTags.USER_NAME, name);
                    params.put (AppConfigTags.USER_EMAIL, email);
                    params.put (AppConfigTags.MESSAGE, message);
                    params.put (AppConfigTags.USER_MOBILE, number);
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
            Utils.showSnackBar (EnquiryActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    /*Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);*/
                }
            });
        }
    }


    public void call_action() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:8527198118"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }

    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EnquiryActivity.this, MainActivityNew.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
