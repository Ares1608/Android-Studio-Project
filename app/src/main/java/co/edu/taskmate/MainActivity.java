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
    private TextView quoteTextView;  // TextView para mostrar la cita

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerView);
        fabAddTask = findViewById(R.id.fab_add_task);
        quoteTextView = findViewById(R.id.quoteText); // Aquí está el TextView para la cita

        // Inicializar DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadTasks();

        // Configurar el botón flotante para agregar tareas
        fabAddTask.setOnClickListener(v -> {
            // Abrir la actividad AddEditTaskActivity para agregar tareas
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivity(intent);
        });

        // Llamar al método para cargar la cita al abrir la actividad
        loadQuote();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar tareas cada vez que la actividad se reinicie
        loadTasks();
    }

    private void loadTasks() {
        // Obtener todas las tareas de la base de datos
        List<Task> taskList = dbHelper.getAllTasks();

        // Inicializar el adaptador y asignarlo al RecyclerView
        taskAdapter = new TaskAdapter(taskList, dbHelper, this, new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {
                // Abrir la actividad de detalles de la tarea
                Intent intent = new Intent(MainActivity.this, ActivityTaskDetail.class);
                intent.putExtra("TASK_ID", task.getId());
                startActivity(intent);
            }

            @Override
            public void onTaskChecked(Task task, boolean isChecked) {
                // Actualizar el estado de la tarea en la base de datos
                task.setStatus(isChecked ? 1 : 0);
                dbHelper.updateTask(task);
            }
        });
        recyclerView.setAdapter(taskAdapter);
    }

    // Método para cargar la cita de la API
    private void loadQuote() {
        // Crear una instancia de Retrofit
        ZenQuotesApi zenQuotesApi = RetrofitClient.getClient().create(ZenQuotesApi.class);

        // Realizar la llamada a la API para obtener la cita
        Call<List<Quote>> call = zenQuotesApi.getQuote();

        call.enqueue(new Callback<List<Quote>>() {
            @Override
            public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Quote> quotes = response.body();

                    if (quotes != null && !quotes.isEmpty()) {
                        // Obtener la primera cita
                        Quote quote = quotes.get(0);
                        String quoteText = quote.getText();
                        String author = quote.getAuthor();

                        // Mostrar la cita en el TextView
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


