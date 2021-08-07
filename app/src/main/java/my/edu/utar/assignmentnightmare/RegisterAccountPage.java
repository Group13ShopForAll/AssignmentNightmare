package my.edu.utar.assignmentnightmare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterAccountPage extends AppCompatActivity {

    private EditText edtRegAcc, edtRegPassword, edtConfirmPassword;
    private TextView btnRegisterAcc, tvErrorRegisterBanner;
    private ProgressBar pbRegAcc;

    private String emailAcc, password, confirmPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account_page);

        edtRegAcc = (EditText) findViewById(R.id.edtRegAccount);
        edtRegPassword = (EditText) findViewById(R.id.edtPassword);
        edtConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);

        pbRegAcc = (ProgressBar) findViewById(R.id.pbRegAcc);
        btnRegisterAcc = (TextView) findViewById(R.id.btnRegAcc);

        tvErrorRegisterBanner = (TextView) findViewById(R.id.tvErrorRegisterBanner);

        mAuth = FirebaseAuth.getInstance();

        // when user click on the register account button, functions trigger
        btnRegisterAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //first check if the user had filled in all the required fields.
                validateRegisterAccountInfo();
            }
        });

    }

    private void validateRegisterAccountInfo() {
        emailAcc = edtRegAcc.getText().toString();
        password = edtRegPassword.getText().toString();
        confirmPassword = edtConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(emailAcc)){
            edtRegAcc.setError("Please fill in email");
            return;
        }
        if (TextUtils.isEmpty(password)){
            edtRegPassword.setError("Please type your password");
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)){
            edtConfirmPassword.setError("Please type your password again");
            return;
        }
        if (!password.equals(confirmPassword)){
            edtConfirmPassword.setError("Confirm password needs to be same as previous password");
        }
        // if the user had typed in all the required info, proceed to account registration action
        pbRegAcc.setVisibility(View.VISIBLE);
        registerAccount();
    }

    private void registerAccount() {
        mAuth.createUserWithEmailAndPassword(emailAcc,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                pbRegAcc.setVisibility(View.INVISIBLE);
                tvErrorRegisterBanner.setVisibility(View.INVISIBLE);
                Toast.makeText(RegisterAccountPage.this, "Successfully created new account", Toast.LENGTH_SHORT).show();
                sendToPersonalInfoActivity();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterAccountPage.this, "Failed created account, "+e.getMessage(), Toast.LENGTH_SHORT).show();
                tvErrorRegisterBanner.setVisibility(View.VISIBLE);
                pbRegAcc.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void sendToPersonalInfoActivity() {
        startActivity(new Intent(RegisterAccountPage.this, PersonalInfoActivity.class));
    }

    private void sendToLoginPage() {
        startActivity(new Intent(RegisterAccountPage.this, LoginPage.class));
    }
}