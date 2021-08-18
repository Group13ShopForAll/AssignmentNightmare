package my.edu.utar.assignmentnightmare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;

import org.w3c.dom.Text;

public class LoginPage extends AppCompatActivity {

    //Done by Jiun Lin
    private TextView btnLoginAccount, btnRegisterAccount, tvErrorLoginBanner, tvForgetPassword;
    private EditText edtLoginAccount, edtLoginPassword;
    private ProgressBar pbLoginAcc;

    private String loginAccount, loginPassword;

    private FirebaseAuth mAuth;

    private static final String TAG = "LoginPage";
    private TextView mTokentv;
    private Button mGetTokenBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        edtLoginAccount = (EditText) findViewById(R.id.edtLoginAccount);
        edtLoginPassword = (EditText) findViewById(R.id.edtLoginPassword);
        btnLoginAccount = (TextView) findViewById(R.id.btnLoginAccount);
        btnRegisterAccount = (TextView) findViewById(R.id.btnRegisterAccount);
        tvErrorLoginBanner = (TextView) findViewById(R.id.tvErrorLoginBanner);
        pbLoginAcc = (ProgressBar) findViewById(R.id.pbLoginAcc);

        mTokentv = (TextView) findViewById(R.id.push_token_tv);
        mGetTokenBtn = (Button) findViewById(R.id.get_push_token_btn);

        mGetTokenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToken();
            }
        });

        //tvForgetPassword = (TextView) findViewById(R.id.tvForgetPassword);

        mAuth = FirebaseAuth.getInstance();

        // when click on the register account button redirect to account registration page
        btnRegisterAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToRegisterAccPage();
            }
        });

        // when click on the login button, perform input validation checking
        btnLoginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateLoginInfo();
            }
        });

    }

    private void showToken(final String token) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTokentv.setText(token);
            }
        });
    }

    private void validateLoginInfo() {
        loginAccount = edtLoginAccount.getText().toString();
        loginPassword = edtLoginPassword.getText().toString();

        if (TextUtils.isEmpty(loginAccount)){
            edtLoginAccount.setError("Please type in your email account");
            return;
        }
        if (TextUtils.isEmpty(loginPassword)){
            edtLoginPassword.setError("Please type in your account password");
            return;
        }

        pbLoginAcc.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(loginAccount,loginPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                pbLoginAcc.setVisibility(View.INVISIBLE);
                tvErrorLoginBanner.setVisibility(View.INVISIBLE);
                sendToHomePage();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pbLoginAcc.setVisibility(View.INVISIBLE);
                tvErrorLoginBanner.setVisibility(View.VISIBLE);
                return;
            }
        });

    }

    private void sendToHomePage() {
        startActivity(new Intent(LoginPage.this, Homepagee.class));
    }

    private void sendToRegisterAccPage() {
        startActivity(new Intent(LoginPage.this, RegisterAccountPage.class));
    }

    private void getToken() {
        // Create a thread.
        new Thread() {
            @Override
            public void run() {
                try {
                    // Obtain the app ID from the agconnect-service.json file.
                    String appId = AGConnectServicesConfig.fromContext(LoginPage.this).getString("client/app_id");
                    // Set tokenScope to HCM.
                    String tokenScope = "HCM";
                    String token = HmsInstanceId.getInstance(LoginPage.this).getToken(appId, tokenScope);
                    Log.i(TAG, "get token: " + token);

                    // Check whether the token is empty.
                    if (!TextUtils.isEmpty(token)) {
                        showToken(token);
                        sendRegTokenToServer(token);
                    }
                } catch (ApiException e) {
                    Log.e(TAG, "get token failed, " + e);
                }
            }
        }.start();
    }

    private void sendRegTokenToServer(String token) {
        Log.i(TAG, "sending token to server. token:" + token);
    }

}