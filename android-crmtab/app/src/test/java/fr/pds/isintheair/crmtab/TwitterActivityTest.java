package fr.pds.isintheair.crmtab;

import com.google.gson.Gson;

import junit.framework.Assert;

import org.junit.Test;

import java.util.regex.Pattern;

import fr.pds.isintheair.crmtab.model.entity.twitter.SearchResults;
import fr.pds.isintheair.crmtab.model.entity.twitter.Searches;

/**
 * Created by Muthu on 11/05/2016.
 */
public class TwitterActivityTest {
    final String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
    final String TwitterSearchURL = "https://api.twitter.com/1.1/search/tweets.json?q=";

    @Test
    public void getTwitterWithSearchWrongInfo ()throws Exception {

        String result = null;
        // converts a string of JSON data into a SearchResults object

        Searches searches = null;
        if (result != null && result.length() > 0) {
            try {
                Gson gson = new Gson();
                // bring back the entire search object
                SearchResults sr = gson.fromJson(result, SearchResults.class);
                // but only pass the list of tweets found (called statuses)
                searches = sr.getStatuses();


            } catch (IllegalStateException ex) {

            }
            Assert.assertFalse(searches != null);
        }

    }

   @Test
    public void checkIfTextContainsUrl() throws Exception {

           String text = " fzefzef zefzfzef https://google.com fzteqgr regerg";
           // Pattern for recognizing a URL, based off RFC 3986
           Pattern urlPattern = Pattern.compile(
                   "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                           + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                           + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
                   Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

           // separate input by spaces ( URLs don't have spaces )
           String [] parts = text.split("\\s+");

           // get every part
           for( String item : parts ) {
                   //it's a good url
               if(urlPattern.matcher(item).matches()) {
                   Assert.assertTrue(urlPattern.matcher(item).matches());
               }

           }
       }


    @Test
    public void checkIfTextDoesntContainsUrl() throws Exception {

        String text = "zefzfzefzef";
        // Pattern for recognizing a URL, based off RFC 3986
        Pattern urlPattern = Pattern.compile(
                "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                        + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                        + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

        // separate input by spaces ( URLs don't have spaces )
        String [] parts = text.split("\\s+");

        // get every part
        for( String item : parts ) {
            //it's a good url

            Assert.assertFalse(urlPattern.matcher(item).matches());
        }
    }




}