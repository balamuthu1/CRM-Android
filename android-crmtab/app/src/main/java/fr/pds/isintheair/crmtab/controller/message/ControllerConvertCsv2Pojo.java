package fr.pds.isintheair.crmtab.controller.message;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.pds.isintheair.crmtab.model.entity.Contact;

/**
 * Created by Muthu on 02/03/2016.
 */
public class ControllerConvertCsv2Pojo {

    public List<Contact> convertCsv2Contact(File csvContent){
        List<Contact> contacts = new ArrayList<Contact>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(csvContent));

            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                Contact contact = new Contact();
                contact.setContactId(Integer.parseInt(line.split(";")[0]));
                contact.setContactName(line.split(";")[1]);
                contact.setContactFname(line.split(";")[2]);
                contact.setContactTel(line.split(";")[3]);
                contact.setContactJob(line.split(";")[4]);
                contact.setContactStatus(line.split(";")[5]);
                contact.setClientId(Long.getLong(line.split(";")[6]).longValue());

                contacts.add(contact);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contacts;
    }
}
