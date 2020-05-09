package com.example.inhacsecapstone.drugs;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inhacsecapstone.R;

import java.util.ArrayList;
import java.util.List;

public class RecogResultListAdapter extends RecyclerView.Adapter<RecogResultListAdapter.RecogResultListHolder> {
    class RecogResultListHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView nameView;
        private final EditText amountView;
        private final TextView descView;
        private final EditText singleDoseView;
        private final EditText dailyDoseView;
        private final EditText numberOfDayTakensView;
        private RecogResultListHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.drugImage) ;
            nameView = itemView.findViewById(R.id.drugName) ;
            amountView = itemView.findViewById(R.id.Amount) ;
            descView = itemView.findViewById(R.id.desc) ;
            singleDoseView =  itemView.findViewById(R.id.singleDose) ;
            dailyDoseView =  itemView.findViewById(R.id.dailyDose) ;
            numberOfDayTakensView = itemView.findViewById(R.id.period);
        }
    }
    private Context context;
    private final LayoutInflater mInflater;
    private ArrayList<MedicineEntity> mdrugs; // Cached copy of words

    public RecogResultListAdapter(Context context, ArrayList<MedicineEntity> drugs) {
        mInflater = LayoutInflater.from(context);
        this.mdrugs = drugs;
        this.context = context;
    }

    @Override
    public RecogResultListAdapter.RecogResultListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recog_result_list_item, parent, false);
        return new RecogResultListAdapter.RecogResultListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecogResultListAdapter.RecogResultListHolder holder, int position) {
        if (mdrugs != null) {
            MedicineEntity curDrug = mdrugs.get(position);

            Glide.with(context).load(curDrug.getImage()).into(holder.imageView);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImage(curDrug.getImage());
                }
            });
            holder.amountView.setText(curDrug.getAmount() == -1 ? "" : Integer.toString(curDrug.getAmount()));
            holder.dailyDoseView.setText(curDrug.getDailyDose() == -1 ? "" : Integer.toString(curDrug.getDailyDose()));
            holder.descView.setText(curDrug.getDesc() == null ? ""  : curDrug.getDesc());
            holder.nameView.setText(curDrug.getName() == null ? "" : curDrug.getName());
            holder.numberOfDayTakensView.setText(curDrug.getNumberOfDayTakens() == -1 ? "" : Integer.toString(curDrug.getNumberOfDayTakens()));
            holder.singleDoseView.setText(curDrug.getSingleDose() == null ? "" : curDrug.getSingleDose());

            holder.amountView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String str = s.toString();
                    try {
                        curDrug.setAmount(Integer.parseInt(str));
                    }catch(Exception ex)
                    {
                        Toast.makeText(context, "양식에 맞게 써주세요.", Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                }
            });
            holder.dailyDoseView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String str = s.toString();
                    try {
                        curDrug.setDailyDose(Integer.parseInt(str));
                    }
                    catch(Exception ex)
                    {
                        Toast.makeText(context, "양식에 맞게 써주세요.", Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }

                }
            });
            holder.numberOfDayTakensView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String str = s.toString();
                    try {
                        curDrug.setNumberOfDayTakens(Integer.parseInt(str));
                    }
                    catch(Exception ex)
                    {
                        Toast.makeText(context, "양식에 맞게 써주세요.", Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                }
            });

            holder.singleDoseView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String str = s.toString();
                    try {
                        curDrug.setSingleDose(str);
                    }catch(Exception ex)
                    {
                        Toast.makeText(context, "양식에 맞게 써주세요.", Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                }
            });
        } else {
        }
    }
    @Override
    public int getItemCount() {
        if (mdrugs != null)
            return mdrugs.size();
        else return 0;
    }
    public void showImage(String url) {
        LayoutInflater factory = LayoutInflater.from(context);
        final View view = factory.inflate(R.layout.myphoto_layout, null);
        Dialog dialog = new Dialog(context);
        ImageView iv = view.findViewById(R.id.iv);
        Glide.with(context).load(url).into(iv);
        dialog.setContentView(view);
        dialog.show();
    }
}