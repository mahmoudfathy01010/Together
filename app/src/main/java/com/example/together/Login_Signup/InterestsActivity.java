package com.example.together.Login_Signup;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.CompoundButtonCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.together.R;
import com.example.together.data.model.User;
import com.example.together.data.storage.Storage;
import com.example.together.group_screens.AddGroup;
import com.example.together.utils.HelperClass;
import com.example.together.view_model.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.together.utils.HelperClass.ERROR_INTERESTS;
import static com.example.together.utils.HelperClass.TAG;
import static com.example.together.utils.HelperClass.showAlert;


public class InterestsActivity extends AppCompatActivity {

    final String[] interests = {" PHP", " JAVA", " JSON", " C#", " Objective-C"};
    LinearLayout containerLayout;
    Button signupBtn;
    UserViewModel userViewModel;
    List<String> selectedInterest;
    ColorStateList colorStateList = new ColorStateList(
            new int[][]{
                    new int[]{-android.R.attr.state_checked}, // unchecked
                    new int[]{android.R.attr.state_checked}, // checked
            },
            new int[]{
                    Color.parseColor("#000000"),
                    Color.parseColor("#1278da")
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);
        getSupportActionBar().hide();

        containerLayout = findViewById(R.id.container_layout);
        signupBtn = findViewById(R.id.signup_btn);
        selectedInterest = new ArrayList<>();

        CompoundButton.OnCheckedChangeListener listener = (buttonView, isChecked) -> {
            if (isChecked) {
//                Toast.makeText(getApplicationContext(),buttonView.getText(), Toast.LENGTH_SHORT).show();
                //TODO
                selectedInterest.add(buttonView.getText().toString());
            } else {
//                Toast.makeText(getApplicationContext(), buttonView.getText() + "Removed", Toast.LENGTH_SHORT).show();
                //TODO
                selectedInterest.remove(buttonView.getText().toString());

            }
        };

        for (int i = 0; i < interests.length; i++) {
            CheckBox ch = new CheckBox(this);
            ch.setTextColor(getResources().getColor(R.color.black));
            CompoundButtonCompat.setButtonTintList(ch, colorStateList);

            ch.setText(interests[i]);
            ch.setOnCheckedChangeListener(listener);
            containerLayout.addView(ch);


            LayoutParams lp = (LinearLayout.LayoutParams) ch.getLayoutParams();

            lp.setMargins(0, 0, 0, 17);
            ch.setLayoutParams(lp);

        }

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        signupBtn.setOnClickListener(v -> createAccount());

    }

    private void createAccount() {
        if (selectedInterest.isEmpty()) {
            showAlert(ERROR_INTERESTS, this);
        } else {

            for (String i : selectedInterest) {
                Log.i(TAG, "createAccount: " + i);
            }
            Storage s = new Storage();
            User user = s.getPassUser(this);
            user.setInterests(selectedInterest);
            userViewModel.signUp(user).observe(this, this::userSignUpObservable);

          /*  userViewModel.signUp(user).observe(this, new Observer<String>() {

                @Override
                public void onChanged(String s) {

                }
            });*/
        }
    }

    private void userSignUpObservable(String res) {

        Log.i(TAG, "SignUpActivity -- createAccount()  res >> " + res);

        if (res.equals(HelperClass.SING_UP_SUCCESS)) {
            Toast.makeText(this, res, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            showAlert(res, this);
        }
    }


}


