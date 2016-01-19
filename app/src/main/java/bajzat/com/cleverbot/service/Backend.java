package bajzat.com.cleverbot.service;

import bajzat.com.cleverbot.ApiService;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by name on 19/01/2016.
 */
public class Backend {
    private RestAdapter restAdapter;
    private static final String URL = "http://junimea.hol.es/api/cleverbot/index.php/";
    private ApiService apiService;

    public ApiService getApiService() {
        return apiService;
    }

    public Backend() {
        restAdapter = new RestAdapter.Builder().setEndpoint(URL).build();
        apiService = restAdapter.create(ApiService.class);

    }
}
