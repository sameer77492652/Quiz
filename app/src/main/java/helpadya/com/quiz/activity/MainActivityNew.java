package helpadya.com.quiz.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import az.plainpie.PieView;
import helpadya.com.quiz.R;
import helpadya.com.quiz.model.Banner;
import helpadya.com.quiz.model.Option;
import helpadya.com.quiz.model.OptionNew;
import helpadya.com.quiz.model.QuestionNew;
import helpadya.com.quiz.model.ScoreList;
import helpadya.com.quiz.utils.AppConfigTags;
import helpadya.com.quiz.utils.AppConfigURL;
import helpadya.com.quiz.utils.NetworkConnection;
import helpadya.com.quiz.utils.SetTypeFace;
import helpadya.com.quiz.utils.UserDetailsPref;
import helpadya.com.quiz.utils.Utils;

public class MainActivityNew extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    ArrayList<Banner> bannerArrayList = new ArrayList<Banner>();
    private SliderLayout slider;
    Bundle savedInstanceState;
    ArrayList<QuestionNew> questionListNew = new ArrayList<>();
    public static ArrayList<ScoreList> scoreList = new ArrayList<>();
    TextView tvQues;
    TextView tvOptionA;
    TextView tvOptionB;
    TextView tvOptionC;
    TextView tvOptionD;
    TextView tvTimer;
    TextView tvTitle;
    TextView tvScore;
    TextView tvQuestionPoint;
    ImageView ivNavigation;
    PieView pieView;
    Option optionone;
    Calendar calendar;
    OptionNew optionNew;
    RelativeLayout rlQuiz;
    RelativeLayout rlStartQuiz;
    RelativeLayout rlQuizComplete;
    TextView tvStartQuiz;
    TextView tvHindi;
    TextView tvEnglish;
    int language_id = 0;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    String correctvalue;
    int k = 0;
    int count = 0;
    TextView tvStartQuizHindi;
    TextView tvSkipQuestion;
    TextView tvQuiz;
    JSONArray jsonArrayFaq;
    ProgressDialog progressDialog;
    CoordinatorLayout clMain;
    UserDetailsPref userDetailsPref;
    CountDownTimer yourCountDownTimer;
    String english = "en";
    String hindi = "hi";
    public static boolean status = false;
    int today = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        isLogin();
        initSlider();
        initListener();
        initDrawer();
    }

    private void initView() {
        clMain = (CoordinatorLayout) findViewById(R.id.clMain);
        ivNavigation = (ImageView) findViewById(R.id.ivNavigation);
        tvSkipQuestion = (TextView) findViewById(R.id.tvSkipQuestion);
        tvQues = (TextView) findViewById(R.id.tvQues);
        tvScore = (TextView) findViewById(R.id.tvScore);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        tvEnglish = (TextView) findViewById(R.id.tvEnglish);
        tvHindi = (TextView) findViewById(R.id.tvHindi);
        tvStartQuizHindi = (TextView) findViewById(R.id.tvStartQuizHindi);
        tvQuestionPoint = (TextView) findViewById(R.id.tvQuestionPoint);
        tvOptionA = (TextView) findViewById(R.id.tvOptionA);
        tvOptionB = (TextView) findViewById(R.id.tvOptionB);
        tvOptionC = (TextView) findViewById(R.id.tvOptionC);
        tvOptionD = (TextView) findViewById(R.id.tvOptionD);
        rlQuiz = (RelativeLayout) findViewById(R.id.rlQuiz);
        rlStartQuiz = (RelativeLayout) findViewById(R.id.rlStartQuiz);
        rlQuizComplete = (RelativeLayout) findViewById(R.id.rlQuizComplete);
        tvStartQuiz = (TextView) findViewById(R.id.tvStartQuiz);
        tvQuiz = (TextView) findViewById(R.id.tvQuiz);
        slider = (SliderLayout) findViewById(R.id.slider);
        pieView = (PieView) findViewById(R.id.pieView);
    }

    private void initListener() {
        tvStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userDetailsPref.getIntPref(MainActivityNew.this, UserDetailsPref.DAY_OF_YEAR) == 0){
                    userDetailsPref.putIntPref(MainActivityNew.this, UserDetailsPref.DAY_OF_YEAR, today);
                }
                language_id = 1;
                getQuestionlistNew();
                Configuration config = getBaseContext().getResources().getConfiguration();
                Locale locale = new Locale(english);
                Locale.setDefault(locale);
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                tvQuiz.setText(getResources().getText(R.string.activity_do_you_know));
                tvSkipQuestion.setText(getResources().getText(R.string.activity_skip));
                rlStartQuiz.setVisibility(View.GONE);
                rlQuiz.setVisibility(View.VISIBLE);
            }
        });
        tvStartQuizHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                language_id = 2;
                getQuestionlistNew();
                Configuration config = getBaseContext().getResources().getConfiguration();
                Locale locale = new Locale(hindi);
                Locale.setDefault(locale);
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                tvQuiz.setText(getResources().getText(R.string.activity_do_you_know));
                tvSkipQuestion.setText(getResources().getText(R.string.activity_skip));
                rlStartQuiz.setVisibility(View.GONE);
                rlQuiz.setVisibility(View.VISIBLE);
                //questionListOption();
            }
        });
        ivNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.openDrawer();
            }
        });
        tvSkipQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count == (questionListNew.size()) - 1) {
                    yourCountDownTimer.cancel();
                    Intent intent = new Intent(MainActivityNew.this, ThankuActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    if (k > (questionListNew.size()) - 1) {
                        k = (questionListNew.size()) + count;
                    } else {
                        k = count;
                    }
                    k++;
                    count++;
                    questionListOption();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });
        tvEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Configuration config = getBaseContext().getResources().getConfiguration();
                Locale locale = new Locale(english);
                Locale.setDefault(locale);
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                userDetailsPref.putStringPref(MainActivityNew.this, UserDetailsPref.LANGUAGE_TYPE, english);
                tvQuiz.setText(getResources().getText(R.string.activity_do_you_know));
                tvSkipQuestion.setText(getResources().getText(R.string.activity_skip));
                tvEnglish.setBackgroundColor(getResources().getColor(R.color.mb_green_new));
                tvHindi.setBackgroundColor(getResources().getColor(R.color.text_color_red));
                if (count == (questionListNew.size()) - 1) {
                    yourCountDownTimer.cancel();
                    Intent intent = new Intent(MainActivityNew.this, ThankuActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    k = count;
                    //k++;
                    //count++;
                    questionListOption();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });
    }

    private void isLogin() {
        if (userDetailsPref.getStringPref(MainActivityNew.this, UserDetailsPref.USER_EMAIL).equalsIgnoreCase("")) {
            Intent intent = new Intent(MainActivityNew.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (userDetailsPref.getStringPref(MainActivityNew.this, UserDetailsPref.USER_EMAIL).equalsIgnoreCase(""))
            finish();
    }

    private void initData(){
        calendar = Calendar.getInstance();
        userDetailsPref = new UserDetailsPref();
        progressDialog = new ProgressDialog(this);
        today = calendar.get(Calendar.DAY_OF_YEAR);
        if(today != userDetailsPref.getIntPref(MainActivityNew.this, UserDetailsPref.DAY_OF_YEAR)){
            status = false;
        }
        if(status){
            rlStartQuiz.setVisibility(View.GONE);
            rlQuizComplete.setVisibility(View.VISIBLE);
        }else{
            rlStartQuiz.setVisibility(View.VISIBLE);
            rlQuizComplete.setVisibility(View.GONE);
        }

        userDetailsPref.putStringPref(MainActivityNew.this, UserDetailsPref.LANGUAGE_TYPE, english);
        tvTitle.setText(userDetailsPref.getStringPref(MainActivityNew.this, UserDetailsPref.USER_NAME));
        tvScore.setText("Earned Score: " + userDetailsPref.getIntPref(MainActivityNew.this, UserDetailsPref.USER_TOTAL_AMOUNT));
        pieView.setPercentageBackgroundColor(getResources().getColor(R.color.pie_color_good));
        pieView.setInnerBackgroundColor(getResources().getColor(R.color.primary));
        pieView.setTextColor(getResources().getColor(R.color.text_color_white));
        bannerArrayList.add(new Banner(1, "Banner", "Image", "https://helpadya.com/img/851491210277banner%202.jpg", "1"));
        bannerArrayList.add(new Banner(2, "Banner", "Image", "https://helpadya.com/img/4091491211282BANNER2.jpg", "2"));
        bannerArrayList.add(new Banner(3, "Banner", "Image", "https://helpadya.com/img/3391491211311banner%204.jpg", "3"));
        bannerArrayList.add(new Banner(4, "Banner", "Image", "https://helpadya.com/img/4951491211418Homepage-Banner.jpg", "4"));
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    private void initSlider() {
//        slider.removeAllSliders ();
        for (int i = 0; i < bannerArrayList.size(); i++) {
            Banner banner = bannerArrayList.get(i);
            SpannableString s = new SpannableString(banner.getTitle());
            //s.setSpan (new TypefaceSpan(MainActivity.this, Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            DefaultSliderView defaultSliderView = new DefaultSliderView(this);
            defaultSliderView
                    .image(banner.getUrl())
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);


            defaultSliderView.bundle(new Bundle());
            defaultSliderView.getBundle().putString("url", banner.getUrl());
            slider.addSlider(defaultSliderView);
        }

        slider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);
        slider.setPresetTransformer(SliderLayout.Transformer.Default);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(5000);
        slider.addOnPageChangeListener(this);
        slider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content(R.string.dialog_text_quit_application)
                .positiveColor(getResources().getColor(R.color.primary_text2))
                .contentColor(getResources().getColor(R.color.primary_text2))
                .negativeColor(getResources().getColor(R.color.primary_text2))
                .canceledOnTouchOutside(true)
                .cancelable(true)
                .positiveText(R.string.dialog_action_yes)
                .negativeText(R.string.dialog_action_no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }).build();
        dialog.show();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (yourCountDownTimer != null) {
            yourCountDownTimer.cancel();
        }
        Log.e("onPause", "onPause");
        // System.runFinalizersOnExit(true);
        //System.exit(0);
//        android.os.Process.killProcess(android.os.Process.myPid());
        //this.finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (yourCountDownTimer != null) {
            yourCountDownTimer.cancel();
        }
        finish();
    }

    private void getQuestionlistNew() {
        if (NetworkConnection.isNetworkAvailable(MainActivityNew.this)) {
            Utils.showProgressDialog(progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_QUESTION_NEW+ "/" + language_id, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.GET, AppConfigURL.URL_QUESTION_NEW+ "/" + language_id,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utils.showLog(Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            userDetailsPref.putStringPref(MainActivityNew.this, UserDetailsPref.RESPONSE
                            ,response);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean error = jsonObj.getBoolean(AppConfigTags.ERROR);
                                    String message = jsonObj.getString(AppConfigTags.MESSAGE);
                                    if (!error) {
                                        JSONArray jsonArrayQuestion = jsonObj.getJSONArray(AppConfigTags.QUESTION);
                                        for (int i = 0; i < jsonArrayQuestion.length(); i++) {
                                            JSONObject jsonObjQuestion = jsonArrayQuestion.getJSONObject(i);
                                            QuestionNew questionNew = new QuestionNew();
                                            questionNew.setId(jsonObjQuestion.getInt(AppConfigTags.ID));
                                            questionNew.setQuestion_english(new String(jsonObjQuestion.getString(AppConfigTags.QUESTION_ENGLISH).getBytes("ISO-8859-1"), "UTF-8"));
                                            //questionNew.setQuestion_english(jsonObjQuestion.getString(AppConfigTags.QUESTION_ENGLISH));
                                            questionNew.setQuestion_hindi("");
                                            questionNew.setCorrect_id(jsonObjQuestion.getInt(AppConfigTags.CORRECT_ID));
                                            questionNew.setPoints(jsonObjQuestion.getString("points"));

                                            JSONArray jsonArrayOptionNew = jsonObjQuestion.getJSONArray(AppConfigTags.OPTION);
                                            for(int j=0; j < jsonArrayOptionNew.length(); j++){
                                                JSONObject jsonObjectOption = jsonArrayOptionNew.getJSONObject(j);
                                                OptionNew optionNew = new OptionNew();
                                                optionNew.setId(jsonObjectOption.getInt(AppConfigTags.ID));
                                                optionNew.setQuestion_id(jsonObjectOption.getInt(AppConfigTags.QUESTION_ID));
                                                //optionNew.setOption(jsonObjectOption.getString(AppConfigTags.OPTION));
                                                optionNew.setOption(new String(jsonObjectOption.getString(AppConfigTags.OPTION).getBytes("ISO-8859-1"), "UTF-8"));
                                                questionNew.addQuestionOptionNew(optionNew);
                                            }
                                            questionListNew.add(questionNew);

                                        }
                                        scoreList.clear();
                                        JSONArray jsonArrayScoreList = jsonObj.getJSONArray(AppConfigTags.SCORELIST);
                                        for(int k = 0; k < jsonArrayScoreList.length(); k++){
                                            JSONObject jsonObjectScoreList = jsonArrayScoreList.getJSONObject(k);
                                            scoreList.add(new ScoreList(jsonObjectScoreList.getInt(AppConfigTags.ID),
                                                            jsonObjectScoreList.getInt(AppConfigTags.USER_SCORE),
                                                            jsonObjectScoreList.getString(AppConfigTags.USER_NAME)
                                                    )
                                            );
                                        }
                                        questionListOption();
                                    } else {
                                        Utils.showSnackBar(MainActivityNew.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showSnackBar(MainActivityNew.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(MainActivityNew.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                Utils.showLog(Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss();
                            //swipeRefreshLayout.setRefreshing (false);
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // swipeRefreshLayout.setRefreshing (false);
                            progressDialog.dismiss();
                            Utils.showLog(Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString(), true);
                            Utils.showSnackBar(MainActivityNew.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                        }
                    }) {

            };
            Utils.sendRequest(strRequest1, 60);
        } else {
            Utils.showSnackBar(this, clMain, getResources().getString(R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_go_to_settings), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
            });
        }
    }

    private void questionListOption() {
        tvScore.setText("Earned Score: " + userDetailsPref.getIntPref(MainActivityNew.this, UserDetailsPref.USER_TOTAL_AMOUNT));
        /*for(int i = 0; i < questionList.size(); i++){
            if(questionList.get(i).getLanguage_id() == 2) {
                Log.e("QUestion", questionList.get(i).getQuestion());
                for(int j = 0; j < questionList.get(i).getQuestionOptionList().size(); j++){
                    if(questionList.get(i).getQuestionOptionList().get(j).getLanguage_id() == 2) {
                        Log.e("Option", questionList.get(i).getQuestionOptionList().get(j).getOption());
                    }
                }
            }
        }*/

        /*for(int i = 0; i < questionListNew.size(); i++){
            Log.e("Question", questionListNew.get(i).getQuestion_english());
            for(int j = 0; j < questionListNew.get(i).getQuestionOptionListNew().size(); j++){
                Log.e("Option", questionListNew.get(i).getQuestionOptionListNew().get(j).getOption());
            }
        }*/
        for (int i = 0; i < 3; i++) {
            if (questionListNew.get(k).getQuestionOptionListNew().get(i).getId() == questionListNew.get(k).getCorrect_id()) {
                correctvalue = questionListNew.get(k).getQuestionOptionListNew().get(i).getOption();
                Log.e("correctvalue", correctvalue);
            }
        }
        final int j = 0;
        double amount = count + 1;
        double total_question = questionListNew.size();
        pieView.setInnerText(((amount / total_question) * 100.0f) + "%");
        pieView.setPercentageTextSize(Utils.pxFromDp(MainActivityNew.this, 8));
        pieView.setPercentage((float) ((amount / total_question) * 100.0f));
        tvOptionA.setBackground(getResources().getDrawable(R.drawable.rounded_corner));
        tvOptionB.setBackground(getResources().getDrawable(R.drawable.rounded_corner));
        tvOptionC.setBackground(getResources().getDrawable(R.drawable.rounded_corner));
        tvOptionD.setBackground(getResources().getDrawable(R.drawable.rounded_corner));
        tvQues.setText("Ques " + (count + 1) + ". " + questionListNew.get(k).getQuestion_english());
        tvQuestionPoint.setText(getResources().getText(R.string.activity_point_score) + " - " + questionListNew.get(k).getPoints());
        optionNew = questionListNew.get(k).getQuestionOptionListNew().get(j);
        tvOptionA.setText("a. " + questionListNew.get(k).getQuestionOptionListNew().get(j).getOption());
        tvOptionB.setText("b. " + questionListNew.get(k).getQuestionOptionListNew().get(j + 1).getOption());
        tvOptionC.setText("c. " + questionListNew.get(k).getQuestionOptionListNew().get(j + 2).getOption());
        tvOptionD.setText("d.   " + questionListNew.get(k).getQuestionOptionListNew().get(j + 3).getOption());
        tvOptionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionListNew.get(k).getQuestionOptionListNew().get(j).getId() == questionListNew.get(k).getCorrect_id()) {
                    tvOptionA.setBackground(getResources().getDrawable(R.drawable.rounded_corner_green));
                    tvOptionA.setPadding(10, 10, 10, 10);
                    if (userDetailsPref.getIntPref(MainActivityNew.this, UserDetailsPref.USER_TOTAL_AMOUNT) != 0) {
                        userDetailsPref.putIntPref(MainActivityNew.this, UserDetailsPref.USER_TOTAL_AMOUNT, userDetailsPref.getIntPref(MainActivityNew.this, UserDetailsPref.USER_TOTAL_AMOUNT) + Integer.parseInt(questionListNew.get(k).getPoints()));
                    } else {
                        userDetailsPref.putIntPref(MainActivityNew.this, UserDetailsPref.USER_TOTAL_AMOUNT, Integer.parseInt(questionListNew.get(k).getPoints()));
                    }
                    handle("" + getResources().getText(R.string.activity_correct_answer)+" "+ questionListNew.get(k).getPoints());
                } else {
                    tvOptionA.setBackground(getResources().getDrawable(R.drawable.rounded_corner_red));
                    tvOptionA.setPadding(10, 10, 10, 10);
                    handle("" + getResources().getText(R.string.activity_incorrect_answer) +" "+ correctvalue);
                }
            }
        });
        tvOptionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("question Option ID",""+questionListNew.get(k).getQuestionOptionListNew().get(j).getId());
                Log.e("question Correct ID",""+questionListNew.get(k).getCorrect_id());
                if (questionListNew.get(k).getQuestionOptionListNew().get(j+1).getId() == questionListNew.get(k).getCorrect_id()) {
                    tvOptionB.setBackground(getResources().getDrawable(R.drawable.rounded_corner_green));
                    tvOptionB.setPadding(10, 10, 10, 10);
                    if (userDetailsPref.getIntPref(MainActivityNew.this, UserDetailsPref.USER_TOTAL_AMOUNT) != 0) {
                        userDetailsPref.putIntPref(MainActivityNew.this, UserDetailsPref.USER_TOTAL_AMOUNT, userDetailsPref.getIntPref(MainActivityNew.this, UserDetailsPref.USER_TOTAL_AMOUNT) + Integer.parseInt(questionListNew.get(k).getPoints()));
                    } else {
                        userDetailsPref.putIntPref(MainActivityNew.this, UserDetailsPref.USER_TOTAL_AMOUNT, Integer.parseInt(questionListNew.get(k).getPoints()));
                    }
                    handle("" + getResources().getText(R.string.activity_correct_answer)+" "+questionListNew.get(k).getPoints());
                } else {
                    tvOptionB.setBackground(getResources().getDrawable(R.drawable.rounded_corner_red));
                    tvOptionB.setPadding(10, 10, 10, 10);
                    handle("" + getResources().getText(R.string.activity_incorrect_answer) +" "+ correctvalue);
                }
            }
        });
        tvOptionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("question Option ID",""+questionListNew.get(k).getQuestionOptionListNew().get(j).getId());
                Log.e("question Correct ID",""+questionListNew.get(k).getCorrect_id());
                if (questionListNew.get(k).getQuestionOptionListNew().get(j+2).getId() == questionListNew.get(k).getCorrect_id()) {
                    tvOptionC.setBackground(getResources().getDrawable(R.drawable.rounded_corner_green));
                    tvOptionC.setPadding(10, 10, 10, 10);
                    if (userDetailsPref.getIntPref(MainActivityNew.this, UserDetailsPref.USER_TOTAL_AMOUNT) != 0) {
                        userDetailsPref.putIntPref(MainActivityNew.this, UserDetailsPref.USER_TOTAL_AMOUNT, userDetailsPref.getIntPref(MainActivityNew.this, UserDetailsPref.USER_TOTAL_AMOUNT) + Integer.parseInt(questionListNew.get(k).getPoints()));
                    } else {
                        userDetailsPref.putIntPref(MainActivityNew.this, UserDetailsPref.USER_TOTAL_AMOUNT, Integer.parseInt(questionListNew.get(k).getPoints()));
                    }
                    handle("" + getResources().getText(R.string.activity_correct_answer)+" " +questionListNew.get(k).getPoints());
                } else {
                    tvOptionC.setBackground(getResources().getDrawable(R.drawable.rounded_corner_red));
                    tvOptionC.setPadding(10, 10, 10, 10);
                    handle("" + getResources().getText(R.string.activity_incorrect_answer) +" "+ correctvalue);
                }

            }
        });
        tvOptionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("question Option ID",""+questionListNew.get(k).getQuestionOptionListNew().get(j).getId());
                Log.e("question Correct ID",""+questionListNew.get(k).getCorrect_id());
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (questionListNew.get(k).getQuestionOptionListNew().get(j+3).getId() == questionListNew.get(k).getCorrect_id()) {
                    tvOptionD.setBackground(getResources().getDrawable(R.drawable.rounded_corner_green));
                    tvOptionD.setPadding(10, 10, 10, 10);
                    if (userDetailsPref.getIntPref(MainActivityNew.this, UserDetailsPref.USER_TOTAL_AMOUNT) != 0) {
                        userDetailsPref.putIntPref(MainActivityNew.this, UserDetailsPref.USER_TOTAL_AMOUNT, userDetailsPref.getIntPref(MainActivityNew.this, UserDetailsPref.USER_TOTAL_AMOUNT) + Integer.parseInt(questionListNew.get(k).getPoints()));
                    } else {
                        userDetailsPref.putIntPref(MainActivityNew.this, UserDetailsPref.USER_TOTAL_AMOUNT, Integer.parseInt(questionListNew.get(k).getPoints()));
                    }
                    handle("" + getResources().getText(R.string.activity_correct_answer)+" " + questionListNew.get(k).getPoints());
                } else {
                    tvOptionD.setBackground(getResources().getDrawable(R.drawable.rounded_corner_red));
                    tvOptionD.setPadding(10, 10, 10, 10);
                    handle("" + getResources().getText(R.string.activity_incorrect_answer) +" "+ correctvalue);
                }
            }
        });

        timer();

    }

    public void dialogMessage(String message) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content(message)
                .positiveColor(getResources().getColor(R.color.primary_text2))
                .contentColor(getResources().getColor(R.color.primary_text2))
                .negativeColor(getResources().getColor(R.color.primary_text2))
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .positiveText(R.string.dialog_action_yes)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //if(k == questionList.size() - 1){
                        if (count == questionListNew.size() - 1) {
                            yourCountDownTimer.cancel();
                            Intent intent = new Intent(MainActivityNew.this, ThankuActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        } else {
                            yourCountDownTimer.cancel();
                            k++;
                            count++;
                            questionListOption();
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        }
                    }
                }).build();
        dialog.show();
    }

    private void timer() {
        if (yourCountDownTimer != null) {
            yourCountDownTimer.cancel();
        }

        yourCountDownTimer = new CountDownTimer(30000, 1000) {                     //geriye sayma
            @Override
            public void onFinish() {
                //if(k == questionList.size() - 1){
                if (count == questionListNew.size() - 1) {
                    Intent intent = new Intent(MainActivityNew.this, ThankuActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    count++;
                    k++;
                    questionListOption();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {
                long sec = millisUntilFinished / 1000 + 1;
                tvTimer.setText("Timer: " + sec + " SEC");

            }

        };
        yourCountDownTimer.start();
    }

    private void handle(final String value) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialogMessage(value);
            }
        }, 100);
    }

    private void initDrawer() {
        IProfile profile = new IProfile() {
            @Override
            public Object withName(String name) {
                return null;
            }

            @Override
            public StringHolder getName() {
                return null;
            }

            @Override
            public Object withEmail(String email) {
                return null;
            }

            @Override
            public StringHolder getEmail() {
                return null;
            }

            @Override
            public Object withIcon(Drawable icon) {
                return null;
            }

            @Override
            public Object withIcon(Bitmap bitmap) {
                return null;
            }

            @Override
            public Object withIcon(@DrawableRes int iconRes) {
                return null;
            }

            @Override
            public Object withIcon(String url) {
                return null;
            }

            @Override
            public Object withIcon(Uri uri) {
                return null;
            }

            @Override
            public Object withIcon(IIcon icon) {
                return null;
            }

            @Override
            public ImageHolder getIcon() {
                return null;
            }

            @Override
            public Object withSelectable(boolean selectable) {
                return null;
            }

            @Override
            public boolean isSelectable() {
                return false;
            }

            @Override
            public Object withIdentifier(long identifier) {
                return null;
            }

            @Override
            public long getIdentifier() {
                return 0;
            }
        };

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                if (uri != null) {
                    Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
                }
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.clear(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.colorPrimary).sizeDp(56);
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_white_1000);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder(ctx, tag);
            }
        });
        if (userDetailsPref.getStringPref (MainActivityNew.this, UserDetailsPref.PROFILE).length () != 0) {
            headerResult = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withCompactStyle(false)
                    .withTypeface(SetTypeFace.getTypeface(MainActivityNew.this))
                    .withTypeface(SetTypeFace.getTypeface(this))
                    .withPaddingBelowHeader(false)
                    .withSelectionListEnabled(false)
                    .withSelectionListEnabledForSingleProfile(false)
                    .withProfileImagesVisible(true)
                    .withOnlyMainProfileImageVisible(true)
                    .withDividerBelowHeader(true)
                    .withHeaderBackground(R.color.primary_dark)
                    .withSavedInstance(savedInstanceState)
                    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                        @Override
                        public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                            //Intent intent = new Intent (MainActivity.this, MyProfileActivity.class);
                            //startActivity (intent);
                            return false;
                        }
                    })
                    .build();
            headerResult.addProfiles(new ProfileDrawerItem()
                    .withIcon(userDetailsPref.getStringPref(MainActivityNew.this, UserDetailsPref.PROFILE))
                    .withName(userDetailsPref.getStringPref(MainActivityNew.this, UserDetailsPref.USER_NAME))
                    .withEmail(userDetailsPref.getStringPref(MainActivityNew.this, UserDetailsPref.USER_EMAIL)));
        }else{
            headerResult = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withCompactStyle(false)
                    .withTypeface(SetTypeFace.getTypeface(MainActivityNew.this))
                    .withTypeface(SetTypeFace.getTypeface(this))
                    .withPaddingBelowHeader(false)
                    .withSelectionListEnabled(false)
                    .withSelectionListEnabledForSingleProfile(false)
                    .withProfileImagesVisible(false)
                    .withOnlyMainProfileImageVisible(false)
                    .withDividerBelowHeader(true)
                    .withHeaderBackground(R.color.primary_dark)
                    .withSavedInstance(savedInstanceState)
                    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                        @Override
                        public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                            //Intent intent = new Intent (MainActivity.this, MyProfileActivity.class);
                            //startActivity (intent);
                            return false;
                        }
                    })
                    .build();
            headerResult.addProfiles(new ProfileDrawerItem()
                    .withName(userDetailsPref.getStringPref(MainActivityNew.this, UserDetailsPref.USER_NAME))
                    .withEmail(userDetailsPref.getStringPref(MainActivityNew.this, UserDetailsPref.USER_EMAIL)));
        }
        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
