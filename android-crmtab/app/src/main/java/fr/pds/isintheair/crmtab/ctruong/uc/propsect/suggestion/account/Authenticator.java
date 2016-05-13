package fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT;
import static fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.account.AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;
import static fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.account.AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS_LABEL;
import static fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.account.AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY;
import static fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.account.AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY_LABEL;
/**
 * Created by Truong on 4/24/2016.
 */
public class Authenticator extends AbstractAccountAuthenticator {

    private String TAG = "IsinTheAirAuthenticator";
    private final Context mContext;

    public Authenticator(Context context) {
        super(context);

        // I hate you! Google - set mContext as protected!
        this.mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {

        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        Log.d("IsinTheAir", TAG + "> getAuthToken");
        return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        if (AUTHTOKEN_TYPE_FULL_ACCESS.equals(authTokenType))
            return AUTHTOKEN_TYPE_FULL_ACCESS_LABEL;
        else if (AUTHTOKEN_TYPE_READ_ONLY.equals(authTokenType))
            return AUTHTOKEN_TYPE_READ_ONLY_LABEL;
        else
            return authTokenType + " (Label)";
    }


    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(KEY_BOOLEAN_RESULT, false);
        return result;
    }
}
