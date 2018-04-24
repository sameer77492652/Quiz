package helpadya.com.quiz.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import helpadya.com.quiz.utils.UserDetailsPref;

/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        UserDetailsPref userDetailsPref = UserDetailsPref.getInstance();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("Token", refreshedToken);
        userDetailsPref.putStringPref(getApplicationContext(), UserDetailsPref.USER_FIREBASE_ID, refreshedToken);
        // Saving reg id to shared preferences

        // Notify UI that registration has completed, so the progress indicator can be hidden.

    }

}