//                .withToolbar (toolbar)
//                .withItemAnimator (new AlphaCrossFadeAnimator ())

                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home).withIdentifier(1).withTypeface(SetTypeFace.getTypeface(MainActivityNew.this)),
                        new PrimaryDrawerItem().withName("My Account").withIcon(FontAwesome.Icon.faw_user).withIdentifier(2).withSelectable(false).withTypeface(SetTypeFace.getTypeface(MainActivityNew.this)),
                        new PrimaryDrawerItem().withName("Faqs").withIcon(FontAwesome.Icon.faw_question).withIdentifier(3).withSelectable(false).withTypeface(SetTypeFace.getTypeface(MainActivityNew.this)),
                        new PrimaryDrawerItem().withName("Refer").withIcon(FontAwesome.Icon.faw_share).withIdentifier(4).withSelectable(false).withTypeface(SetTypeFace.getTypeface(MainActivityNew.this)),
                        new PrimaryDrawerItem().withName("Earned Points").withIcon(FontAwesome.Icon.faw_phone).withIdentifier(5).withSelectable(false).withTypeface(SetTypeFace.getTypeface(MainActivityNew.this)),
                        new PrimaryDrawerItem().withName("Winner List").withIcon(FontAwesome.Icon.faw_trophy).withIdentifier(8).withSelectable(false).withTypeface(SetTypeFace.getTypeface(MainActivityNew.this)),
                        //new PrimaryDrawerItem().withName("Change Password").withIcon(FontAwesome.Icon.faw_key).withIdentifier(5).withSelectable(false).withTypeface(SetTypeFace.getTypeface(MainActivity.this)),
                        new PrimaryDrawerItem().withName("Enquiry").withIcon(FontAwesome.Icon.faw_phone).withIdentifier(7).withSelectable(false).withTypeface(SetTypeFace.getTypeface(MainActivityNew.this)),
                        new PrimaryDrawerItem().withName("Sign Out").withIcon(FontAwesome.Icon.faw_sign_out).withIdentifier(6).withSelectable(false).withTypeface(SetTypeFace.getTypeface(MainActivityNew.this))
                )
                .withSavedInstance(savedInstanceState)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case 2:
                                Intent intentSubmitAd = new Intent(MainActivityNew.this, ProfileUpdateActivity.class);
                                startActivity(intentSubmitAd);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                break;

                            case 3:
                                Intent intentFaq = new Intent(MainActivityNew.this, FaqActivity.class);
                                startActivity(intentFaq);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                break;

                            case 4:
                                String message = "www.getjar.mobi/mobile/949674/AdyaQuiz";
                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("text/plain");
                                share.putExtra(Intent.EXTRA_TEXT, message);
                                startActivity(Intent.createChooser(share, "Share To"));

                                break;

                            case 5:
                                //UserDetailsPref userDetailsPref = UserDetailsPref.getInstance();
                                MaterialDialog dialog = new MaterialDialog.Builder(MainActivityNew.this)
                                        .content("Your Earned Points: " + userDetailsPref.getIntPref(MainActivityNew.this, UserDetailsPref.USER_TOTAL_AMOUNT))
                                        .positiveColor(getResources().getColor(R.color.primary_text2))
                                        .contentColor(getResources().getColor(R.color.primary))
                                        .negativeColor(getResources().getColor(R.color.primary_text2))
                                        .canceledOnTouchOutside(true)
                                        .cancelable(true)
                                        .positiveText("Redeem Your Points")
                                        .negativeText(R.string.dialog_action_no)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                //finish ();
                                                //overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                                            }
                                        }).build();
                                dialog.show();
                                break;

                            case 6:
                                showLogOutDialog();
                                break;

                            case 7:
                                Intent intentEnquiry = new Intent(MainActivityNew.this, EnquiryActivity.class);
                                startActivity(intentEnquiry);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                break;

                            case 8:
                                Intent intent = new Intent(MainActivityNew.this, ScoreListActivity.class);
                                startActivity(intent);
                                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                                break;

                        }
                        return false;
                    }
                })
                .build();
//        result.getActionBarDrawerToggle ().setDrawerIndicatorEnabled (false);
    }

    private void showLogOutDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .limitIconToDefaultSize()
                .content(getResources().getText(R.string.dialog_text_quit_application))
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .positiveText(getResources().getText(R.string.dialog_action_yes))
                .negativeText(getResources().getText(R.string.dialog_action_no))
                .typeface(SetTypeFace.getTypeface(MainActivityNew.this), SetTypeFace.getTypeface(MainActivityNew.this))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //    LoginManager.getInstance().logOut();
                        //    LISessionManager.getInstance(getApplicationContext()).clearSession();

                        //userDetailsPref.putStringPref(MainActivity.this, UserDetailsPref.USER_NAME, "");
                        userDetailsPref.putStringPref(MainActivityNew.this, UserDetailsPref.USER_EMAIL, "");
                        //userDetailsPref.putStringPref(MainActivity.this, UserDetailsPref.USER_MOBILE, "");
                        Intent intent = new Intent(MainActivityNew.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }).build();
        dialog.show();
    }

}
