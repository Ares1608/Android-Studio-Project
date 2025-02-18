package co.edu.taskmate;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private DatabaseHelper dbHelper;
    private Context context;
    private OnTaskClickListener listener;

    // Interfaz para manejar clics en las tareas
    public interface OnTaskClickListener {
        void onTaskClick(Task task);
        void onTaskChecked(Task task, boolean isChecked);
    }

    // Constructor
    public TaskAdapter(List<Task> taskList, DatabaseHelper dbHelper, Context context, OnTaskClickListener listener) {
        this.taskList = taskList;
        this.dbHelper = dbHelper;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout para cada ítem de la lista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        // Obtener la tarea en la posición actual
        Task task = taskList.get(position);

        // Mostrar los datos de la tarea en las vistas
        holder.title.setText(task.getTitle());
        holder.date.setText(task.getDate());
        holder.status.setChecked(task.getStatus() == 1);

        // Configurar el clic en la tarea para abrir los detalles
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTaskClick(task);
            }
        });

        // Configurar el clic en el CheckBox para marcar como completada
        holder.status.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onTaskChecked(task, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // ViewHolder para las tareas
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title, date;
        CheckBox status;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.task_title);
            date = itemView.findViewById(R.id.task_date);
            status = itemView.findViewById(R.id.task_status);
        }
    }

    // Metodo para actualizar la lista de tareas
    public void updateTaskList(List<Task> newTaskList) {
        taskList.clear();
        taskList.addAll(newTaskList);
        notifyDataSetChanged();
    }
}