package bajzat.com.cleverbot;

import android.telecom.Call;


import bajzat.com.cleverbot.models.Suggestion;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by name on 19/01/2016.
 */
public interface ApiService {
    @GET("/suggestion")
    void getSuggestion(@Query("question") String question,
                       Callback<Suggestion> suggestionCallback);

    @GET("/suggestion/add")
    void addSuggestion(@Query("question") String question,
                       @Query("answer") String answer,
                       Callback<Suggestion> suggestionCallback);


}
