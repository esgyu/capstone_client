package com.example.inhacsecapstone.drugs.Recog;

import android.content.Context;
import android.content.Intent;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.MainActivity;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.cameras.CameraActivity;
import com.example.inhacsecapstone.chatbot.MessengerActivity;
import com.example.inhacsecapstone.drugs.AppDatabase;
import com.example.inhacsecapstone.drugs.Drugs;
import com.example.inhacsecapstone.drugs.RecyclerViewDecorator;
import com.example.inhacsecapstone.initial.InformationSetting;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class RecogResultActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecogResultListAdapter adapter;
    private Context context;
    private AppDatabase db;
    private int ADD_MEDICINE_REQUEST = 2;
    private ArrayList<Medicine> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recog_result);
        arrayList = new ArrayList<Medicine>();
        Calendar calendar = Calendar.getInstance();
        String day = Integer.toString(calendar.get(Calendar.YEAR)) + "."+ Integer.toString(calendar.get(Calendar.MONTH) + 1) + "." + Integer.toString(calendar.get(Calendar.DATE));
        Drugs[] drugs = (Drugs[]) getIntent().getSerializableExtra("drugs");
        for (Drugs iter : drugs) {
            String img = iter.getSmall_image() == null || iter.getSmall_image().equals("null") || iter.getSmall_image().equals("") ?
                    (iter.getPack_image()== null ||  iter.getPack_image().equals("null") || iter.getPack_image().equals("") ? null : iter.getPack_image()) : iter.getSmall_image();
            arrayList.add(new Medicine(iter.getCode(), iter.getDrug_name(), img, iter.getEffect(), iter.getUsages(), -1,
                    iter.getSingle_dose(), iter.getDaily_dose(), iter.getTotal_dose(), -1, day)); // 카테고리, warning 해결해야함.
        }

        mRecyclerView = this.findViewById(R.id.recogList);
        adapter = new RecogResultListAdapter(this, arrayList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = AppDatabase.getDataBase(getApplicationContext());
        mRecyclerView.addItemDecoration(new RecyclerViewDecorator(30));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this::floatingonClick);


        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Medicine> toss = new ArrayList<Medicine>();
                for(Medicine iter : arrayList)
                {
                    if(iter.getDailyDose() == -1 ||  iter.getSingleDose() == -1 || iter.getNumberOfDayTakens() == -1) {
                        Toast.makeText(context, "빈칸을 채워주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(iter.getDailyDose() == 0 ||  iter.getSingleDose() == 0 || iter.getNumberOfDayTakens() == 0)
                    {
                        Toast.makeText(context, "각 칸에 0을 입력할 수는 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                for (Medicine iter : arrayList)
                {
                    toss.add(iter);
                }
                Intent intent = new Intent(context, SetTimeActivity.class);
                intent.putExtra("medicine", toss);
                if(toss.size() > 0)
                    startActivity(intent);
                else
                    Toast.makeText(context, "모든 약이 복용중입니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        findViewById(R.id.nosubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CameraActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_MEDICINE_REQUEST)
        {
            if(resultCode == RESULT_OK)
            {
                Medicine medi = (Medicine)data.getSerializableExtra("medicine");
                arrayList.add(medi);
                adapter.notifyDataSetChanged();
            }
            else{
                Toast.makeText(this, "약 추가에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void floatingonClick(View v) {
        Intent intent = new Intent(RecogResultActivity.this,  AddMedicineActivity.class);
        startActivityForResult(intent, ADD_MEDICINE_REQUEST);
    }
}