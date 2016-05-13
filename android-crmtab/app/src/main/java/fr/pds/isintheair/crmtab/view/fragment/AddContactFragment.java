package fr.pds.isintheair.crmtab.view.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.helper.ContactHelper;
import fr.pds.isintheair.crmtab.jbide.uc.registercall.ControllerContact;
import fr.pds.isintheair.crmtab.model.mock.Contact;


/**
 * Created by jbide on 09/05/2016.
 */
public class AddContactFragment extends Fragment {
    @Bind(R.id.edittextnumber)
    EditText number;
    @Bind(R.id.edittextfname)
    EditText fname;
    @Bind(R.id.edittextlname)
    EditText lname;
    @Bind(R.id.registercontact)
    Button register;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addcontact, container, false);
        ButterKnife.bind(this, view);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact co = new Contact();

                //add contact in app database
                co.setFirstName(fname.getText().toString());
                co.setLastName(lname.getText().toString());
                co.setPhoneNumber(number.getText().toString());
                co.save();

                //add contact in phone contacts
                //fname lname???
                ContactHelper.addContactinPhoneDatabase(getActivity().getContentResolver(), fname.getText().toString(), number.getText().toString());

                //add contact in server
                fr.pds.isintheair.crmtab.model.entity.Contact con = new fr.pds.isintheair.crmtab.model.entity.Contact();
                con.setContactFname(fname.getText().toString());
                con.setContactName(lname.getText().toString());
                con.setContactTel(number.getText().toString());
                ControllerContact.registerContact(con,getActivity());

                //redirect
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new ContactListFragment());
                fragmentTransaction.commit();
            }
        });
        return view;
    }
}
