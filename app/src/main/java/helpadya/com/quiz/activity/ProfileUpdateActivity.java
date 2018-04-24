package helpadya.com.quiz.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import helpadya.com.quiz.R;
import helpadya.com.quiz.utils.AppConfigTags;
import helpadya.com.quiz.utils.AppConfigURL;
import helpadya.com.quiz.utils.Constants;
import helpadya.com.quiz.utils.NetworkConnection;
import helpadya.com.quiz.utils.TypefaceSpan;
import helpadya.com.quiz.utils.UserDetailsPref;
import helpadya.com.quiz.utils.Utils;

/**
 * Created by actiknow on 9/14/17.
 */

public class ProfileUpdateActivity extends AppCompatActivity{
    CoordinatorLayout clMain;
    EditText etName;
    EditText etEmail;
    EditText etPhone;
    TextView tvDob;
    TextView tvUpdate;
    ImageView ivProfile;
    RadioGroup rgGender;
    ProgressDialog progressDialog;
    UserDetailsPref userDetailsPref;
    String imageURI;
    String encodedImage;
    Uri selectedImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        userDetailsPref = UserDetailsPref.getInstance();
        progressDialog = new ProgressDialog(this);
        etName.setText(userDetailsPref.getStringPref(ProfileUpdateActivity.this, UserDetailsPref.USER_NAME));
        etEmail.setText(userDetailsPref.getStringPref(ProfileUpdateActivity.this, UserDetailsPref.USER_EMAIL));
        etPhone.setText(userDetailsPref.getStringPref(ProfileUpdateActivity.this, UserDetailsPref.USER_MOBILE));
        if (userDetailsPref.getStringPref(ProfileUpdateActivity.this, UserDetailsPref.DOB).length() > 0) {
            tvDob.setText(userDetailsPref.getStringPref(ProfileUpdateActivity.this, UserDetailsPref.DOB));
        }
        if (userDetailsPref.getStringPref(ProfileUpdateActivity.this, UserDetailsPref.GENDER).equalsIgnoreCase("male")) {
            rgGender.check(R.id.rbMale);
        } else if (userDetailsPref.getStringPref(ProfileUpdateActivity.this, UserDetailsPref.GENDER).equalsIgnoreCase("female")) {
            rgGender.check(R.id.rbFemale);
        }
        if(userDetailsPref.getStringPref(ProfileUpdateActivity.this, UserDetailsPref.PROFILE).length() > 0){
            Glide.with (ProfileUpdateActivity.this)
                    .load (userDetailsPref.getStringPref(ProfileUpdateActivity.this, UserDetailsPref.PROFILE))
                    .listener (new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException (Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                           // progressBar.setVisibility (View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady (GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                          //  progressBar.setVisibility (View.GONE);
                            return false;
                        }
                    })
                    .into (ivProfile);
        }


    }

    private void initListener() {

        tvDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endDate(tvDob);
            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });


        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyboard (ProfileUpdateActivity.this);
                int selected=rgGender.getCheckedRadioButtonId();
                RadioButton gender=(RadioButton) findViewById(selected);
                SpannableString s = new SpannableString (getResources ().getString (R.string.please_enter_name));
                s.setSpan (new TypefaceSpan(ProfileUpdateActivity.this, Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString (getResources ().getString (R.string.please_enter_email));
                s2.setSpan (new TypefaceSpan (ProfileUpdateActivity.this, Constants.font_name), 0, s2.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s3 = new SpannableString (getResources ().getString (R.string.please_enter_mobile));
                s3.setSpan (new TypefaceSpan (ProfileUpdateActivity.this, Constants.font_name), 0, s3.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s4 = new SpannableString (getResources ().getString (R.string.please_enter_valid_email));
                s4.setSpan (new TypefaceSpan (ProfileUpdateActivity.this, Constants.font_name), 0, s4.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s5 = new SpannableString (getResources ().getString (R.string.please_enter_valid_mobile));
                s5.setSpan (new TypefaceSpan (ProfileUpdateActivity.this, Constants.font_name), 0, s5.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s6 = new SpannableString (getResources ().getString (R.string.please_enter_valid_email));
                s6.setSpan (new TypefaceSpan (ProfileUpdateActivity.this, Constants.font_name), 0, s6.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s7 = new SpannableString (getResources ().getString (R.string.please_enter_password));
                s7.setSpan (new TypefaceSpan (ProfileUpdateActivity.this, Constants.font_name), 0, s3.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s8 = new SpannableString (getResources ().getString (R.string.please_enter_confirm_password));
                s8.setSpan (new TypefaceSpan (ProfileUpdateActivity.this, Constants.font_name), 0, s3.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s9 = new SpannableString ("Your Password and Confirm Password does not matched");
                s8.setSpan (new TypefaceSpan (ProfileUpdateActivity.this, Constants.font_name), 0, s3.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (etName.getText ().toString ().trim ().length () == 0 && etEmail.getText ().toString ().length () == 0 && etPhone.getText ().toString ().length () == 0) {
                    etName.setError (s);
                    etEmail.setError (s2);
                    etPhone.setError (s3);
                } else if (etName.getText ().toString ().trim ().length () == 0) {
                    etName.setError (s);
                } else if (etEmail.getText ().toString ().trim ().length () == 0) {
                    etEmail.setError (s2);
                } else if (! Utils.isValidEmail1 (etEmail.getText ().toString ())) {
                    etEmail.setError (s6);
                } else if (etPhone.getText ().toString ().trim ().length () == 0) {
                    etPhone.setError (s3);
                } else{
                    sendUpdateDetailsToServer (etName.getText ().toString ().trim (), etEmail.getText ().toString ().trim (), etPhone.getText ().toString ().trim (), tvDob.getText().toString().trim(), gender.getText().toString().trim());
                }
            }
        });


    }

    private void endDate(final TextView etDob) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                etDob.setText(String.format("%02d", dayOfMonth) + "-" + String.format("%02d", monthOfYear + 1) + "-" + year);
                String date = etDob.getText().toString().trim();
                Log.e("date", date);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void initView() {
        clMain            = (CoordinatorLayout) findViewById(R.id.clMain);
        etName            = (EditText) findViewById(R.id.etName);
        etEmail           = (EditText) findViewById(R.id.etEmail);
        etPhone           = (EditText) findViewById(R.id.etPhone);
        tvDob             = (TextView) findViewById(R.id.tvDob);
        rgGender          = (RadioGroup) findViewById(R.id.rgGender);
        ivProfile         = (ImageView) findViewById(R.id.ivProfile);
        tvUpdate          = (TextView) findViewById(R.id.tvUpdate);
    }

    private void sendUpdateDetailsToServer (final String name, final String email, final String number, final String dob, final String gender) {
        if (NetworkConnection.isNetworkAvailable (ProfileUpdateActivity.this)) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_UPDATE_PROFILE, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_UPDATE_PROFILE,
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
                                        userDetailsPref.putIntPref (ProfileUpdateActivity.this, UserDetailsPref.USER_ID, jsonObj.getInt (AppConfigTags.ID));
                                        userDetailsPref.putStringPref (ProfileUpdateActivity.this, UserDetailsPref.USER_NAME, jsonObj.getString (AppConfigTags.NAME));
                                        userDetailsPref.putStringPref (ProfileUpdateActivity.this, UserDetailsPref.USER_EMAIL, jsonObj.getString (AppConfigTags.EMAIL));
                                        userDetailsPref.putStringPref (ProfileUpdateActivity.this, UserDetailsPref.USER_MOBILE, jsonObj.getString (AppConfigTags.MOBILE));
                                        userDetailsPref.putStringPref (ProfileUpdateActivity.this, UserDetailsPref.DOB, jsonObj.getString (AppConfigTags.DOB));
                                        userDetailsPref.putStringPref (ProfileUpdateActivity.this, UserDetailsPref.GENDER, jsonObj.getString (AppConfigTags.GENDER));
                                        userDetailsPref.putStringPref (ProfileUpdateActivity.this, UserDetailsPref.PROFILE, jsonObj.getString (AppConfigTags.PROFILE));
                                        Intent intent = new Intent (ProfileUpdateActivity.this, MainActivityNew.class);
                                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity (intent);
                                        ProfileUpdateActivity.this.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else {
                                        Utils.showSnackBar (ProfileUpdateActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (ProfileUpdateActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (ProfileUpdateActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (ProfileUpdateActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put(AppConfigTags.USER_ID, String.valueOf(userDetailsPref.getIntPref(ProfileUpdateActivity.this, UserDetailsPref.USER_ID)));
                    params.put (AppConfigTags.USER_NAME, name);
                    params.put (AppConfigTags.USER_EMAIL, email);
                    params.put (AppConfigTags.USER_MOBILE, number);
                    params.put(AppConfigTags.PROFILE, String.valueOf(encodedImage));
                    params.put (AppConfigTags.DOB, dob);
                    params.put (AppConfigTags.GENDER, gender);

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
            Utils.showSnackBar (ProfileUpdateActivity.this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                selectedImageUri = data.getData();
                ivProfile.setImageURI(selectedImageUri);

            }
        }

        if(selectedImageUri != null) {
            Cursor cursor = getContentResolver().query(selectedImageUri,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imageURI = cursor.getString(columnIndex);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            Log.e("encodedImage", encodedImage);
            cursor.close();
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileUpdateActivity.this, MainActivityNew.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
