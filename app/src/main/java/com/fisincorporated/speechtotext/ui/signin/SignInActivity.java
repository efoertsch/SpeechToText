package com.fisincorporated.speechtotext.ui.signin;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fisincorporated.speechtotext.R;
import com.fisincorporated.speechtotext.application.TokenStorage;
import com.fisincorporated.speechtotext.audio.utils.SpeechToTextConversionData;
import com.fisincorporated.speechtotext.jobscheduler.JobSchedulerUtil;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * See https://github.com/googlesamples/google-services/blob/master/android/signin/app/src/main/java/com/google/samples/quickstart/signin/RestApiActivity.java
 * For this app, to have a successful sign-in 3 things must occur
 * 1. Google Sign in successful
 * 2. Firebase sign in succesful
 * 3. Google Oauth2 credential with gcs scope obtained.
 */
//TODO convert to MVVM/databinding
public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_RECOVERABLE = 9002;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;


    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private ProgressDialog mProgressDialog;
    private AlertDialog signOnErrorDialog;
    private SpeechToTextConversionData speechToTextConversionData;

    @Inject
    public JobSchedulerUtil jobSchedulerUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setupViews();
        setupGoogleAndFirebase();

    }

    private void setupGoogleAndFirebase() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Scope is to allow speech to text api to read uploaded gcs audio file
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                // Strings from src/debug/res/values/strings.xml
                .requestIdToken(getString(R.string.web_client_id))
                // scopes not needed?
//                .requestScopes(new Scope(getString(R.string.gcs_access_scope))
//                        , new Scope(getString(R.string.gcs_read_write_scope)))
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProgressDialog();
        checkForSpeechToTextData();
        String oauth2Token = TokenStorage.getToken(this);
//        if (oauth2Token == null) {
//            if (mAuth.getCurrentUser() == null) {
//                signOut();
//            } else {
//                signIn();
//            }
//
//        } else {
//            createJobSchedulerJob(speechToTextConversionData, oauth2Token);
//            finish();
//        }
    }


    private void checkForSpeechToTextData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String jsonData = bundle.getString(SpeechToTextConversionData.SPEECH_TO_TEXT_CONVERSION_DATA);
            if (jsonData != null && !jsonData.isEmpty()) {
                Gson gson = new Gson();
                speechToTextConversionData = gson.fromJson(jsonData, SpeechToTextConversionData.class);
                if (speechToTextConversionData == null ||
                        speechToTextConversionData.getAudioRecordRealmId() <= 0
                        || speechToTextConversionData.getAudio3gpFileName() == null
                        || speechToTextConversionData.getAudio3gpFileName().isEmpty()) {
                    finish();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                updateUI(null);
                displaySignOnErrorDialog(getString(R.string.google_signin_unsuccessful));
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        // get token for passing to jobscheduler for xlat of audio
                        new Oauth2TokenTask().execute(acct.getAccount());
                        //shouldStartSpeechToTextConverstion(user.getIdToken(true));
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        displaySignOnErrorDialog(getString(R.string.firebase_signin_unsuccessful));
                        updateUI(null);
                    }

                    // [START_EXCLUDE]
                    hideProgressDialog();
                    // [END_EXCLUDE]
                });
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> updateUI(null));
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                task -> updateUI(null));
    }


    private void setupViews() {
        setContentView(R.layout.activity_google_signin);

        // Views
        mStatusTextView = findViewById(R.id.status);
        mDetailTextView = findViewById(R.id.detail);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        //findViewById(R.id.disconnect_button).setOnClickListener(this);

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
        }
    }

    /**
     * AsyncTask that uses the credentials from Google Sign In to access the People API.
     */
    private class Oauth2TokenTask extends AsyncTask<Account, Void, String> {

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(Account... params) {
            try {
                ArrayList<String> scopes = new ArrayList<>();
                //scopes.add("https://www.googleapis.com/auth/drive.file");
                scopes.add("https://www.googleapis.com/auth/cloud-platform");
                GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                        SignInActivity.this, scopes);
                credential.setSelectedAccount(params[0]);
                return credential.getToken();
            } catch (UserRecoverableAuthIOException userRecoverableException) {
                Log.w(TAG, "UserRecoverableAuthIOException", userRecoverableException);
                startActivityForResult(userRecoverableException.getIntent(), RC_RECOVERABLE);
            } catch (GoogleAuthException e) {
                Log.w(TAG, "GoogleAuthException:" + e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.w(TAG, "IOException:" + e.toString());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String oauth2Token) {
            hideProgressDialog();
            if (oauth2Token == null) {
                displaySignOnErrorDialog(getString(R.string.error_on_getting_oauth2_token));
            } else {
                TokenStorage.storeToken(SignInActivity.this, oauth2Token);
                createJobSchedulerJob(speechToTextConversionData, oauth2Token);
                finish();
            }
        }
    }

    private void createJobSchedulerJob(SpeechToTextConversionData speechToTextConversionData, String token) {
        speechToTextConversionData.setOauth2Token(token);
        jobSchedulerUtil.scheduleXlatJob(this, speechToTextConversionData);
    }

    private void displaySignOnErrorDialog(String errorMsg) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(errorMsg)
                .setTitle(R.string.signOnError);

        builder.setPositiveButton(R.string.retry, (dialog, id) -> {
            signOut();
            signIn();
        });
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
            finish();
        });

        // 3. Get the AlertDialog from create()
        signOnErrorDialog = builder.create();
        signOnErrorDialog.setCanceledOnTouchOutside(false);
        signOnErrorDialog.show();
    }



    public static class IntentBuilder {
        private Bundle extras;

        private IntentBuilder() {
            extras = new Bundle();
        }

        public static IntentBuilder getBuilder() {
            IntentBuilder builder = new IntentBuilder();
            return builder;
        }

        public IntentBuilder setSpeechToTextData(String speechTToTextConversionDataJson) {
            extras.putString(SpeechToTextConversionData.SPEECH_TO_TEXT_CONVERSION_DATA, speechTToTextConversionDataJson);
            return this;
        }


        public Intent build(Context ctx) {
            Intent i = new Intent(ctx, SignInActivity.class);
            i.putExtras(extras);
            return i;
        }
    }
}
