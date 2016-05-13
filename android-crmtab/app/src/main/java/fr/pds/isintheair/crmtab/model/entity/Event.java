package fr.pds.isintheair.crmtab.model.entity;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Calendar;

import fr.pds.isintheair.crmtab.model.database.OrmTabDataBase;

/******************************************
 * Created by        :                    *
 * Creation date     : 03/19/16            *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

@Table(database = OrmTabDataBase.class)
public class Event extends BaseModel {
    @PrimaryKey
    @Column
    Long id;

    @Column
    private String title;

    @Column
    private Calendar startTime;

    @Column
    private Calendar endTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }
}
