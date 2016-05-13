package fr.pds.isintheair.crmtab.controller.message;

import java.util.List;

import fr.pds.isintheair.crmtab.model.entity.Customer;
import fr.pds.isintheair.crmtab.model.entity.HealthCenter;
import fr.pds.isintheair.crmtab.model.entity.Independant;
import fr.pds.isintheair.crmtab.model.entity.ResponseRestCustomer;
import fr.pds.isintheair.crmtab.view.fragment.CreatePhoningCampaignFragment;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by tlacouque on 26/04/2016.
 */
public class CallBackGetOtherCustomers implements Callback<ResponseRestCustomer> {

    CreatePhoningCampaignFragment fragment;
    List<HealthCenter> healthCenterList;
    List<Customer> customers;

    public CallBackGetOtherCustomers() {
    }

    public CallBackGetOtherCustomers(CreatePhoningCampaignFragment fragment, List<HealthCenter> list,
                                     List<Customer> customers) {
        this.fragment = fragment;
        this.healthCenterList = list;
        this.customers = customers;
    }

    @Override
    public void onResponse(Response<ResponseRestCustomer> response, Retrofit retrofit) {

        if (response.errorBody() == null) {
            List<HealthCenter> healthCenters = response.body().getHealthCenters();
            List<Independant> independants = response.body().getIndependants();
            if (healthCenters != null) {
                for (HealthCenter healthCenter : response.body().getHealthCenters()) {
                    boolean alreadyOnTablet = false;
                    for (HealthCenter healthCenterTab : healthCenterList) {
                        if (healthCenterTab.getSiretNumber() == healthCenter.getSiretNumber()) {
                            alreadyOnTablet = true;
                        }
                    }
                    if (!alreadyOnTablet) {
                        customers.add(healthCenter);
                    }

                }
            }
            if (independants != null) {
                for (Independant independant : response.body().getIndependants()) {
                    customers.add(independant);
                }
            }
            fragment.initAdapter(customers);
            fragment.isCampaignAlreadySet();
        } else {
            onFailure(null);
        }
    }

    @Override
    public void onFailure(Throwable t) {
        fragment.callRestFailed();
    }
}
