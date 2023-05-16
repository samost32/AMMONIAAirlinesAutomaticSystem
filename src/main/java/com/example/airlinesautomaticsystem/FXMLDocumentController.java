package com.example.airlinesautomaticsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.airlinesautomaticsystem.database.connectDb;


public class FXMLDocumentController implements Initializable {

    private LocalDateTime loginDateTime;
    public AnchorPane main_form;
    public Hyperlink employee_hyperLink;
    public Hyperlink admin_hyperLink;
    public AnchorPane adminform;
    public AnchorPane employee_form;
    public TextField username1;
    public PasswordField password1;
    public Button loginBtn1;
    public Button close1;
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginBtn;

    private double x = 0;
    private double y = 0;

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


    public void employeeLogin() {
        String employeeData = "SELECT * FROM employee WHERE phoneNum = ? and password = ?";
        Connection connect = connectDb();

        try {
            Alert alert;
            assert connect != null;
            PreparedStatement prepare = connect.prepareStatement(employeeData);

            if (username1.getText().isEmpty() || password1.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Введите имя пользователя и пароль");
                alert.showAndWait();
            } else {
                prepare.setString(1, username1.getText());
                prepare.setString(2, password1.getText());
                ResultSet result = prepare.executeQuery();

                if (result.next()) {
                    getData.username = username1.getText();
                    LocalDate loginDate = LocalDate.now();
                    LocalTime loginTime = LocalTime.now();


                    saveDateTimeToDatabase(loginDate, loginTime, null);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Успешный вход в систему!");
                    alert.showAndWait();

                    loginBtn1.getScene().getWindow().hide();

                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("employeeDashboard.fxml")));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    root.setOnMousePressed((MouseEvent event) -> {
                        x = event.getSceneX();
                        y = event.getSceneY();
                    });

                    root.setOnMouseDragged((MouseEvent event) -> {
                        stage.setX(event.getScreenX() - x);
                        stage.setY(event.getScreenY() - y);
                    });

                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Неправильное имя пользователя или пароль");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginAdmin() {

        String sql = "SELECT * FROM admin WHERE phoneNum = ? and password = ?";

        Connection connect = connectDb();

        try {
            assert connect != null;
            PreparedStatement prepare = connect.prepareStatement(sql);
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());

            ResultSet result = prepare.executeQuery();
            Alert alert;

            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, заполните все пустые поля");
                alert.showAndWait();
            } else {
                if (result.next()) {
                    getData.username = username.getText();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Успешно вход в систему");
                    alert.showAndWait();

                    loginBtn.getScene().getWindow().hide();
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dashboard.fxml")));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    root.setOnMousePressed((MouseEvent event) -> {
                        x = event.getSceneX();
                        y = event.getSceneY();
                    });

                    root.setOnMouseDragged((MouseEvent event) -> {
                        stage.setX(event.getScreenX() - x);
                        stage.setY(event.getScreenY() - y);
                    });

                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();

                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Неправильное имя пользователя/пароль");
                    alert.showAndWait();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void switchForm(ActionEvent event) {
        if (event.getSource() == admin_hyperLink) {
            main_form.setVisible(true);
            employee_form.setVisible(false);

        } else if (event.getSource() == employee_hyperLink) {
            main_form.setVisible(false);
            employee_form.setVisible(true);

        }
    }

    public void close() {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }


}
