package fr.pds.isintheair.crmtab.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import fr.pds.isintheair.crmtab.model.database.OrmTabDataBase;

/**
 * Created by tlacouque on 28/03/2016.
 */

@Table(database = OrmTabDataBase.class)
public class PhoningCampaign extends BaseModel implements Parcelable {

    public static String STATE_DEFINED = "Cree";
    public static String STATE_BEGINED = "En cours";
    public static String STATE_STOPPED = "En pause";
    public static String STATE_ENDED = "Termine";

    @Column
    @PrimaryKey(autoincrement = true)
    long    campaignId;
    @Column
    String  campaignTheme;
    @Column
    String    campaignType;
    @Column
    String     campaignObjectives;
    @Column
    String statut;
    @Column
    String beginDate;
    @Column
    String endDate;

    public PhoningCampaign() {
    }

    public PhoningCampaign( String campaignTheme, String campaignType, String campaignObjectives) {
        this.campaignTheme = campaignTheme;
        this.campaignType = campaignType;
        this.campaignObjectives = campaignObjectives;
    }

    public long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(long campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignTheme() {
        return campaignTheme;
    }

    public void setCampaignTheme(String campaignTheme) {
        this.campaignTheme = campaignTheme;
    }

    public String getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(String campaignType) {
        this.campaignType = campaignType;
    }

    public String getCampaignObjectives() {
        return campaignObjectives;
    }

    public void setCampaignObjectives(String campaignObjectives) {
        this.campaignObjectives = campaignObjectives;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public static final Parcelable.Creator<PhoningCampaign> CREATOR = new Parcelable.Creator<PhoningCampaign>() {
        @Override
        public PhoningCampaign createFromParcel(Parcel in) {
            return new PhoningCampaign(in);
        }

        @Override
        public PhoningCampaign[] newArray(int size) {
            return new PhoningCampaign[size];
        }
    };

    protected PhoningCampaign(Parcel in) {
        campaignId = in.readLong();
        campaignTheme = in.readString();
        campaignType = in.readString();
        campaignObjectives = in.readString();
    }

    /**
     *
     * Return the number of serializable attributes to needed to parse an phoning campaign object
     *
     * @return int
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(campaignId);
        dest.writeString(campaignTheme);
        dest.writeString(campaignType);
        dest.writeString(campaignObjectives);

    }

}
