package ncut.cs.wulin.myzone.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;
import java.util.ArrayList;

import ncut.cs.wulin.myzone.ListData.Data;
import ncut.cs.wulin.myzone.ListData.SQLiteHelper;
import ncut.cs.wulin.myzone.R;
import ncut.cs.wulin.myzone.item_content.item_content;
import ncut.cs.wulin.myzone.login.data.model.LoggedInUser;

public class home_fragment extends Fragment {

    SQLiteHelper sqLiteHelper;
    SQLiteDatabase db;
    View contentView;
    ArrayList<Data> listData = new ArrayList<>();

    MyListAdapter listAdapter;
    ListView listView ;
    Activity activity;
    LoggedInUser user;
    home_fragment fragment;
    FragmentTransaction ft;
    FragmentManager fm;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm = getActivity().getSupportFragmentManager();
        ft = fm.beginTransaction();
        fragment = this;
        contentView = inflater.inflate(R.layout.fragment_home_layout, container, false);
        listAdapter = new MyListAdapter();
        Bundle bundle = getArguments();
        assert bundle != null;

        user = (LoggedInUser)bundle.getSerializable("LoggedInUser");
        return contentView;
    }

    @Override
    public void onStart(){
        super.onStart();
        initComponent();
        setListAdapter();
    }

    private void initComponent(){
        activity = getActivity();
        listView = activity.findViewById(R.id.list);
        sqLiteHelper = new SQLiteHelper(activity, 5);
        db = sqLiteHelper.getWritableDatabase();
        getData();
    }

    private void getData(){
        listData.clear();
        Cursor cursor = sqLiteHelper.select_content();
        if(cursor.moveToFirst()){
            try{
//                int countid = 1;
                do{
                    String userid = cursor.getString(0);
                    String content = cursor.getString(1);
                    int dis_col = cursor.getColumnIndex("displayname");
                    int time_col = cursor.getColumnIndex("date");
                    String time = cursor.getString(time_col);
                    String displayname = cursor.getString(dis_col);
                    int content_col = cursor.getColumnIndex("contentid");
                    int contentid = cursor.getInt(content_col);
                    Data data = new Data(content, userid, displayname, contentid);
                    data.setDate(time);
                    listData.add(data);
                }while(cursor.moveToNext());
            }catch (SQLException e){
                Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void add_Data(int num) {
        String user = "临时名称";
        for(int i = 1; i<=num; i++) {
            String content = "这是第" + i + "条说说内容";
            String temp_name = "第" + i + "个人";
            Data data = new Data(content, temp_name, user, i);
            try {
                sqLiteHelper.insert_content(data);
            }catch (SQLException e){
                Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean add_Data(Data data){
        try {
            int count = listData.size();
            count++;
            data.setContent_id(count);
            sqLiteHelper.insert_content(data);
            getData();
            return true;
        }catch (SQLException e){
            Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void setListAdapter() {

        listView.setAdapter(new MyListAdapter());
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    class MyListAdapter extends BaseAdapter {
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
        public View getView(final int i, View v, ViewGroup viewGroup) {
            ViewHolder holder;
            if (v == null) {
                v =  View.inflate(activity, R.layout.item, null);
                holder = new ViewHolder();
                initHolder(holder, v, i);
                initView(v, i);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
            Data data = listData.get(i);
            setText(data, holder);
            return v;
        }

        private void initHolder(ViewHolder holder, View v, final int i){
            holder.tv = v.findViewById(R.id.item_view);
            holder.username = v.findViewById(R.id.displayname);
            holder.time = v.findViewById(R.id.time);
            holder.delete = v.findViewById(R.id.delete);
            holder.contentid = v.findViewById(R.id.contentid);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        sqLiteHelper.delete(listData.get(i));
                    }catch (Exception e){
                        Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
                    }
                    getData();
                    notifyDataSetChanged();
                }
            });
        }

        private void initView(View v, final int i){
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, item_content.class);
                    Data data = listData.get(i);
                    intent.putExtra("user", user);
                    intent.putExtra("data", data);
                    startActivityForResult(intent, 1);
                }
            });
        }

        private void setText(Data data, ViewHolder holder){
            holder.tv.setText(data.getContent());
            holder.username.setText(data.getDisplayname());
            holder.time.setText(data.getTime());
            try{
                holder.contentid.setText(data.getContent_id()+"");
            }catch (Exception e){
                Toast.makeText(activity, e.toString(),Toast.LENGTH_LONG).show();
            }
        }

        class ViewHolder {
            TextView tv;
            TextView username;
            TextView time;
            TextView contentid;
            Button delete;
        }
    }
}