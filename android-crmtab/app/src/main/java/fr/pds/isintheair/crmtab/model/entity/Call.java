package fr.pds.isintheair.crmtab.model.entity;

public class Call {
    private String phoneNumber;

    public Call(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
