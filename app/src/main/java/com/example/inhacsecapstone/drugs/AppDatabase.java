package com.example.inhacsecapstone.drugs;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;
import android.util.Pair;

import androidx.core.content.ContextCompat;

import com.example.inhacsecapstone.Entity.Medicine;
import com.example.inhacsecapstone.Entity.Takes;
import com.example.inhacsecapstone.cameras.CameraActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class AppDatabase extends SQLiteOpenHelper {
    private static AppDatabase INSTANCE;
    private static String databaseName = "app_database";
    private static SQLiteDatabase.CursorFactory factory = null;
    private static int version = 1;

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    private AppDatabase(Context context) {
        super(context, databaseName, factory, version);
    }

    public static AppDatabase getDataBase(Context context) {
                if (INSTANCE == null) {
                        synchronized (AppDatabase.class) {
                            INSTANCE = new AppDatabase(context);
                        }
        }
        return INSTANCE;
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE medicine_list (code INTEGER PRIMARY KEY, " +
                "name TEXT , " +
                "image TEXT," +
                "effect TEXT," +
                "usage TEXT," +
                "category INTEGER," +
                "single_dose FLOAT," +
                "daily_dose INTEGER," +
                "number_of_day_takens INTEGER," +
                "warning INTEGER, "+
                "start_day TEXT)");
        db.execSQL("CREATE TABLE taked (" + "" +
                "code INTEGER, " +
                "day TEXT, " +
                "time TEXT," +
                "memo TEXT," +
                "PRIMARY KEY (code, day, time))");
        db.execSQL("CREATE TABLE will_take (" +
                "code INTEGER, " +
                "time TEXT)");
        db.execSQL("CREATE TABLE temp_time (" +
                "code INTEGER," +
                "time TEXT)");
    }
/*
    public void init() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM medicine_list");
        db.execSQL("DELETE FROM taked");
        db.execSQL("DELETE FROM will_take");
        db.execSQL("DELETE FROM temp_time");

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int date = calendar.get(Calendar.DATE);

        String cur = Integer.toString(year) + "." + Integer.toString(month) + "." + Integer.toString(date);

        Medicine medi = new Medicine(11111111, "포크랄시럽", "https://www.health.kr/images/ext_images/pack_img/P_A11AGGGGA5864_01.jpg", "불면증, 수술 전 진정", "1일 1회 복용"
                , 0, 1, 3, 10, 0, cur);
        Takes take = new Takes(11111111, "2020.5.9", "12:10");
        insert(medi);
        insert(take);
        insertWillTake(11111111, "14:32");
        insertWillTake(11111111, "19:10");
        insertTempTake(11111111, "14:32");
        insertTempTake(11111111, "19:10");
    }*/


    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // Medicine 관련 함수들
    public void insert(Medicine medicine) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO medicine_list VALUES(" + medicine.getCode() + ", " +
                    isNull(medicine.getName()) + "," +
                    isNull(medicine.getImage()) + "," +
                    isNull(medicine.getEffect()) + "," +
                    isNull(medicine.getUsage()) + "," +
                    medicine.getCategory() + "," +
                    medicine.getSingleDose() + "," +
                    medicine.getDailyDose() + "," +
                    medicine.getNumberOfDayTakens() + "," +
                    medicine.getWarning() + "," +
                    isNull(medicine.getStartDay()) + ")");
        db.close();
    }
    public void deleteAllForCode(int code){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM medicine_list WHERE code = " + code);
        db.execSQL("DELETE FROM taked WHERE code = " + code);
        db.execSQL("DELETE FROM will_take WHERE code = " + code);
        db.execSQL("DELETE FROM temp_time WHERE code = " + code);
        db.close();
    }
    public ArrayList<Medicine> getAllMedicine() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Medicine> result = new ArrayList<Medicine>();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM medicine_list", null);
            while (cursor.moveToNext()) {
                Medicine current = new Medicine(cursor.getInt(cursor.getColumnIndex("code")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("image")),
                        cursor.getString(cursor.getColumnIndex("effect")),
                        cursor.getString(cursor.getColumnIndex("usage")),
                        cursor.getInt(cursor.getColumnIndex("category")),
                        cursor.getFloat(cursor.getColumnIndex("single_dose")),
                        cursor.getInt(cursor.getColumnIndex("daily_dose")),
                        cursor.getInt(cursor.getColumnIndex("number_of_day_takens")),
                        cursor.getInt(cursor.getColumnIndex("warning")),
                        cursor.getString(cursor.getColumnIndex("start_day")));
                result.add(current);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
    public ArrayList<Medicine> getMedicineAtDay(String day) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Medicine> result = new ArrayList<Medicine>();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM medicine_list INNER JOIN taked ON medicine_list.code = taked.code WHERE day = " + isNull(day) , null);
            while (cursor.moveToNext()) {
                Medicine current = new Medicine(cursor.getInt(cursor.getColumnIndex("code")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("image")),
                        cursor.getString(cursor.getColumnIndex("effect")),
                        cursor.getString(cursor.getColumnIndex("usage")),
                        cursor.getInt(cursor.getColumnIndex("category")),
                        cursor.getFloat(cursor.getColumnIndex("single_dose")),
                        cursor.getInt(cursor.getColumnIndex("daily_dose")),
                        cursor.getInt(cursor.getColumnIndex("number_of_day_takens")),
                        cursor.getInt(cursor.getColumnIndex("warning")),
                        cursor.getString(cursor.getColumnIndex("start_day")));
                result.add(current);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
    public void update(Medicine medi){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE medicine_list SET name = "+ isNull(medi.getName()) + "," +
                "image = "+ isNull(medi.getImage())+ ","+
                "effect = "+ isNull(medi.getEffect())+ ","+
                "usage = "+ isNull(medi.getUsage())+ ","+
                "category = "+ medi.getCategory()+ ","+
                "single_dose = "+ medi.getSingleDose()+ ","+
                "daily_dose = "+ medi.getDailyDose()+ ","+
                "number_of_day_takens = "+ medi.getNumberOfDayTakens()+ ","+
                "warning = "+ medi.getWarning()+ ","+
                "start_day = "+ isNull(medi.getStartDay())+ ""+
                "WHERE code = " + medi.getCode());

        db.close();
    }
    public Medicine getMedicine(int code){
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM medicine_list WHERE code = " + code + "", null);
            while (cursor.moveToNext()) {
                Medicine current = new Medicine(cursor.getInt(cursor.getColumnIndex("code")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("image")),
                        cursor.getString(cursor.getColumnIndex("effect")),
                        cursor.getString(cursor.getColumnIndex("usage")),
                        cursor.getInt(cursor.getColumnIndex("category")),
                        cursor.getFloat(cursor.getColumnIndex("single_dose")),
                        cursor.getInt(cursor.getColumnIndex("daily_dose")),
                        cursor.getInt(cursor.getColumnIndex("number_of_day_takens")),
                        cursor.getInt(cursor.getColumnIndex("warning")),
                        cursor.getString(cursor.getColumnIndex("start_day")));
                return current;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // take 관련 함수들
    public void insert(Takes take) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("INSERT INTO taked VALUES(" + take.getCode() + "," + isNull(take.getDay()) + "," + isNull(take.getTime()) + ", "+ isNull(take.getMemo()) + ")");
        db.close();
    }
    public void update(Takes take, String prevTime){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE taked SET day =" + isNull(take.getDay()) + ", time = "+ isNull(take.getTime()) + ", memo = "+ isNull(take.getMemo()) +" WHERE code = " + take.getCode() +
                " AND day=" + isNull(take.getDay()) + "AND time=" + isNull(prevTime));
    }
    public ArrayList<Takes> getAllTakes() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Takes> result = new ArrayList<Takes>();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM taked", null);
            while (cursor.moveToNext()) {
                Takes current = new Takes(cursor.getInt(cursor.getColumnIndex("code")),
                        cursor.getString(cursor.getColumnIndex("day")),
                        cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getString(cursor.getColumnIndex("memo")));
                result.add(current);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public String isNull(String str){
        if(str == null)
            return "null";
        else
            return "'" + str + "'";
    }
    class TakeSort implements Comparator<Takes> {
        public int compare(Takes a, Takes b)
        {

            String str_a[] = a.getTime().split(":");
            String str_b[] = b.getTime().split(":");
            int hour_a = Integer.parseInt(str_a[0]);
            int min_a = Integer.parseInt(str_a[1]);

            int hour_b = Integer.parseInt(str_b[0]);
            int min_b = Integer.parseInt(str_b[1]);

            if(hour_a == hour_b)
                return min_a - min_b;

            return hour_a - hour_b;

        }
    }
    class TimeSort implements Comparator<String> {
        public int compare(String a, String b)
        {

            String str_a[] = a.split(":");
            String str_b[] = b.split(":");
            int hour_a = Integer.parseInt(str_a[0]);
            int min_a = Integer.parseInt(str_a[1]);

            int hour_b = Integer.parseInt(str_b[0]);
            int min_b = Integer.parseInt(str_b[1]);

            if(hour_a == hour_b)
                return min_a - min_b;

            return hour_a - hour_b;
        }
    }

    public ArrayList<Takes> gettakesAtDay(String day) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Takes> result = new ArrayList<Takes>();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM taked WHERE day = " + isNull(day), null);
            while (cursor.moveToNext()) {
                Takes current = new Takes(cursor.getInt(cursor.getColumnIndex("code")),
                        cursor.getString(cursor.getColumnIndex("day")),
                        cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getString(cursor.getColumnIndex("memo")));
                result.add(current);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Collections.sort(result,new TakeSort());
        return result;
    }

    // will_take 관련 함수들
    public ArrayList<String> getWillTakeAtMedi(int code)
    {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> result = new ArrayList<String>();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM will_take WHERE code=" + code, null);
            while (cursor.moveToNext()) {
                result.add(cursor.getString(cursor.getColumnIndex("time")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Collections.sort(result,new TimeSort());
        return result;
    }
    public void insertWillTake(int code, String time){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO will_take VALUES("+ code + ", " + isNull(time)  +")");
        db.close();
    }
    public void updateWillTake(int code, String time, String pre){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE will_take SET time = " + isNull(time) + " WHERE code = " + code + " AND time = " + isNull(pre) );
        db.close();
    }
    public void deleteWillTake(int code, String time){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM will_take WHERE code = " + code + " AND time = " + isNull(time) );
        db.close();
    }


    public void insertTempTake(int code, String time){
        SQLiteDatabase db = getWritableDatabase();
        Calendar calendar = Calendar.getInstance();

        String day = Integer.toString(calendar.get(Calendar.YEAR)) + "." + Integer.toString(calendar.get(Calendar.MONTH) + 1) + "." + Integer.toString(calendar.get(Calendar.DATE));
        db.execSQL("INSERT INTO temp_time VALUES("+ code + ", " + isNull(time)  +")");
        db.close();
    }
    public void updateTempTake(int code, String time, String pre){
        SQLiteDatabase db = getWritableDatabase();
        Calendar calendar = Calendar.getInstance();

        String day = Integer.toString(calendar.get(Calendar.YEAR)) + "." + Integer.toString(calendar.get(Calendar.MONTH) + 1) + "." + Integer.toString(calendar.get(Calendar.DATE));
        // db.execSQL("UPDATE temp_time SET time = "+ isNull(time) + " WHERE code = " + code + " AND time = " + isNull(pre) + " AND day = " + isNull(day));
        db.execSQL("UPDATE temp_time SET time = "+ isNull(time) + " WHERE code = " + code + " AND time = " + isNull(pre));
        db.close();
    }

    public void deleteTempTake(int code, String time){
        SQLiteDatabase db = getWritableDatabase();
        Calendar calendar = Calendar.getInstance();

        String day = Integer.toString(calendar.get(Calendar.YEAR)) + "." + Integer.toString(calendar.get(Calendar.MONTH) + 1) + "." + Integer.toString(calendar.get(Calendar.DATE));
        db.execSQL("DELETE FROM temp_time WHERE code = " + code + " AND time = " + isNull(time));
        db.close();
    }

    public void setTempTime()
    {
        SQLiteDatabase dbwrite = getWritableDatabase();
        SQLiteDatabase dbread = getReadableDatabase();
        Calendar current = Calendar.getInstance();
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH)+1;
        int date = current.get(Calendar.DATE);

        String str = Integer.toString(year) + "." + Integer.toString(month) + "." + Integer.toString(date);
        try{
            dbwrite.execSQL("DELETE FROM temp_time");
            Cursor cursor = dbread.rawQuery("SELECT * FROM medicine_list A INNER JOIN will_take B ON A.code = B.code", null);
            while(cursor.moveToNext())
            {
                int code = cursor.getInt(cursor.getColumnIndex("code"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                int number_of_takens = cursor.getInt(cursor.getColumnIndex("number_of_day_takens"));
                String cursor_day = cursor.getString(cursor.getColumnIndex("start_day"));
                String cursor_day_str[] = cursor_day.split("\\.");
                Calendar calendar = Calendar.getInstance();

                calendar.set(Integer.parseInt(cursor_day_str[0]), Integer.parseInt(cursor_day_str[1])-1, Integer.parseInt(cursor_day_str[2]), 0, 0, 0);
                calendar.add(Calendar.DATE, number_of_takens);
                int result = calendar.compareTo(current);
                if(result > 0)
                    dbwrite.execSQL("INSERT INTO temp_time VALUES(" + code + ", " + isNull(time) +")");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public HashMap<String, ArrayList<Integer>> getRecentAlarmInfo()
    {
        SQLiteDatabase db = getReadableDatabase();
        HashMap<String, ArrayList<Integer>> result = new HashMap<String, ArrayList<Integer>>();

        Calendar calendar = Calendar.getInstance();
        int check = -1;
        ArrayList<Pair<String, Integer>> pairs = new ArrayList<>();

        int criterion = calendar.get(Calendar.HOUR_OF_DAY )*100 + calendar.get(Calendar.MINUTE);
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM temp_time B", null);
            while(cursor.moveToNext())
            {
                int code = cursor.getInt(cursor.getColumnIndex("code"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                pairs.add(new Pair<String, Integer>(time, code));
            }


            Collections.sort(pairs, new pairSort());
            for(Pair<String, Integer> iter : pairs){
                String time = iter.first;
                int code = iter.second;

                String hour_min[] = time.split(":");
                int prior = Integer.parseInt(hour_min[0])*100 + Integer.parseInt(hour_min[1]);

                if(criterion < prior)
                {

                    if(prior != check && check != -1)
                        break;

                    if(result.isEmpty())
                    {
                        check = prior;
                        result.put(time, new ArrayList<Integer>());
                        result.get(time).add(code);
                    }
                    else
                        result.get(time).add(code);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return result;
    }
    class pairSort implements Comparator<Pair<String, Integer>> {
        public int compare(Pair<String, Integer> a, Pair<String, Integer> b)
        {
            String str_a[] = a.first.split(":");
            String str_b[] = b.first.split(":");
            int hour_a = Integer.parseInt(str_a[0]);
            int min_a = Integer.parseInt(str_a[1]);

            int hour_b = Integer.parseInt(str_b[0]);
            int min_b = Integer.parseInt(str_b[1]);

            if(hour_a == hour_b)
                return min_a - min_b;

            return hour_a - hour_b;
        }
    }
}