package miage.pds.api.jdatour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Shell {
    public static Integer executeCommand (String command) {
        Process cmdProc = null;

        try {
            cmdProc = Runtime.getRuntime().exec(command);
            cmdProc.waitFor();
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if (cmdProc != null) {
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(cmdProc.getErrorStream()));
            String         line         = "";

            try {
                while ((line = stdoutReader.readLine()) != null) {
                    System.out.println("stdout : " + line);
                }
                while ((line = stderrReader.readLine()) != null) {
                    System.out.println("stderr : " + line);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return cmdProc.exitValue();
        }

        return null;
    }
}
