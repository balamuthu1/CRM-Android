package miage.pds.api.common.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ClockinObject {


    private User user;
    private String tagId;
    private String date;
    private String time;

    public ClockinObject() {
        // TODO Auto-generated constructor stub
    }

    public ClockinObject(User user, String tagId) {
        this.user = user;
        this.tagId = tagId;
        Calendar c = Calendar.getInstance(Locale.FRANCE);
        date = c.get(c.DAY_OF_MONTH)+"-"+c.get(c.MONTH)+"-"+c.get(c.YEAR);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        time = sdf.format(new Date());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}