package co.edu.taskmate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private DatabaseHelper dbHelper;
    private FloatingActionButton fabAddTask;
    private TextView quoteTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerView);
        fabAddTask = findViewById(R.id.fab_add_task);
        quoteTextView = findViewById(R.id.quoteText); // Aquí está el TextView para la cita


        dbHelper = new DatabaseHelper(this);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadTasks();


        fabAddTask.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivity(intent);
        });


        loadQuote();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadTasks();
    }

    private void loadTasks() {

        List<Task> taskList = dbHelper.getAllTasks();


        taskAdapter = new TaskAdapter(taskList, dbHelper, this, new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {

                Intent intent = new Intent(MainActivity.this, ActivityTaskDetail.class);
                intent.putExtra("TASK_ID", task.getId());
                startActivity(intent);
            }

            @Override
            public void onTaskChecked(Task task, boolean isChecked) {

                task.setStatus(isChecked ? 1 : 0);
                dbHelper.updateTask(task);
            }
        });
        recyclerView.setAdapter(taskAdapter);
    }


    private void loadQuote() {

        ZenQuotesApi zenQuotesApi = RetrofitClient.getClient().create(ZenQuotesApi.class);


        Call<List<Quote>> call = zenQuotesApi.getQuote();

        call.enqueue(new Callback<List<Quote>>() {
            @Override
            public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Quote> quotes = response.body();

                    if (quotes != null && !quotes.isEmpty()) {

                        Quote quote = quotes.get(0);
                        String quoteText = quote.getText();
                        String author = quote.getAuthor();


                        quoteTextView.setText("\"" + quoteText + "\"\n- " + author);
                    } else {
                        quoteTextView.setText("No hay citas disponibles");
                    }
                } else {
                    Log.e("ZenQuotes", "Error en la respuesta de la API");
                    quoteTextView.setText("Error al cargar cita");
                }
            }

            @Override
            public void onFailure(Call<List<Quote>> call, Throwable t) {
                Log.e("ZenQuotes", "Fallo la llamada a la API: " + t.getMessage());
                quoteTextView.setText("Error de conexión");
            }
        });
    }
}


