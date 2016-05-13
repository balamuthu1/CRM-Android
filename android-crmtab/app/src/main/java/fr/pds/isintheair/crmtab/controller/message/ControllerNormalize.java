package fr.pds.isintheair.crmtab.controller.message;

import android.annotation.SuppressLint;
import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by Muthu on 17/03/2016.
 */
public class ControllerNormalize {

    public String normalizeNumber(String number){

        return PhoneNumberUtils.normalizeNumber(number);
    }

    public String formatTelNumber(String number){
        return PhoneNumberUtils.formatNumber(normalizeNumber(number), Locale.getDefault().getCountry());
    }

    public boolean checkIfRightNumber(String phoneNumber){
        return PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber);
    }

    public String getLocal(){

        return  Locale.getDefault().getCountry();
    }

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    public boolean isValidEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public String removeDiacritics(String input)
    {
        String nrml = Normalizer.normalize(input, Normalizer.Form.NFD);
        StringBuilder stripped = new StringBuilder();
        for (int i=0;i<nrml.length();++i)
        {
            if (Character.getType(nrml.charAt(i)) != Character.NON_SPACING_MARK)
            {
                stripped.append(nrml.charAt(i));
            }
        }
        return stripped.toString();
    }

    @SuppressLint("NewApi")
    public String asciiNormalizar(String s) {
        String str;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
		/* Use Normalizer normally */
            str = Normalizer.normalize(s, Normalizer.Form.NFD);
            str = str.replaceAll("[^\\p{ASCII}]", "");
            str = str.replaceAll(" ", "%20");
            return str.toLowerCase();
        } else {

            str = s;
            str = str.replaceAll("[çÇ]+", "c");
            str = str.replaceAll("[ãÃáÁ]+", "a");
            str = str.replaceAll("[éÉ]+", "e");
            str = str.replaceAll("[íÍ]+", "i");
            str = str.replaceAll("[õÕóÓ]+", "o");
            str = str.replaceAll("[úÚ]+", "u");

            str = str.replaceAll("[^\\p{ASCII}]", "");
            str = str.replaceAll(" ", "%20");
            Log.d("Teste", str);
            return str.toLowerCase();
        }
    }


}
