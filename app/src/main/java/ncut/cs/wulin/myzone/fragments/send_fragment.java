package ncut.cs.wulin.myzone.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ncut.cs.wulin.myzone.ListData.Data;
import ncut.cs.wulin.myzone.R;
import ncut.cs.wulin.myzone.login.data.model.LoggedInUser;

public class send_fragment extends Fragment {

    private View contentView;
    private Activity activity;
    private LoggedInUser user;
    private EditText et;
    private Button send;
    private Data data;
    private addData2ListData addDataListener;

    private long lastClickTime = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_send_layout, container, false);
        return contentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            if(context != null){
                addDataListener = (addData2ListData) context;
            }
        }catch (Exception e){
            throw new ClassCastException(context.toString()
                + "must implement addData2ListData");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        activity = getActivity();
        Bundle bundle = getArguments();
        user = (LoggedInUser)bundle.getSerializable("LoggedInUser");
        et = activity.findViewById(R.id.send_content);
        send = activity.findViewById(R.id.send);
        initEvent();
    }

    private void initEvent() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now= System.currentTimeMillis();
                if(now - lastClickTime >1000){
                    lastClickTime = now;
                    String content = et.getText().toString();
                    if(content==null){
                        return ;
                    }
                    data = new Data();
                    data.setDisplayname(user.getDisplayName());
                    data.setUserId(user.getUserId());
                    data.update_content(content);
                    addDataListener.dataTransmit(data, send);
                    et.setText("");
                }
            }
        });
    }
    
    public interface addData2ListData{
        public void dataTransmit(Data data, View view);
    }
    
    public void setAddDataListener(addData2ListData listener){
        this.addDataListener = listener;
    }

}
