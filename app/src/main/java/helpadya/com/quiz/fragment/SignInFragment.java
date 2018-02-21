package helpadya.com.quiz.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import helpadya.com.quiz.R;
import helpadya.com.quiz.activity.MainActivity;
import helpadya.com.quiz.activity.MainActivityNew;
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

public class SignInFragment extends Fragment{
    CoordinatorLayout clMain;
    EditText etEmail;
    EditText etPassword;
    TextView tvSignIn;
    ProgressDialog progressDialog;
    View rootView;
    TextView tvSignUp;
    TextView tvForgotPassword;
    TextView tvSkipSignUp;
    UserDetailsPref userDetailsPref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initView(rootView);
        initData();
        initListener();
        return rootView;
    }

    private void initData() {
        progressDialog = new ProgressDialog (getActivity ());
        userDetailsPref = UserDetailsPref.getInstance();

    }

    private void initListener() {
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogForgotPassword();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm =  getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                SignUpFragment signUpFragment = new SignUpFragment();
                fragmentTransaction.replace(R.id.frameLayout, signUpFragment);
                fragmentTransaction.commit();
            }
        });

        tvSkipSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(getActivity(), MainActivity.class);
                startActivity(mainActivity);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyboard (getActivity ());
                SpannableString s1 = new SpannableString (getResources ().getString (R.string.please_enter_email));
                s1.setSpan (new TypefaceSpan(getActivity (), Constants.font_name), 0, s1.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString (getResources ().getString (R.string.please_enter_valid_email));
                s2.setSpan (new TypefaceSpan(getActivity (), Constants.font_name), 0, s2.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s7 = new SpannableString (getResources ().getString (R.string.please_enter_password));
                if (etEmail.getText ().toString ().trim ().length () == 0) {
                    etEmail.setError (s1);
                } else if (! Utils.isValidEmail1 (etEmail.getText ().toString ())) {
                    etEmail.setError (s2);
                } else if (etPassword.getText ().toString ().trim ().length () == 0) {
                    etPassword.setError (s7);
                }else{
                    sendLoginCredentialsToServer (etEmail.getText ().toString ().trim (), etPassword.getText ().toString ().trim ());
                    /*if(etEmail.getText().toString().trim().equalsIgnoreCase("Sudhanshusharma@gmail.com")  && etPassword.getText().toString().trim().equalsIgnoreCase("123456")) {
                        userDetailsPref.putStringPref(getActivity(),UserDetailsPref.USER_EMAIL,"Sudhanshusharma@gmail.com");
                        Intent mainActivity = new Intent(getActivity(), MainActivity.class);
                        startActivity(mainActivity);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    */
                  /*  }else{
                        Utils.showSnackBar (getActivity (), clMain, "Invalid Username Or Password", Snackbar.LENGTH_LONG, "", null);
                    }*/
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

    private void dialogForgotPassword(){
        MaterialDialog builder = new MaterialDialog.Builder(getActivity())
                .title("Forgot Password")
                .widgetColor(getResources().getColor(R.color.primary))
                //.inputMaxLength(30,R.color.material_blue_grey_950)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("Enter Email Address", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {

                        getForgotPassword(input.toString());
                    }
                }).negativeText("Cancel").show();
    }

    private void initView(View rootView) {
        clMain            = (CoordinatorLayout) rootView.findViewById(R.id.clMain);
        tvSignIn          = (TextView) rootView.findViewById (R.id.tvSignIn);
        etEmail           = (EditText) rootView.findViewById(R.id.etEmail);
        etPassword        = (EditText) rootView.findViewById(R.id.etPassword);
        tvSignUp          = (TextView) rootView.findViewById(R.id.tvSignUp);
        tvSkipSignUp      = (TextView) rootView.findViewById(R.id.tvSkipSignUp);
        tvForgotPassword  = (TextView) rootView.findViewById(R.id.tvForgotPassword);
    }

    private void sendLoginCredentialsToServer (final String email, final String password) {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SIGN_IN, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_SIGN_IN,
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
                                        userDetailsPref.putStringPref (getActivity (), UserDetailsPref.PROFILE, jsonObj.getString (AppConfigTags.PROFILE));
                                        //Intent intentMyDashboard = new Intent(getActivity(), MyDashboardActivity.class);
                                        Intent intentMyDashboard = new Intent(getActivity(), MainActivityNew.class);
                                        startActivity(intentMyDashboard);
                                        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.USER_EMAIL, email);
                    params.put (AppConfigTags.USER_PASSWORD, password);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
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

    private void getForgotPassword (final String email) {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_FORGOT_PASSWORD, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_FORGOT_PASSWORD,
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
                                        Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
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
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.USER_EMAIL, email);
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
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
