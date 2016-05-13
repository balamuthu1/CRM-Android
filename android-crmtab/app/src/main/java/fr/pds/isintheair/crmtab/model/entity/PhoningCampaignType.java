package fr.pds.isintheair.crmtab.model.entity;

/**
 * Created by tlacouque on 27/03/2016.
 */
public enum PhoningCampaignType {

    PROPOSED_A_PRODUCT("Proposer un produit"),
    NEW_SERVICE("Proposer un nouveau service"),
    ADDITIONNAL_SERVICE("Proposer un service additionnel");

    private String stringValue;

    private PhoningCampaignType(String toString) {
        stringValue = toString;
    }

    /**
     * Method implemented to call Enum.attribute.toString(). Return string of this attribute.
     * @return String
     */
    @Override
    public String toString() {
        return stringValue;
    }
}
