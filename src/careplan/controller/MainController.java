package careplan.controller;

import careplan.model.Task;
import careplan.utils.DataManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.List;

public class MainController {

    // UI elements from the FXML file
    @FXML private ListView<Task> taskListView;
    @FXML private TextField taskInput;
    @FXML private ComboBox<String> categoryInput;
    @FXML private ComboBox<Task.Priority> priorityInput;
    @FXML private DatePicker dateInput;
    @FXML private Button addButton;

    // List to hold all the tasks
    private ObservableList<Task> tasks = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Load saved tasks from file
        List<Task> loadedTasks = DataManager.loadTasks();
        if (loadedTasks != null) {
            tasks.addAll(loadedTasks);
        }

        // Set values for category and priority dropdowns
        categoryInput.getItems().addAll("General", "Shopping", "School", "Work", "Health");
        priorityInput.getItems().addAll(Task.Priority.values());

        // Set the ListView to use our tasks list
        taskListView.setItems(tasks);
        taskListView.setCellFactory(list -> new TaskListCell());

        // Set default values for input fields
        categoryInput.setValue("General");
        priorityInput.setValue(Task.Priority.MEDIUM);
        dateInput.setValue(LocalDate.now());
    }

    @FXML
    private void handleAddTask() {
        // Get the title from input and trim spaces
        String title = taskInput.getText().trim();

        // Only add if the title is not empty
        if (!title.isEmpty()) {
            Task newTask = new Task(title); // Create new task
            newTask.setCategory(categoryInput.getValue()); // Set category
            newTask.setPriority(priorityInput.getValue()); // Set priority

            tasks.add(newTask); // Add task to the list
            DataManager.saveTasks(tasks); // Save updated list

            taskInput.clear(); // Clear input for next task
        }
    }

    // This class defines how each item in the task list looks and behaves
    private class TaskListCell extends ListCell<Task> {

        private final CheckBox checkBox = new CheckBox();
        private final Label timeLabel = new Label();
        private final Label categoryLabel = new Label();
        private final HBox container = new HBox(10, checkBox, timeLabel, categoryLabel);

        public TaskListCell() {
            // This happens when the checkbox is clicked
            checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                Task task = getItem();
                if (task != null) {
                    task.setDone(newVal); // Set the task as done/undone
                    updateItem(task, false); // Refresh the item view
                    DataManager.saveTasks(tasks); // Save the updated list
                }
            });

            // Styling
            container.setStyle("-fx-alignment: CENTER_LEFT;");
            timeLabel.setStyle("-fx-text-fill: #555; -fx-font-style: italic;");
            categoryLabel.setStyle("-fx-padding: 2 6; -fx-background-radius: 10; -fx-background-color: #e3f2fd;");
        }

        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setGraphic(null);
            } else {
                checkBox.setSelected(task.isDone());
                checkBox.setText(task.getTitle());
                timeLabel.setText(task.getFormattedTime());
                categoryLabel.setText(task.getCategory());

                setGraphic(container);
            }
        }
    }
}

