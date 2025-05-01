package careplan.controller;

import careplan.model.Task;
import careplan.utils.DataManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class MainController {
    @FXML private TextField taskInput;
    @FXML private Button addButton;
    @FXML private ListView<Task> taskListView;

    private ObservableList<Task> tasks = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        List<Task> loadedTasks = DataManager.loadTasks();
        if (loadedTasks != null) {
            tasks.addAll(loadedTasks);
        }

        taskListView.setItems(tasks);
        taskListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setText(null);
                } else {
                    setText((task.isDone() ? "✔️ " : "") + task.getTitle());
                }
            }
        });

        addButton.setOnAction(e -> {
            String title = taskInput.getText();
            if (!title.isBlank()) {
                Task newTask = new Task(title);
                tasks.add(newTask);
                DataManager.saveTasks(tasks);
                taskInput.clear();
            }
        });
    }
}
