package ncut.cs.wulin.myzone.item_content;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import ncut.cs.wulin.myzone.ListData.Data;
import ncut.cs.wulin.myzone.ListData.SQLiteHelper;
import ncut.cs.wulin.myzone.R;
import ncut.cs.wulin.myzone.login.data.model.LoggedInUser;

public class item_content extends AppCompatActivity {
    Data content_data;
    Button exit;
    Button comment_button;
    Button top;
    TextView tv;
    EditText editText;
    ListView lv;
    ArrayList<Data> listData = new ArrayList<>();
    ScrollView sv;
    Activity activity;
    SQLiteHelper sqLiteHelper;
    SQLiteDatabase db;
    LoggedInUser user;
    private long lastClickTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_content);
        init();
        initEvent();
        moveEditText();
    }

    private void init() {
        tv = findViewById(R.id.item_content);
        comment_button = findViewById(R.id.comment_button);
        top = findViewById(R.id.item_top);
        exit = findViewById(R.id.exit);
        editText = findViewById(R.id.editText);
        lv = findViewById(R.id.comment_item);
        sv = findViewById(R.id.scrollView2);
        activity = item_content.this;
        sqLiteHelper = new SQLiteHelper(activity, 5);
        user = (LoggedInUser) getIntent().getSerializableExtra("user");
        content_data = (Data) getIntent().getSerializableExtra("data");
    }

    @Override
    public void onStart(){
        super.onStart();
        getData();
    }

    private void moveEditText(){
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
    }

    private void initEvent() {
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("state", 1);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
        final comment_item itemAdapter = new comment_item();
        lv.setAdapter(itemAdapter);
        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now= System.currentTimeMillis();
                if(now - lastClickTime >1000) {
                    lastClickTime = now;
                    String content = editText.getText().toString();
                    if (content.equals("")) {
                        editText.setError("输入无效！请重新输入");
                        return;
                    }
                    Data data = new Data(content, user.getUserId(), user.getDisplayName(), content_data.getContent_id());
                    sqLiteHelper.insert_comment(data);
                    getData();
                    editText.setText("");
                    itemAdapter.notifyDataSetChanged();
                    InputMethodManager imm = (InputMethodManager) getSystemService(activity.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        });

        tv.setText(content_data.getContent());
    }

    private void getData(){
        listData.clear();

        db = sqLiteHelper.getWritableDatabase();
        Cursor cursor = sqLiteHelper.select_comment(content_data.getContent_id());
        if(cursor.moveToFirst()){
            try{
                do{
                    int content_col = cursor.getColumnIndex("contentid");
                    int contentid = cursor.getInt(content_col);
                    if(contentid == content_data.getContent_id()){
                        String userid = cursor.getString(0);
                        String content = cursor.getString(1);
                        int dis_col = cursor.getColumnIndex("displayname");
                        int time_col = cursor.getColumnIndex("date");
                        String time = cursor.getString(time_col);
                        String displayname = cursor.getString(dis_col);
                        Data data = new Data(content, userid, displayname, contentid);
                        data.setDate(time);
                        listData.add(data);
                    }
                }while(cursor.moveToNext());
            }catch (SQLException e){
                Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class comment_item extends BaseAdapter {

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int i) {
            return listData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            Viewholder holder;
            if(v == null){
                v = View.inflate(activity, R.layout.comment_item, null);
                holder = new Viewholder();
                holder.comment = v.findViewById(R.id.comment_content);
                holder.name = v.findViewById(R.id.comment_name);
                v.setTag(holder);
            }
            else{
                holder = (Viewholder)v.getTag();
            }
            Data data = listData.get(position);
            holder.comment.setText(data.getContent());
            holder.name.setText(data.getDisplayname());
            return v;
        }
        public void update(){
            notifyDataSetChanged();
        }
        class Viewholder{
            TextView comment;
            TextView name;
        }
    }
}