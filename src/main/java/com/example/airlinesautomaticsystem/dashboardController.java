package com.example.airlinesautomaticsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;


public class dashboardController implements Initializable {

    private final String[] roleList = {"Administrator", "User"};
    @FXML
    private AnchorPane main_form;
    @FXML
    private Label username;
    @FXML
    private Button logout;
    @FXML
    private TableView<employeeData> addEmployee_tableView;
    @FXML
    private TableColumn<employeeData, String> addEmployee_col_employeeID;
    @FXML
    private TableColumn<employeeData, String> addEmployee_col_firstName;
    @FXML
    private TableColumn<employeeData, String> addEmployee_col_lastName;
    @FXML
    private TableColumn<employeeData, String> addEmployee_col_gender;
    @FXML
    private TableColumn<employeeData, String> addEmployee_col_phoneNum;
    @FXML
    private TableColumn<employeeData, String> addEmployee_col_position;
    @FXML
    private TableColumn<employeeData, String> addEmployee_col_date;
    @FXML
    private TextField addEmployee_search;
    @FXML
    private TextField addEmployee_employeeID;
    @FXML
    private TextField addEmployee_firstName;
    @FXML
    private TextField addEmployee_lastName;
    @FXML
    private ComboBox<?> addEmployee_gender;
    @FXML
    private TextField addEmployee_phoneNum;
    @FXML
    private ComboBox<?> addEmployee_position;
    @FXML
    private ImageView addEmployee_image;

    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;
    private Image image;
    private final String[] listOffice = {"Abu Dhabi", "Bahrain", "Doha"};
    private ObservableList<employeeData> addEmployeeList;
    private double x = 0;
    private double y = 0;

