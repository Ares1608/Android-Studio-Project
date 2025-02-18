package co.edu.taskmate;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditTaskActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription;
    private TextView textViewSelectedDate;
    private CheckBox checkBoxCompleted;
    private Button buttonSelectDate, buttonSaveTask;
    private DatabaseHelper dbHelper;
    private Calendar selectedDate = Calendar.getInstance();
    private int taskId = -1; // -1 indica que es una nueva tarea

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        // Inicializar vistas
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        textViewSelectedDate = findViewById(R.id.textViewSelectedDate);
        checkBoxCompleted = findViewById(R.id.checkBoxCompleted);
        buttonSelectDate = findViewById(R.id.buttonSelectDate);
        buttonSaveTask = findViewById(R.id.buttonSaveTask);

        // Inicializar DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Verificar si se está editando una tarea existente
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("TASK_ID")) {
            taskId = intent.getIntExtra("TASK_ID", -1);
            if (taskId != -1) {
                loadTaskData(taskId);
            }
        }

        // Configurar el botón para seleccionar fecha
        buttonSelectDate.setOnClickListener(v -> showDatePickerDialog());

        // Configurar el botón para guardar la tarea
        buttonSaveTask.setOnClickListener(v -> saveTask());
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(year, month, dayOfMonth);
                    updateSelectedDateText();
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateSelectedDateText() {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        textViewSelectedDate.setText("Fecha seleccionada: " + sdf.format(selectedDate.getTime()));
    }

    private void loadTaskData(int taskId) {
        Task task = dbHelper.getTaskById(taskId);
        if (task != null) {
            editTextTitle.setText(task.getTitle());
            editTextDescription.setText(task.getDescription());
            textViewSelectedDate.setText("Fecha seleccionada: " + task.getDate());
            checkBoxCompleted.setChecked(task.getStatus() == 1);

            // Convertir la fecha de la tarea a Calendar
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                selectedDate.setTime(sdf.parse(task.getDate()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveTask() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.getTime());
        int status = checkBoxCompleted.isChecked() ? 1 : 0;

        if (title.isEmpty()) {
            Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        Task task = new Task(title, description, date, status);

        if (taskId == -1) {
            // Crear una nueva tarea
            dbHelper.addTask(task);
            Toast.makeText(this, "Tarea guardada", Toast.LENGTH_SHORT).show();
        } else {
            // Actualizar una tarea existente
            task.setId(taskId);
            dbHelper.updateTask(task);
            Toast.makeText(this, "Tarea actualizada", Toast.LENGTH_SHORT).show();
        }

        // Cerrar la actividad
        finish();
    }
}