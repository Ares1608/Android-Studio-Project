package co.edu.taskmate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ZenQuotesApi {

    // Define el metodo para obtener una cita aleatoria
    @GET("api/random")
    Call<List<Quote>> getQuote();
}

