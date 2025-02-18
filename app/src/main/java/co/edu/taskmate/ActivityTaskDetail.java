package co.edu.taskmate;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityTaskDetail extends AppCompatActivity {

    private TextView textViewTitle, textViewDescription, textViewDate, textViewStatus;
    private Button buttonEditTask, buttonDeleteTask;
    private DatabaseHelper dbHelper;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Inicializar vistas
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewDate = findViewById(R.id.textViewDate);
        textViewStatus = findViewById(R.id.textViewStatus);
        buttonEditTask = findViewById(R.id.buttonEditTask);
        buttonDeleteTask = findViewById(R.id.buttonDeleteTask);


        dbHelper = new DatabaseHelper(this);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("TASK_ID")) {
            int taskId = intent.getIntExtra("TASK_ID", -1);
            if (taskId != -1) {
                // Cargar los datos de la tarea desde la base de datos
                task = dbHelper.getTaskById(taskId);
                if (task != null) {
                    loadTaskDetails(task);
                } else {
                    Toast.makeText(this, "Tarea no encontrada", Toast.LENGTH_SHORT).show();
                    finish(); // Cerrar la actividad si la tarea no existe
                }
            }
        }


        buttonEditTask.setOnClickListener(v -> {
            Intent editIntent = new Intent(ActivityTaskDetail.this, AddEditTaskActivity.class);
            editIntent.putExtra("TASK_ID", task.getId());
            startActivity(editIntent);
        });


        buttonDeleteTask.setOnClickListener(v -> {
            dbHelper.deleteTask(task.getId());
            Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void loadTaskDetails(Task task) {

        textViewTitle.setText(task.getTitle());
        textViewDescription.setText(task.getDescription());
        textViewDate.setText("Fecha de entrega: " + task.getDate());
        textViewStatus.setText("Estado: " + (task.getStatus() == 1 ? "Completada" : "Pendiente"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (task != null) {
            task = dbHelper.getTaskById(task.getId());
            loadTaskDetails(task);
        }
    }
}