package fr.pds.isintheair.crmtab.view.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.controller.adapter.ContactAdapter;
import fr.pds.isintheair.crmtab.helper.JSONHelper;
import fr.pds.isintheair.crmtab.model.dao.ContactDAO;
import fr.pds.isintheair.crmtab.model.entity.Client;
import fr.pds.isintheair.crmtab.model.mock.Contact;


/**
 * Created by: Julien Datour
 * <p/>
 * Modified by: BALABASCARIN Muthu
 * Date: 02/02/2015
 */
public class ContactListFragment extends Fragment {
    @Bind(R.id.contact_list)
    RecyclerView contactList;
    Client client;

    public ContactListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        ButterKnife.bind(this, view);

        //TODO Remove it when contacts are properly handled
        generateMockedContactsIfNeeded();

        //get args from client list fragment --> args = selected client object
        Bundle bundle = getArguments();
        if (bundle != null) {
            client = (Client) bundle.getSerializable("client");
        }

        List<Contact>  contacts       = ContactDAO.getAll();
        ContactAdapter contactAdapter = new ContactAdapter(getContext(), contacts, client);

        contactList.setAdapter(contactAdapter);
        contactList.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact, menu);
    }

    private void generateMockedContactsIfNeeded() {
        List<Contact> databaseContacts = ContactDAO.getAll();

        if (databaseContacts.size() == 0) {
            ContactDAO.delete();
            StringBuilder stringBuilder = new StringBuilder();
            InputStream inputStream = null;

            try {
                inputStream = getContext().getAssets().open("contact-mock.json");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String input;

                while ((input = bufferedReader.readLine()) != null) {
                    stringBuilder.append(input);
                }

                bufferedReader.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            String contactJson = stringBuilder.toString();
            Contact[] contactsJSon = (Contact[]) JSONHelper.deserialize(contactJson, Contact[].class);
            List<Contact> contacts = Arrays.asList(contactsJSon);

            for (int i = 0; i < contacts.size(); ++i) {
                Contact contact = contacts.get(i);
                contact.save();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addcontact:
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new AddContactFragment());
                fragmentTransaction.commit();
                return true;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
