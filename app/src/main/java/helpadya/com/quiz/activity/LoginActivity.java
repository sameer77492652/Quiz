package helpadya.com.quiz.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import helpadya.com.quiz.R;
import helpadya.com.quiz.fragment.SignInFragment;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        SignInFragment signInFragment = new SignInFragment();
        fragmentTransaction.replace(R.id.frameLayout, signInFragment);
        fragmentTransaction.commit(); // save the changes
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
