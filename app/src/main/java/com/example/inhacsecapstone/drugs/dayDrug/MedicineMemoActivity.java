package com.example.inhacsecapstone.drugs.dayDrug;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.Entity.Takes;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.drugs.AppDatabase;

import java.util.ArrayList;
import java.util.EventListener;

public class MedicineMemoActivity extends AppCompatActivity implements EventListener {
    private AppDatabase appDatabase;
    private EditText et;
    private SpeechRecognizer mRecognizer;
    private String STTresult = null;


    private RecognitionListener STTlistener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float v) {
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int i) {
        }

        @Override
        public void onResults(Bundle results) {
            String key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            for (int i = 0; i < rs.length; i++) {
                Log.d("@@@", rs[i] + "\n");
            }
            STTresult = rs[0];
            et.setText(STTresult);
            mRecognizer.destroy();
        }

        @Override
        public void onPartialResults(Bundle bundle) {
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_memo);
        appDatabase = AppDatabase.getDataBase(getApplicationContext());
        Medicine medi = (Medicine) getIntent().getSerializableExtra("medicine");
        String day = getIntent().getStringExtra("day");
        String time = getIntent().getStringExtra("time");
        String memo = getIntent().getStringExtra("memo");

        TextView txtv_name = findViewById(R.id.drugName);
        ImageView img = findViewById(R.id.drugImage);
        TextView txtv_time = findViewById(R.id.takedTime);
        TextView txtv_memo = findViewById(R.id.memo_contents);
        et = new EditText(MedicineMemoActivity.this);

        Glide.with(this).load(medi.getImage()).into(img);
        txtv_name.setText(medi.getName());
        txtv_time.setText(changeTime(time));
        if(memo != null)
            txtv_memo.setText(memo);
        String[] h_m = time.split(":");

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String texts = null;
                if(!txtv_memo.getText().equals(null)) {
                    texts = txtv_memo.getText().toString();
                }

                String time = hourOfDay + ":" + minute;
                appDatabase.update(new Takes(medi.getCode(), day, time, texts), h_m[0] + ":" + h_m[1]);
                txtv_time.setText(changeTime(time));
                h_m[0] = Integer.toString(hourOfDay);
                h_m[1] = Integer.toString(minute);
            }
        };

        txtv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                int hour = Integer.parseInt(textView.getText().toString().split(":")[0]);
                int minuite = Integer.parseInt(textView.getText().toString().split(":")[1]);
                TimePickerDialog dialog = new TimePickerDialog(MedicineMemoActivity.this, android.R.style.Theme_Holo_Light_Dialog, listener, hour, minuite, true);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });


        txtv_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog ad = new AlertDialog.Builder(MedicineMemoActivity.this)
                        .setTitle("메모 수정")
                        .setMessage("수정할 내용을 입력해주세요.")
                        .setPositiveButton("확인", null)
                        .setNegativeButton("취소", null)
                        .setNeutralButton("음성 입력", null)
                        .create();

                et.setText(txtv_memo.getText());
                et.setTextColor(Color.BLACK);
                if (et.getParent() != null)
                    ((ViewGroup) et.getParent()).removeView(et);
                ad.setView(et);


                ad.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button btn_p = ad.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button btn_n = ad.getButton(AlertDialog.BUTTON_NEGATIVE);
                        Button btn_s = ad.getButton(AlertDialog.BUTTON_NEUTRAL);

                        btn_p.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                txtv_memo.setText(et.getText());
                                // 디비에 내용 업데이트
                                String texts = null;
                                if (!txtv_memo.getText().equals(null)) {
                                    texts = txtv_memo.getText().toString();
                                }
                                appDatabase.update(new Takes(medi.getCode(), day, h_m[0] + ":" + h_m[1], texts), h_m[0] + ":" + h_m[1]);
                                dialog.dismiss();

                            }
                        });
                        btn_n.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();     //닫기
                            }
                        });
                        btn_s.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent SttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                SttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplicationContext().getPackageName());
                                SttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

                                mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                                mRecognizer.setRecognitionListener(STTlistener);

                                mRecognizer.startListening(SttIntent);
                                STTresult = null;
                            }
                        });
                    }
                });
                ad.show();
            }
        });
    }
    public String changeTime(String time) {
        String[] h_m = time.split(":");
        if (h_m[0].length() == 1) h_m[0] = "0" + h_m[0];
        if (h_m[1].length() == 1) h_m[1] = "0" + h_m[1];
        return h_m[0] + ":" + h_m[1];
    }
}
