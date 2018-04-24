package helpadya.com.quiz.fragment;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import java.util.Map;
import java.util.regex.Pattern;

import helpadya.com.quiz.R;
import helpadya.com.quiz.activity.MainActivityNew;
import helpadya.com.quiz.utils.AppConfigTags;
import helpadya.com.quiz.utils.AppConfigURL;
import helpadya.com.quiz.utils.Constants;
import helpadya.com.quiz.utils.NetworkConnection;
import helpadya.com.quiz.utils.SetTypeFace;
import helpadya.com.quiz.utils.TypefaceSpan;
import helpadya.com.quiz.utils.UserDetailsPref;
import helpadya.com.quiz.utils.Utils;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by actiknow on 9/14/17.
 */

public class SignUpFragment extends Fragment{
    CoordinatorLayout clMain;
    EditText etName;
    EditText etEmail;
    EditText etPhone;
    EditText etPassword;
    EditText etConfirmPassword;
    TextView tvSignUp;
    ProgressDialog progressDialog;
    View rootView;
    TextView tvLogin;
    UserDetailsPref userDetailsPref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initView(rootView);
        initData();
        initListener();
        showAutoFillDialog ();
        return rootView;
    }

    private void initData() {
        userDetailsPref = UserDetailsPref.getInstance();
        progressDialog = new ProgressDialog (getActivity ());
    }

    private void initListener() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                SignInFragment signInFragment = new SignInFragment();
                fragmentTransaction.replace(R.id.frameLayout, signInFragment);
                fragmentTransaction.commit(); // save the changes
            }
        });


        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyboard (getActivity ());
                SpannableString s = new SpannableString (getResources ().getString (R.string.please_enter_name));
                s.setSpan (new TypefaceSpan(getActivity (), Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString (getResources ().getString (R.string.please_enter_email));
                s2.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s2.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s3 = new SpannableString (getResources ().getString (R.string.please_enter_mobile));
                s3.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s3.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s4 = new SpannableString (getResources ().getString (R.string.please_enter_valid_email));
                s4.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s4.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s5 = new SpannableString (getResources ().getString (R.string.please_enter_valid_mobile));
                s5.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s5.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s6 = new SpannableString (getResources ().getString (R.string.please_enter_valid_email));
                s6.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s6.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s7 = new SpannableString (getResources ().getString (R.string.please_enter_password));
                s7.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s3.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s8 = new SpannableString (getResources ().getString (R.string.please_enter_confirm_password));
                s8.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s3.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s9 = new SpannableString ("Your Password and Confirm Password does not matched");
                s8.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s3.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
                } else if (etPassword.getText ().toString ().trim ().length () == 0) {
                    etPassword.setError (s7);
                } else if (etConfirmPassword.getText ().toString ().trim ().length () == 0) {
                    etConfirmPassword.setError (s8);
                }else if (!etPassword.getText ().toString ().equalsIgnoreCase(etConfirmPassword.getText ().toString ())) {
                    Log.e("PASSWORD",etPassword.getText ().toString () + " CONFIRM PASSWORD  " +  etConfirmPassword.getText ().toString ());
                    Utils.showSnackBar (getActivity (), clMain, "Your Password and Confirm Password does not matched", Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                }else{
                    sendSignUpDetailsToServer (etName.getText ().toString ().trim (), etEmail.getText ().toString ().trim (), etPhone.getText ().toString ().trim (), etPassword.getText().toString().trim());
                }
            }
        });

        etEmail.addTextChangedListener (new TextWatcher() {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etEmail.setError (null);
                }
            }

            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged (Editable s) {
            }
        });

        etPassword.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etPassword.setError (null);
                }
            }

            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged (Editable s) {
            }
        });

    }

    private void showAutoFillDialog () {
        MaterialDialog dialog = new MaterialDialog.Builder (getActivity())
                .content (R.string.dialog_text_auto_fill)
                .positiveColor (getResources ().getColor (R.color.app_text_color_dark))
                .contentColor (getResources ().getColor (R.color.app_text_color_dark))
                .negativeColor (getResources ().getColor (R.color.app_text_color_dark))
                .typeface (SetTypeFace.getTypeface (getActivity()), SetTypeFace.getTypeface (getActivity()))
                .canceledOnTouchOutside (false)
                .cancelable (false)
                .positiveText (R.string.dialog_action_yes)
                .negativeText (R.string.dialog_action_no)
                .onPositive (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        try {
                            ContentResolver cr = getActivity().getContentResolver ();
                            Cursor cursor = cr.query (ContactsContract.Profile.CONTENT_URI, null, null, null, null);
                            if (cursor.getCount () > 0) {
                                cursor.moveToFirst ();
                                etName.setText (cursor.getString (cursor.getColumnIndex (ContactsContract.Profile.DISPLAY_NAME)));
                            }
                            cursor.close ();
                            Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
                            if (ActivityCompat.checkSelfPermission (getActivity(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            Account[] accounts = AccountManager.get (getActivity()).getAccounts ();
                            for (Account account : accounts) {
                                if (emailPattern.matcher (account.name).matches ()) {
                                    etEmail.setText (account.name);
                                }
                            }
                            TelephonyManager tm = (TelephonyManager) getActivity().getSystemService (TELEPHONY_SERVICE);

                            etPhone.setText (tm.getLine1Number ());
                        } catch (Exception e) {
                            e.printStackTrace ();
                        }

                    }
                }).build ();
        dialog.show ();
    }

    private void initView(View rootView) {
        clMain            = (CoordinatorLayout) rootView.findViewById(R.id.clMain);
        tvLogin           = (TextView) rootView.findViewById (R.id.tvLogin);
        etName            = (EditText) rootView.findViewById(R.id.etName);
        etEmail           = (EditText) rootView.findViewById(R.id.etEmail);
        etPhone           = (EditText) rootView.findViewById(R.id.etPhone);
        etPassword        = (EditText) rootView.findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) rootView.findViewById(R.id.etConfirmPassword);
        tvSignUp          = (TextView) rootView.findViewById(R.id.tvSignUp);
    }

    private void sendSignUpDetailsToServer (final String name, final String email, final String number,final String password) {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SIGN_UP, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_SIGN_UP,
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
                                        userDetailsPref.putIntPref (getActivity (), UserDetailsPref.USER_ID, jsonObj.getInt (AppConfigTags.ID));
                                        userDetailsPref.putStringPref (getActivity (), UserDetailsPref.USER_NAME, jsonObj.getString (AppConfigTags.NAME));
                                        userDetailsPref.putStringPref (getActivity (), UserDetailsPref.USER_EMAIL, jsonObj.getString (AppConfigTags.EMAIL));
                                        userDetailsPref.putStringPref (getActivity (), UserDetailsPref.USER_MOBILE, jsonObj.getString (AppConfigTags.MOBILE));
                                        userDetailsPref.putStringPref (getActivity (), UserDetailsPref.DOB, jsonObj.getString (AppConfigTags.DOB));
                                        userDetailsPref.putStringPref (getActivity (), UserDetailsPref.GENDER, jsonObj.getString (AppConfigTags.GENDER));
                                        userDetailsPref.putIntPref (getActivity (), UserDetailsPref.USER_TOTAL_AMOUNT, jsonObj.getInt (AppConfigTags.SCORE));
                                        userDetailsPref.putStringPref (getActivity (), UserDetailsPref.PROFILE, jsonObj.getString (AppConfigTags.PROFILE));
                                        userDetailsPref.putIntPref (getActivity (), UserDetailsPref.DAY_OF_YEAR, 0);
                                        Intent intent = new Intent (getActivity (), MainActivityNew.class);
                                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity (intent);
                                        getActivity ().overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else {
                                        Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
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
                            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put (AppConfigTags.USER_NAME, name);
                    params.put (AppConfigTags.USER_EMAIL, email);
                    params.put (AppConfigTags.USER_PASSWORD, password);
                    params.put (AppConfigTags.USER_MOBILE, number);
                    params.put (AppConfigTags.USER_FIREBASE_ID, userDetailsPref.getStringPref(getActivity(),UserDetailsPref.USER_FIREBASE_ID));
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
            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
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
