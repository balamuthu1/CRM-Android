package fr.pds.isintheair.crmtab.helper;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

import fr.pds.isintheair.crmtab.model.dao.ContactDAO;
import fr.pds.isintheair.crmtab.model.mock.Contact;

/**
 * Created by jbide on 10/05/2016.
 */
public class ContactHelper {

    public static String getGroupId(String groupTitle,ContentResolver res) {


        String groupId = null;
        Cursor cursor = res.query(ContactsContract.Groups.CONTENT_URI, new String[]{ContactsContract.Groups._ID, ContactsContract.Groups.TITLE}, null, null, null);
        cursor.moveToFirst();
        int len = cursor.getCount();


        for (int i = 0; i < len; i++) {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));
            String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));

            if (title.equals(groupTitle)) {
                groupId = id;
                break;
            }
            cursor.moveToNext();
        }
        cursor.close();

        //create group if not exist
        if(groupId==null){
            ContentValues groupValues;
            groupValues = new ContentValues();
            groupValues.put(ContactsContract.Groups.TITLE, "CRM");
            res.insert(ContactsContract.Groups.CONTENT_URI, groupValues);
            }

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        try{
            res.applyBatch(ContactsContract.AUTHORITY, ops);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        return groupId;
    }

    public static void addContactinPhoneDatabase(ContentResolver res,String name, String number) {

        String DisplayName = name;
        String MobileNumber = number;
        /*String HomeNumber = "1111";
        String WorkNumber = "2222";
        String emailID = "email@nomail.com";
        String company = "bad";
        String jobTitle = "abcd";*/

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        if (DisplayName != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            DisplayName).build());
        }

        //------------------------------------------------------ Mobile Number
        if (MobileNumber != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        //------------------------------------------------------ Home Numbers
        /*if (HomeNumber != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
        }

        //------------------------------------------------------ Work Numbers
        if (WorkNumber != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, WorkNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Email
        if (emailID != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Organization
        if (!company.equals("") && !jobTitle.equals("")) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build());
        }*/
        //Add contact to group CRM
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID, ContactHelper.getGroupId("CRM", res))
                .build());

        // Asking the Contact provider to create a new contact
        try {
            res.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(myContext, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void updateContactinAppDatabase(ContentResolver res) {

        String id, name, phone, hasPhone;
        int idx;



        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID ,
                ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID};

        Cursor cur = res.query(ContactsContract.Data.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID+"="+ContactHelper.getGroupId("CRM", res),
                null,null);

        List<String> names = new ArrayList<String>();
        List<Contact> databaseContacts = ContactDAO.getAll();

        if(cur.moveToFirst()) {

            while (cur.isAfterLast() == false) {
                String nam  = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                names.add(nam);
                //not working so i am made to get the name & search the number related
                //String phoneNumber1 = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER.toString()));

                // Using the contact ID now we will get contact phone number
                Cursor cursorPhone = res.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ? ",
                        new String[]{nam},
                        null);
                boolean a= false;
                if (cursorPhone.moveToFirst()) {
                    String contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    for (Contact co:databaseContacts
                         ) {
                        if(co.getFirstName().equalsIgnoreCase(nam)){
                            a=true;
                            break;
                        }
                        else if(co.getPhoneNumber()==contactNumber){
                            a=true;
                            break;
                        }
                    }
                }

                cursorPhone.close();



                cur.moveToNext();
            }
        }

        cur.close();


        /*Contact co = new Contact();
        //add contact in app database
        co.setFirstName(fname);
        co.setLastName(lname);
        co.setPhoneNumber(number);
        co.save();*/

    }
}
