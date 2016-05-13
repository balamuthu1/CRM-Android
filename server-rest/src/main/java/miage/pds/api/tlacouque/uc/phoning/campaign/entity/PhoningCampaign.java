package miage.pds.api.tlacouque.uc.phoning.campaign.entity;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

/**
 * Created by tlacouque on 08/04/2016.
 */
@Entity("phoningcampaign")
public class PhoningCampaign {

        @Id
        long    campaignId;

        @Property
        String  campaignTheme;

        @Property
        String    campaignType;

        @Property
        String     campaignObjectives;

        @Property
        String statut;

        @Property
        String beginDate;

        @Property
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
}
