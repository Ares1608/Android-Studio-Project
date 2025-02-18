package co.edu.taskmate;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://zenquotes.io/";  // URL base de la API de Zen Quotes
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())  // Usamos Gson para convertir el JSON
                    .build();
        }
        return retrofit;
    }
}

