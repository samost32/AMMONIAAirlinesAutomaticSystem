package com.example.airlinesautomaticsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.example.airlinesautomaticsystem.database.connectDb;


public class EmployeeDashboard implements Initializable {


    private static final String DB_URL = "jdbc:mysql://localhost/employee";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane addEmployee_form;

    @FXML
    private TableView<EmployeeInfo> table;

    @FXML
    private Button close;

    @FXML
    private TableColumn<?, ?> employee_LoginTime;

    @FXML
    private TableColumn<?, ?> employee_LogoutTime;

    @FXML
    private TableColumn<?, ?> employee_allTime;

    @FXML
    private TableColumn<?, ?> employee_data;

    @FXML
    private TableColumn<?, ?> employee_error;

    @FXML
    private Button logout;

    @FXML
    private StackPane main_form;

    @FXML
    private Button minimize;

    @FXML
    private Label name;
    private double x = 0;
    private double y = 0;
    private LocalDate loginDate;
    private LocalTime loginTime;


    public void saveDateTimeToDatabase(LocalDate date, LocalTime loginTime, LocalTime logoutTime) {
        try {
            // Установка соединения с базой данных
            Connection conn = connectDb();

            // Подготовка SQL-запроса для вставки даты, времени входа и времени выхода в базу данных
            String sql = "INSERT INTO employee_info (date, login_time, logout_time) VALUES (?, ?, ?)";
            assert conn != null;
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setObject(1, date);
            statement.setObject(2, loginTime);
            statement.setObject(3, logoutTime);

            // Выполнение SQL-запроса
            statement.executeUpdate();

            // Закрытие ресурсов
            statement.close();
            conn.close();

            System.out.println("Дата, время входа и время выхода успешно сохранены в базе данных.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при сохранении даты, времени входа и времени выхода в базу данных.");
        }
    }

    public void displayUsername() {
        name.setText(getData.username);
    }

    @FXML
    private void logout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Вы уверены, что хотите выйти из системы?");
        Optional<ButtonType> option = alert.showAndWait();
        try {
            if (option.get().equals(ButtonType.OK)) {
                LocalTime logoutTime = LocalTime.now();

                saveDateTimeToDatabase(loginDate, loginTime, logoutTime);

                logout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLDocument.fxml")));
                Stage stage = new Stage();
                Scene scene = new Scene(root);


                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addDataFromDatabase() {
        TableColumn<EmployeeInfo, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<EmployeeInfo, String> loginTimeColumn = new TableColumn<>("Login Time");
        loginTimeColumn.setCellValueFactory(new PropertyValueFactory<>("loginTime"));

        TableColumn<EmployeeInfo, String> logoutTimeColumn = new TableColumn<>("Logout Time");
        logoutTimeColumn.setCellValueFactory(new PropertyValueFactory<>("logoutTime"));

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD); Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM employee_info")) {
            while (resultSet.next()) {
                String date = resultSet.getString("date");
                String loginTime = resultSet.getString("login_time");
                String logoutTime = resultSet.getString("logout_time");

                EmployeeInfo employeeInfo = new EmployeeInfo(date, loginTime, logoutTime);
                table.getItems().add(employeeInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addDataFromDatabase();
        displayUsername();
    }
}
