package fr.pds.isintheair.crmtab.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Truong on 2/10/2016.
 */
public class Prospect{

    @Expose
    @SerializedName("siretNumber")
    private long siretNumber;
    @Expose
    @SerializedName("finessNumber")
    private long finessNumber;
    @Expose
    @SerializedName("turnover")
    private long turnover;
    @Expose
    @SerializedName("streetNumber")
    private int streetNumber;
    @Expose
    @SerializedName("zipCode")
    private int zipCode;
    @Expose
    @SerializedName("bedNumber")
    private int bedNumber;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("streetName")
    private String streetName;
    @Expose
    @SerializedName("town")
    private String town;
    @Expose
    @SerializedName("webSite")
    private String website;
    @Expose
    @SerializedName("etablishmentType")
    private String etablishmentType;


    public Prospect() {
    }


    public long getSiretNumber() {
        return siretNumber;
    }

    public void setSiretNumber(long siretNumber) {
        this.siretNumber = siretNumber;
    }

    public long getFinessNumber() {
        return finessNumber;
    }

    public void setFinessNumber(long finessNumber) {
        this.finessNumber = finessNumber;
    }

    public long getTurnover() {
        return turnover;
    }

    public void setTurnover(long turnover) {
        this.turnover = turnover;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public int getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(int bedNumber) {
        this.bedNumber = bedNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEtablishmentType() {
        return etablishmentType;
    }

    public void setEtablishmentType(String etablishmentType) {
        this.etablishmentType = etablishmentType;
    }
}
