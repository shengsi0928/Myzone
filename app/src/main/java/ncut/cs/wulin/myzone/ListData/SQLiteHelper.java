package ncut.cs.wulin.myzone.ListData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

public class SQLiteHelper extends SQLiteOpenHelper  {
    private final static String DATABASE_NAME = "Library";
    private final static int Version = 1;
    private final static String user_table = "user";
    private final static String content_table = "content";
    private final static String comment_table = "comment";

    public SQLiteHelper(Context context, int version){
        super(context, DATABASE_NAME, null, version);
    }

    public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        String sql = "Create table " + content_table
                + "(userid varchar(30) not null,"
                + "content vanchar(100), "
                + "date timestamp DEFAULT CURRENT_TIMESTAMP,"
                + "likeCount int, "
                + "contentid int not null)";
        try{
            db.execSQL(sql);
        }catch (SQLException e){
            e.printStackTrace();
            Log.e("error", e.toString());
        }
        sql = "Create table " + user_table
                + "(userid varchar(30) primary key, "
                + "displayname varchar(30) )";
        try{
            db.execSQL(sql);
        }catch (SQLException e){
            e.printStackTrace();
            Log.e("error", e.toString());
        }
        sql = "Create table " + comment_table
                + "(userid varchar(30) not null,"
                + "content vanchar(100), "
                + "date timestamp DEFAULT CURRENT_TIMESTAMP,"
                + "contentid int not null)";
        try{
            db.execSQL(sql);
        }catch (SQLException e){
            e.printStackTrace();
            Log.e("error", e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + content_table;
        db.execSQL(sql);
        onCreate(db);
    }

    public void insert_comment(Data data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("userid", data.getUserId());
        content.put("content", data.getContent());
        content.put("contentid", data.getContent_id());
        try{
            db.insert(comment_table, null, content);
        }catch (SQLException e){
            e.printStackTrace();
            Log.e("error", e.toString());
        }
    }

    public void insert_content (Data data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        ContentValues user = new ContentValues();
        user.put("userid", data.getUserId());
        user.put("displayname", data.getDisplayname());
        try{
            db.insert(user_table,null, user);
        }catch (SQLException e){
            e.printStackTrace();
            Log.e("error", e.toString());
        }
        content.put("userid", data.getUserId());
        content.put("content", data.getContent());
        content.put("contentid", data.getContent_id());
        try{
            db.insert(content_table, null, content);
        }catch (SQLException e){
            e.printStackTrace();
            Log.e("error", e.toString());
        }
    }

    public void update() {

    }

    public void delete(Data data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "delete from " + content_table + ", " + comment_table +" where contentid = " + data.getContent_id();
        db.execSQL(sql);
        db.close();
    }

    public Cursor select_content(){
        SQLiteDatabase db = this.getWritableDatabase();
        String str = "select * from " + content_table + " inner join " + user_table + " on "
                 + content_table + ".userid = " + user_table + ".userid";
        Cursor cursor = db.rawQuery(str, null);
        return  cursor;
    }

    public Cursor select_comment(int contentid){
        SQLiteDatabase db = this.getWritableDatabase();
        String str = "select distinct comment.userid, comment.content, comment.contentid, displayname, comment.date from " + comment_table + "," + content_table + " inner join " + user_table + " on "
                + comment_table + ".contentid = " + content_table + ".contentid";
        Cursor cursor = db.rawQuery(str, null);
        return  cursor;
    }
}
