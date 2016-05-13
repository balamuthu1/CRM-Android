package fr.pds.isintheair.crmtab.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.controller.message.ControllerContactRetrofit;
import fr.pds.isintheair.crmtab.controller.message.ControllerConvertCsv2Pojo;
import fr.pds.isintheair.crmtab.controller.message.ControllerReadFile;
import fr.pds.isintheair.crmtab.model.entity.Contact;

public class ImportContactActivity extends Activity {

    String path;
    private static final int FILE_SELECT_CODE = 0;
    final private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_contact);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted

                } else {

                    // permission denied
                }
                return;
            }


        }
    }


    public void importCsv(View view){
        showFileChooser();
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Choisir un fichier csv pour importer"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    //Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path
                    try {
                        path = ControllerReadFile.getPath(this, uri);
                        Log.d("File Uri: ", path);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    //Log.d(TAG, "File Path: " + path);
                    // Get the file instance
                    File file = new File(path);

                    //make conversion: csv to contact object
                    List<Contact> contacts ;
                    contacts = new ControllerConvertCsv2Pojo().convertCsv2Contact(file);
                    for(Contact contact : contacts){
                        new ControllerContactRetrofit().addContacts(contact,this);
                    }

                    // Initiate the upload

                    Toast.makeText(ImportContactActivity.this, "size: "+ contacts.size(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
