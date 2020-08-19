package ncut.cs.wulin.myzone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import ncut.cs.wulin.myzone.ListData.Data;
import ncut.cs.wulin.myzone.ListData.SQLiteHelper;
import ncut.cs.wulin.myzone.fragments.home_fragment;
import ncut.cs.wulin.myzone.fragments.send_fragment;
import ncut.cs.wulin.myzone.fragments.space_fragment;
import ncut.cs.wulin.myzone.login.data.model.LoggedInUser;


public class main_page extends AppCompatActivity implements send_fragment.addData2ListData, Serializable {

    private RadioGroup rg ;
    private RadioButton rb1 ;
    private RadioButton rb2 ;
    private RadioButton rb3 ;
    private home_fragment fragments1;
    private space_fragment fragments2;
    private send_fragment fragments3;
    private final int home_fragment_num = 0;
    private final int space_fragment = 1;
    private final int send_fragment = 2;
    LoggedInUser user;
    private FragmentManager fm = getSupportFragmentManager();
    private FragmentTransaction ft;
    private Data temp_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkFragment(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        initViews();
        initEvents();
        rb1.performClick();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if(fragments1 == null && fragment instanceof home_fragment) {
            fragments1 = (home_fragment) fragment;
        }
        if(fragments2 == null && fragment instanceof space_fragment) {
            fragments2 = (ncut.cs.wulin.myzone.fragments.space_fragment) fragment;
        }
        if(fragments3 == null && fragment instanceof send_fragment) {
            fragments3 = (send_fragment)fragment;
            fragments3.setAddDataListener(this);
        }
    }

    private void initViews() {
        rg = findViewById(R.id.bottom_bar);
        rb1 = findViewById(R.id.home_button);
        rb2 = findViewById(R.id.space_button);
        rb3 = findViewById(R.id.send_button);
        user = (LoggedInUser) getIntent().getSerializableExtra("LoggedInUser");
    }

    private void initEvents() {
        final Bundle bundle = new Bundle();
        bundle.putSerializable("LoggedInUser", user);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.home_button: {
                        select(0, bundle);
                        rb1.setEnabled(false);
                        rb2.setEnabled(true);
                        rb3.setEnabled(true);
                        break;
                    }
                    case R.id.space_button:{
                        select(1, bundle);
                        fragments2.setArguments(bundle);
                        rb1.setEnabled(true);
                        rb2.setEnabled(false);
                        rb3.setEnabled(true);
                        break;
                    }
                    case R.id.send_button:{
                        select(2, bundle);
                        fragments3.setArguments(bundle);
                        rb1.setEnabled(true);
                        rb2.setEnabled(true);
                        rb3.setEnabled(false);
                        break;
                    }

                }
            }
        });
    }

    private void checkFragment(Bundle savedInstanceState) {
        if(savedInstanceState != null)
        {
            fragments1 = (home_fragment) fm.findFragmentByTag("home_fragment");
            fragments2 = (space_fragment) fm.findFragmentByTag("space_fragment");
            fragments3 = (send_fragment) fm.findFragmentByTag("send_fragment");
        }
    }

    private void select(int i, Bundle bundle) {
        ft = fm.beginTransaction();
        hideFragment(ft);
        switch (i) {
            case home_fragment_num:{
                if(fragments1 == null) {
                    fragments1 = new home_fragment();
                    ft.add(R.id.fragment_container, fragments1, "home_fragment");
                    fragments1.setArguments(bundle);
                }
                else {
                    ft.show(fragments1);
                }
                break;

            }
            case space_fragment: {
                if(fragments2 == null) {
                    fragments2 = new space_fragment();
                    ft.add(R.id.fragment_container, fragments2,"space_fragment");
                    fragments2.setArguments(bundle);
                }
                else {
                    ft.show(fragments2);
                }
                break;
            }
            case send_fragment: {
                if(fragments3 == null) {
                    fragments3 = new send_fragment();
                    ft.add(R.id.fragment_container, fragments3,"send_fragment");
                    fragments3.setArguments(bundle);
                }
                else {
                    ft.show(fragments3);
                }
                break;
            }

        }
        ft.commit();
    }

    private void hideFragment(FragmentTransaction ft) {

        if(fragments1 != null && !fragments1.isHidden()) {
            ft.hide(fragments1);
        }
        if(fragments2 != null && !fragments2.isHidden()) {
            ft.hide(fragments2);
        }
        if(fragments3 != null && !fragments3.isHidden()) {
            ft.hide(fragments3);
        }
    }

    @Override
    public void dataTransmit(Data data, View send) {
        temp_data = data;
        String str = temp_data.getDisplayname() + "的说说发表成功！";
        if(fragments1 != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(send.getWindowToken(), 0);
            }
            fragments1.add_Data(temp_data);
            Toast.makeText(main_page.this, str, Toast.LENGTH_SHORT).show();
            rb1.performClick();
        }
    }
}
