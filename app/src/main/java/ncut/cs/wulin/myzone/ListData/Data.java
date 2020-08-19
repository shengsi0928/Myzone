package ncut.cs.wulin.myzone.ListData;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Data implements Serializable {
    private String userId;
    private String content;
    private String displayname;
    private int content_id;
    private int like_count;
    String date;
    public Data() {
        Random ran = new Random();
        userId = ran.toString();
        content = "";
        like_count = 0;
        displayname = "";
    }

    public Data(String _user, String _displayname, int contentid) {
        userId = _user;
        content = "";
        content_id = contentid;
        like_count = 0;
        displayname = _displayname;
    }

    public Data(String _content, String _user,  String _displayname, int contentid) {
        userId = _user;
        content = _content;
        content_id = contentid;
        like_count = 0;
        displayname = _displayname;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getContent()
    {
        return content;
    }

    public String getDisplayname(){
        return displayname;
    }

    public String getTime(){
        return date;
    }

    public int getContent_id(){
        return content_id;
    }

    public int getLike_count(){
        return like_count;
    }

    public int setContent_id(int contentId){
        content_id = contentId;
        return content_id;
    }
    public void setDate(String date){
        this.date = date;
    }

    public void setDisplayname(String displayname){
        this.displayname = displayname;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String update_content(String _content) {
        content = _content;
        return content;
    }

}
