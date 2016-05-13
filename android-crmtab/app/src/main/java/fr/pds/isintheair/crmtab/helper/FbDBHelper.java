package fr.pds.isintheair.crmtab.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import fr.pds.isintheair.crmtab.model.entity.FbEventsPojo.Data;

/**
 * Created by Muthu on 07/04/2016.
 */
public class FbDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "crm.db";
    public static final String EVENTS_TABLE = "datas";
    Gson gson = new Gson();
    public FbDBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table going_datas " +
                        "(id integer primary key, data text)"
        );

        sqLiteDatabase.execSQL(
                "create table pending_datas " +
                        "(id integer primary key, data text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertEvent (String data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("data", data);
        db.insert("going_datas", null, contentValues);
        return true;
    }
    public boolean insertPendingEvent (String data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("data", data);
        db.insert("pending_datas", null, contentValues);
        return true;
    }

    public boolean checkIfDataExistsInDb(Data data)
    {
        ArrayList<Data> array_list = new ArrayList<Data>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from going_datas", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(gson.fromJson(res.getString(res.getColumnIndex("data")),Data.class));

            res.moveToNext();
        }

        for(Data d : array_list ){
            if(d.getId().equalsIgnoreCase(data.getId())){
                return true;
            }
        }
        return false;
    }
    public boolean checkIfPendingDataExistsInDb(Data data)
    {
        ArrayList<Data> array_list = new ArrayList<Data>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from pending_datas", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(gson.fromJson(res.getString(res.getColumnIndex("data")),Data.class));

            res.moveToNext();
        }

        for(Data d : array_list ){
            if(d.getId().equalsIgnoreCase(data.getId())){
                return true;
            }
        }
        return false;
    }
    public List<Data> getAttendingDataInDb()
    {
        ArrayList<Data> array_list = new ArrayList<Data>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from going_datas", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(gson.fromJson(res.getString(res.getColumnIndex("data")),Data.class));
            res.moveToNext();
        }

        return array_list;
    }


}
