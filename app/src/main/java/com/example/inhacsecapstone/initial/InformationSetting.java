package com.example.inhacsecapstone.initial;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inhacsecapstone.MainActivity;
import com.example.inhacsecapstone.R;

import java.util.Calendar;

public class InformationSetting extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener callbackMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_setting);
        InitializeListener();
    }

    public void InitializeListener() {
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                TextView userBirth = findViewById(R.id.userBirth);
                userBirth.setText(year + "년 " + (monthOfYear+1) + "월 " + dayOfMonth + "일");
            }
        };
    }

    public void setDate(View view) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR), month = c.get(Calendar.MONTH), day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, year, month, day);
        dialog.show();
    }

    public void btnClick(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("SHARE_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        TextView userName = findViewById(R.id.userName);
        TextView userBirth = findViewById(R.id.userBirth);
        RadioGroup rg = findViewById(R.id.radioGroup);
        RadioButton rb = findViewById(rg.getCheckedRadioButtonId());
        Spinner spinner = findViewById(R.id.spinner);

        String user_name, user_birth, user_job;
        int user_sex, user_age;
        if (rb.getText().toString() == "남자")
            user_sex = 1;
        else
            user_sex = 2;
        user_name = userName.getText().toString();
        user_birth = userBirth.getText().toString();
        if (TextUtils.isEmpty(userName.getText()) || TextUtils.isEmpty(user_birth)) {
            Toast.makeText(this, "유저 정보를 모두 입력해주세요!", Toast.LENGTH_SHORT).show();
        } else {
            editor.putString("Name", user_name);
            editor.putInt("noti_cnt", 0);
            editor.commit();
            Toast.makeText(this, "유저 정보 설정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
