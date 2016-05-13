package miage.pds.api.jdatour;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.prefs.Preferences;

@Controller
public class DatabaseController {
    private String PREF_KEY_DUMP_FILE_ID = "dumpFileId";

    @RequestMapping(value = "/database/dump", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    ResponseEntity<String> dumpDatabase (@RequestBody String dumpDatabaseRequest) {
        String  dumpCommand    = "/usr/bin/mongodump --db crm --out /home/mongodb";
        Integer dumpReturnCode = Shell.executeCommand(dumpCommand);

        if (dumpReturnCode != null && dumpReturnCode != 0) {
            return new ResponseEntity<String>("Dump went wrong : " + dumpReturnCode, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String  tarCommand    = "/bin/tar -zcvf /home/mongodb/crm-backup.tar.gz -C /home/mongodb crm";
        Integer tarReturnCode = Shell.executeCommand(tarCommand);

        if (tarReturnCode != null && tarReturnCode != 0) {
            return new ResponseEntity<String>("Tar went wrong : " + tarReturnCode, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        byte[] backupFileDatas = null;

        try {
            Path path = Paths.get("/home/mongodb/crm-backup.tar.gz");

            backupFileDatas = Files.readAllBytes(path);

            if (backupFileDatas == null) {
                return new ResponseEntity<String>("No data found in backup file", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            return new ResponseEntity<String>("Exception found while trying to read backup file : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            InputStream      secretStream = DatabaseController.class.getClassLoader().getResourceAsStream("ESIAG-PDS-2eac60e12628.json");
            GoogleCredential credential   = GoogleCredential.fromStream(secretStream).createScoped(Collections.singleton("https://www.googleapis.com/auth/drive"));

            if (!credential.refreshToken()) {
                return new ResponseEntity<String>("Could not get a new token from drive api", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String                       authorization = "Bearer " + credential.getAccessToken();
            Call<UploadFileResponse>     call          = RetrofitHandlerSingleton.getInstance().getDriveApi().updloadFile(authorization, backupFileDatas);
            Response<UploadFileResponse> response      = call.execute();

            if (response.body() != null) {
                Preferences.userRoot().node(this.getClass().getName()).put(PREF_KEY_DUMP_FILE_ID, response.body().id);
            } else {
                return new ResponseEntity<String>("Drive api response error : " + response.errorBody(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            return new ResponseEntity<String>("Exception found while trying to use drive api : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(Preferences.userRoot().node(this.getClass().getName()).get(PREF_KEY_DUMP_FILE_ID, ":("), HttpStatus.OK);
    }

    @RequestMapping(value = "/database/restore", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    ResponseEntity<String> restoreDatabase (@RequestBody String restoreDatabaseRequest) {
        InputStream      secretStream = DatabaseController.class.getClassLoader().getResourceAsStream("ESIAG-PDS-2eac60e12628.json");
        GoogleCredential credential   = null;

        try {
            credential = GoogleCredential.fromStream(secretStream).createScoped(Collections.singleton("https://www.googleapis.com/auth/drive"));
            credential.refreshToken();

            String       fileId        = Preferences.userRoot().node(this.getClass().getName()).get(PREF_KEY_DUMP_FILE_ID, "");
            String       authorization = "Bearer " + credential.getAccessToken();
            Call<byte[]> call          = RetrofitHandlerSingleton.getInstance().getDriveApi().downloadFile(authorization, fileId);

            Response<byte[]> response = call.execute();

            if (response.errorBody() != null) {
                return new ResponseEntity<String>("Something went wrong with drive downloading file : " + response.errorBody(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (response.body() != null) {
                String  path                  = "/home/mongodb/crm-restore.tar.gz";
                File    file                  = new File(path);
                Boolean fileCreationSucceeded = false;

                file.getParentFile().mkdirs();
                file.createNewFile();

                FileOutputStream fileOutputStream = new FileOutputStream(path);

                fileOutputStream.write(response.body());
                fileOutputStream.close();
            }
        } catch (IOException e) {
            return new ResponseEntity<String>("Exception : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String  tarCommand    = "/bin/tar -zxvf /home/mongodb/crm-restore.tar.gz -C /home/mongodb";
        Integer tarReturnCode = Shell.executeCommand(tarCommand);

        if (tarReturnCode != null && tarReturnCode != 0) {
            return new ResponseEntity<String>("Tar went wrong : " + tarReturnCode, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String  restoreCommand    = "/usr/bin/mongorestore --db crm /home/mongodb/crm";
        Integer restoreReturnCode = Shell.executeCommand(restoreCommand);

        if (restoreReturnCode != null && restoreReturnCode != 0) {
            return new ResponseEntity<String>("Restore went wrong : " + restoreReturnCode, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }
}