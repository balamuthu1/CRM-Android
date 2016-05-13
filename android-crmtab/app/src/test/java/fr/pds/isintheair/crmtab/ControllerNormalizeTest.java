package fr.pds.isintheair.crmtab;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import fr.pds.isintheair.crmtab.controller.message.ControllerNormalize;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Muthu on 20/03/2016.
 */
public class ControllerNormalizeTest {
    ControllerNormalize controllerNormalize;

    @Before
    public void init() throws Exception {
        Log.d("normalize", "Tests started");
        controllerNormalize = new ControllerNormalize();
    }

    /* @Test
     public void testNormalizeNumber() throws Exception {
         String tel = "07 78 80 17 08";
         String normalized = controllerNormalize.normalizeNumber(tel);
         assertTrue(controllerNormalize.checkIfRightNumber(normalized));
     }*/
    @Test
    public void testNormalizeNumberWithNonCorrectTelephone() throws Exception {
        String tel        = "1234567890";
        String normalized = controllerNormalize.normalizeNumber(tel);
        assertFalse(controllerNormalize.checkIfRightNumber(normalized));
    }

    /* @Test
    public void testNormalizeNumberStartingWithInternationalCode() throws Exception {
        String tel = "+33778801708";
        String normalized = controllerNormalize.normalizeNumber(tel);
        assertTrue(controllerNormalize.checkIfRightNumber(normalized));
    }*/
    @Test
    public void testNormalizeNumberStartingWithWrongInternationalCode() throws Exception {
        String tel        = "+330778801708";
        String normalized = controllerNormalize.normalizeNumber(tel);
        assertFalse(controllerNormalize.checkIfRightNumber(normalized));
    }


    @Test
    public void testGetLocal() throws Exception {
        Boolean isCorrect = false;
        String  local     = controllerNormalize.getLocal();
        if (local.contains("FR"))
            isCorrect = true;

        assertTrue(isCorrect);
    }

    @Test
    public void testIsValidEmail() throws Exception {
        Boolean isCorrect = false;
        String  mail      = "test@crm.fr";
        isCorrect = controllerNormalize.isValidEmail(mail);
        assertTrue(isCorrect);
    }

    @Test
    public void testIsNotValidEmail() throws Exception {
        Boolean isCorrect;
        String  mail = "testcrm.fr";
        isCorrect = controllerNormalize.isValidEmail(mail);
        assertFalse(isCorrect);
    }

    @Test
    public void testIsNotValidEmailOnlySymbols() throws Exception {
        Boolean isCorrect;
        String  mail = "@.";
        isCorrect = controllerNormalize.isValidEmail(mail);
        assertFalse(isCorrect);
    }

    @Test
    public void testIsNotValidEmailMisplacedSymbols() throws Exception {
        Boolean isCorrect;
        String  mail = "com.crm@test";
        isCorrect = controllerNormalize.isValidEmail(mail);
        assertFalse(isCorrect);
    }

    @Test
    public void testRemoveDiacritics() throws Exception {
        Boolean isCorrect     = false;
        String  text          = "é, è, ê, ë, à, â, î, ï, ô, ù, û, ü, ÿ, æ, œ et ç";
        String  normaizedText = controllerNormalize.removeDiacritics(text);
        if (!normaizedText.equals(text)) {
            isCorrect = true;
        }
        assertTrue(isCorrect);
    }

    @Test
    public void testRemoveDiacriticsInNormalPhrase() throws Exception {
        Boolean isCorrect     = false;
        String  text          = "client satisfait";
        String  normaizedText = controllerNormalize.removeDiacritics(text);
        if (!normaizedText.equals(text)) {
            isCorrect = true;
        }
        assertFalse(isCorrect);
    }

    @Test
    public void testAsciiNormalizar() throws Exception {
        Boolean isCorrect     = false;
        String  text          = "çÇãÃáÁéÉíÍõÕóÓúÚ";
        String  normalizeText = controllerNormalize.asciiNormalizar(text);
        if (!normalizeText.equals(text))
            isCorrect = true;

        assertTrue(isCorrect);
    }

    @Test
    public void testAsciiNormalizarInNormalPhrase() throws Exception {
        Boolean isCorrect     = false;
        String  text          = "phrase normale";
        String  normalizeText = controllerNormalize.asciiNormalizar(text);
        if (normalizeText.contains("%20"))
            isCorrect = true;

        assertTrue(isCorrect);
    }
}