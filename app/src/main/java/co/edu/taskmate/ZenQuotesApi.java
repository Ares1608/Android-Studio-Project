package co.edu.taskmate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ZenQuotesApi {

    // Define el mtodo para obtener una cita aleatoria
    @GET("api/random")  // Aquí se usa el endpoint que te da una cita aleatoria
    Call<List<Quote>> getQuote();
}

