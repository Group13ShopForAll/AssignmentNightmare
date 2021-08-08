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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;

public class RegisterAccountPage extends AppCompatActivity {

    private EditText edtRegAcc, edtRegPassword, edtConfirmPassword;
    private TextView btnRegNext, tvErrorRegisterBanner;
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
        btnRegNext = (TextView) findViewById(R.id.btnRegNext);

        tvErrorRegisterBanner = (TextView) findViewById(R.id.tvErrorRegisterBanner);

        mAuth = FirebaseAuth.getInstance();

        // when user click on the register account button, functions trigger
        btnRegNext.setOnClickListener(new View.OnClickListener() {
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
            return;
        }
        // if the user had typed in all the required info, proceed to account registration action
        pbRegAcc.setVisibility(View.VISIBLE);
        checkAccountExistence();
    }

    private void checkAccountExistence() {
        mAuth.fetchSignInMethodsForEmail(emailAcc).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()){
                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                    if (isNewUser){
                        pbRegAcc.setVisibility(View.INVISIBLE);
                        tvErrorRegisterBanner.setVisibility(View.INVISIBLE);
                        sendToPersonalInfoActivity(emailAcc,password);
                    }else{
                        tvErrorRegisterBanner.setVisibility(View.VISIBLE);
                        pbRegAcc.setVisibility(View.INVISIBLE);
                        return;
                    }
                }
            }
        });
    }

    private void sendToPersonalInfoActivity(String emailAcc, String password) {
        Intent intent = new Intent(RegisterAccountPage.this, PersonalInfoActivity.class);
        intent.putExtra("emailAcc",emailAcc);
        intent.putExtra("password",password);
        startActivity(intent);
    }

}