    public void addEmployeeAdd() {

        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "INSERT INTO employee " + "(employee_id,firstName,lastName,gender,phoneNum,position,image,date) " + "VALUES(?,?,?,?,?,?,?,?)";

        connect = database.connectDb();

        try {
            Alert alert;
            if (addEmployee_employeeID.getText().isEmpty() || addEmployee_firstName.getText().isEmpty() || addEmployee_lastName.getText().isEmpty() || addEmployee_gender.getSelectionModel().getSelectedItem() == null || addEmployee_phoneNum.getText().isEmpty() || addEmployee_position.getSelectionModel().getSelectedItem() == null || getData.path == null || getData.path.equals("")) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, заполните все пустые поля");
                alert.showAndWait();
            } else {

                String check = "SELECT employee_id FROM employee WHERE employee_id = '" + addEmployee_employeeID.getText() + "'";

                statement = connect.createStatement();
                result = statement.executeQuery(check);

                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Error");
                    alert.showAndWait();

                } else {

                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, addEmployee_employeeID.getText());
                    prepare.setString(2, addEmployee_firstName.getText());
                    prepare.setString(3, addEmployee_lastName.getText());
                    prepare.setString(4, (String) addEmployee_gender.getSelectionModel().getSelectedItem());
                    prepare.setString(5, addEmployee_phoneNum.getText());
                    prepare.setString(6, (String) addEmployee_position.getSelectionModel().getSelectedItem());

                    String uri = getData.path;
                    uri = uri.replace("\\", "\\\\");

                    prepare.setString(7, uri);
                    prepare.setString(8, String.valueOf(sqlDate));
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Успешно добавлено!");
                    alert.showAndWait();

                    addEmployeeShowListData();
                    addEmployeeReset();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addEmployeeUpdate() {

        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");

        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "UPDATE employee SET firstName = '" + addEmployee_firstName.getText() + "', lastName = '" + addEmployee_lastName.getText() + "', gender = '" + addEmployee_gender.getSelectionModel().getSelectedItem() + "', phoneNum = '" + addEmployee_phoneNum.getText() + "', position = '" + addEmployee_position.getSelectionModel().getSelectedItem() + "', image = '" + uri + "', date = '" + sqlDate + "' WHERE employee_id ='" + addEmployee_employeeID.getText() + "'";

        connect = database.connectDb();

        try {
            Alert alert;
            if (addEmployee_employeeID.getText().isEmpty() || addEmployee_firstName.getText().isEmpty() || addEmployee_lastName.getText().isEmpty() || addEmployee_gender.getSelectionModel().getSelectedItem() == null || addEmployee_phoneNum.getText().isEmpty() || addEmployee_position.getSelectionModel().getSelectedItem() == null || getData.path == null || getData.path.equals("")) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, заполните все пустые поля");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Вы уверены, что хотите обновить " + addEmployee_firstName.getText() + " " + addEmployee_lastName.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    String updateInfo = "UPDATE employee SET firstName = '" + addEmployee_firstName.getText() + "', lastName = '" + addEmployee_lastName.getText() + "', position = '" + addEmployee_position.getSelectionModel().getSelectedItem() + "' WHERE employee_id = '" + addEmployee_employeeID.getText() + "'";

                    prepare = connect.prepareStatement(updateInfo);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Успешно обновлено!");
                    alert.showAndWait();

                    addEmployeeReset();
                    addEmployeeShowListData();


                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addEmployeeDelete() {

        String sql = "DELETE FROM employee WHERE employee_id = '" + addEmployee_employeeID.getText() + "'";

        connect = database.connectDb();

        try {

            Alert alert;
            if (addEmployee_employeeID.getText().isEmpty() || addEmployee_firstName.getText().isEmpty() || addEmployee_lastName.getText().isEmpty() || addEmployee_gender.getSelectionModel().getSelectedItem() == null || addEmployee_phoneNum.getText().isEmpty() || addEmployee_position.getSelectionModel().getSelectedItem() == null || getData.path == null || getData.path.equals("")) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, заполните все пустые поля");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Вы уверены, что хотите удалить " + addEmployee_firstName.getText() + " " + addEmployee_lastName.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    String deleteInfo = "DELETE FROM employee WHERE employee_id = '" + addEmployee_employeeID.getText() + "'";

                    prepare = connect.prepareStatement(deleteInfo);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Успешно удалено!");
                    alert.showAndWait();

                    addEmployeeShowListData();
                    addEmployeeReset();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addEmployeeReset() {
        addEmployee_employeeID.setText("");
        addEmployee_firstName.setText("");
        addEmployee_lastName.setText("");
        addEmployee_gender.getSelectionModel().clearSelection();
        addEmployee_position.getSelectionModel().clearSelection();
        addEmployee_phoneNum.setText("");
        addEmployee_image.setImage(null);
        getData.path = "";
        addEmployeeShowListData();
    }

    public void addEmployeeInsertImage() {

        FileChooser open = new FileChooser();
        File file = open.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {
            getData.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(), 101, 127, false, true);
            addEmployee_image.setImage(image);
        }
    }

    public void addEmployeeRoleList() {
        List<String> listP = new ArrayList<>();

        Collections.addAll(listP, roleList);

        ObservableList listData = FXCollections.observableArrayList(listP);
        addEmployee_position.setItems(listData);

    }

    public void addEmployeeOfficeList() {
        List<String> listG = new ArrayList<>();

        Collections.addAll(listG, listOffice);

        ObservableList listData = FXCollections.observableArrayList(listG);
        addEmployee_gender.setItems(listData);
    }

    public void addEmployeeSearch() {

        FilteredList<employeeData> filter = new FilteredList<>(addEmployeeList, e -> true);

        addEmployee_search.textProperty().addListener((Observable, oldValue, newValue) -> filter.setPredicate(predicateEmployeeData -> {

            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            String searchKey = newValue.toLowerCase();

            if (predicateEmployeeData.getEmployeeId().toString().contains(searchKey)) {
                return true;
            } else if (predicateEmployeeData.getFirstName().toLowerCase().contains(searchKey)) {
                return true;
            } else if (predicateEmployeeData.getLastName().toLowerCase().contains(searchKey)) {
                return true;
            } else if (predicateEmployeeData.getGender().toLowerCase().contains(searchKey)) {
                return true;
            } else if (predicateEmployeeData.getPhoneNum().toLowerCase().contains(searchKey)) {
                return true;
            } else if (predicateEmployeeData.getPosition().toLowerCase().contains(searchKey)) {
                return true;
            } else return predicateEmployeeData.getDate().toString().contains(searchKey);
        }));

        SortedList<employeeData> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(addEmployee_tableView.comparatorProperty());
        addEmployee_tableView.setItems(sortList);
    }

    public ObservableList<employeeData> addEmployeeListData() {

        ObservableList<employeeData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM employee";

        connect = database.connectDb();

        try {
            prepare = Objects.requireNonNull(connect).prepareStatement(sql);
            result = prepare.executeQuery();
            employeeData employeeD;

            while (result.next()) {
                employeeD = new employeeData(result.getInt("employee_id"), result.getString("firstName"), result.getString("lastName"), result.getString("gender"), result.getString("phoneNum"), result.getString("position"), result.getString("image"), result.getDate("date"));
                listData.add(employeeD);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;

    }

    public void addEmployeeShowListData() {
        addEmployeeList = addEmployeeListData();

        addEmployee_col_employeeID.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        addEmployee_col_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        addEmployee_col_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        addEmployee_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        addEmployee_col_phoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        addEmployee_col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
        addEmployee_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        addEmployee_tableView.setItems(addEmployeeList);


        addEmployeeOfficeList();
        addEmployeeRoleList();


    }

    public void addEmployeeSelect() {
        employeeData employeeD = addEmployee_tableView.getSelectionModel().getSelectedItem();
        int num = addEmployee_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        addEmployee_employeeID.setText(String.valueOf(employeeD.getEmployeeId()));
        addEmployee_firstName.setText(employeeD.getFirstName());
        addEmployee_lastName.setText(employeeD.getLastName());
        addEmployee_phoneNum.setText(employeeD.getPhoneNum());

        getData.path = employeeD.getImage();

        String uri = "file:" + employeeD.getImage();

        image = new Image(uri, 101, 127, false, true);
        addEmployee_image.setImage(image);
    }


    public void displayUsername() {
        username.setText(getData.username);
    }

    public void logout() {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Вы уверены, что хотите выйти из системы?");
        Optional<ButtonType> option = alert.showAndWait();
        try {
            if (option.get().equals(ButtonType.OK)) {

                logout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLDocument.fxml")));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> stage.setOpacity(1));

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
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
        displayUsername();
        addEmployeeShowListData();
        addEmployeeOfficeList();
        addEmployeeRoleList();

    }

}
