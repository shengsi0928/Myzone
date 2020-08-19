package ncut.cs.wulin.myzone.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ncut.cs.wulin.myzone.R;
import ncut.cs.wulin.myzone.login.data.model.LoggedInUser;

public class space_fragment extends Fragment {

    View contentView;
    Activity activity;
    LoggedInUser user;
    TextView tv;
    TextView time;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        contentView = inflater.inflate(R.layout.fragment_space_layout, container, false);
        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init()
    {
        Bundle bundle = getArguments();
        user = (LoggedInUser)bundle.getSerializable("LoggedInUser");
        activity = getActivity();
        tv = activity.findViewById(R.id.user_space);
        time = activity.findViewById(R.id.time);
        String string = "欢饮来到个人空间！" + user.getDisplayName();
        tv.setText(string);
        time.setText(getTime());
    }

    private String getTime()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return (year + "年" + month + "月" + day + "日" + hour + "时" + minute + "分");
    }
}
