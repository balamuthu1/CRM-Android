package fr.pds.isintheair.crmtab.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.controller.message.CallMessageController;
import fr.pds.isintheair.crmtab.helper.NetworkHelper;
import fr.pds.isintheair.crmtab.helper.ResourceHelper;
import fr.pds.isintheair.crmtab.model.mock.Contact;
import fr.pds.isintheair.crmtab.model.entity.Client;

public class ContactDetailActivity extends Activity {
    Contact currentContact;
    Client currentClient;

    @Bind(R.id.txtName)
    TextView txtName;

    @Bind(R.id.txtFname)
    TextView txtFname;

    @Bind(R.id.txtTel)
    TextView txtTel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        ButterKnife.bind(this);
        currentContact = getIntent().getParcelableExtra("contact");
        currentClient = (Client) getIntent().getSerializableExtra("client");

        if (currentContact != null) {

            txtName.setText(currentContact.getFirstName());
            txtFname.setText(currentContact.getLastName());
            txtTel.setText(currentContact.getPhoneNumber());

        }

    }


    @OnClick(R.id.imgSms)
    public void onSmsClick() {
        //TODO Implement click event
    }

    @OnClick(R.id.imgCall)
    public void onPhoneClick() {
        if (currentContact != null) {
            if (NetworkHelper.isNetworkAvailable()) {
                CallMessageController.call(currentContact.getPhoneNumber());
            }

            else {
                Toast.makeText(this, ResourceHelper.getString(R.string.message_failed_no_internet), Toast.LENGTH_LONG).show();
            }
        }
    }

    @OnClick(R.id.imgVisit)
    public void onVisitClick() {
        if(currentClient != null && currentContact != null){
            Intent visitActivity = new Intent(this, VisitActivity.class);
            visitActivity.putExtra("contact", currentContact);
            visitActivity.putExtra("client", currentClient);
            startActivity(visitActivity);
        }
    }

    @OnClick(R.id.imgAgenda)
    public void onAgendaClick() {
        //TODO Implement click event
    }
}
