package fr.pds.isintheair.crmtab;

import org.junit.Test;

import fr.pds.isintheair.crmtab.helper.CredentialHelper;
import fr.pds.isintheair.crmtab.model.entity.User;
import fr.pds.isintheair.crmtab.model.rest.RetrofitHandlerSingleton;
import fr.pds.isintheair.crmtab.model.rest.service.LoginService;
import fr.pds.isintheair.crmtab.view.activity.LoginActivity;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static org.junit.Assert.assertTrue;

/**
 * Created by jbide on 23/03/2016.
 */
public class LoginActivityTest {
    @Test
    public void loginTest(){

        LoginActivity log = new LoginActivity();


        String login    = "test";
        String password = "test";
        String basic    = CredentialHelper.getBase64Credentials(login, password);
        User currentUser = new User();
        currentUser.setEmail(login);
        currentUser.setPassword(basic);

        LoginService loginService = RetrofitHandlerSingleton.getInstance().getLoginService();
        Call<User> call         = loginService.login(currentUser);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                assertTrue(response.isSuccess());

            }
            @Override
            public void onFailure(Throwable t) {

            }
    });
    }
}
