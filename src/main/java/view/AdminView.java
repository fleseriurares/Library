package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import view.model.UserDTO;

import java.util.List;

public class AdminView {

    private TableView userTableView;
    private final ObservableList<UserDTO> usersObservableList;
    private TextField usernameTextField;
    private PasswordField passwordField;
    private Label usernameLabel;
    private Label passwordLabel;
    private Button saveButton;
    private Button updateButton;
    private ComboBox rolesComboBox;
    private Button generatePdfButton;
    private Text actionTarget;

    public AdminView(Stage primaryStage, List<UserDTO> users){
        primaryStage.setTitle("Library");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        usersObservableList = FXCollections.observableArrayList(users);

        initTableView(gridPane);

        initSaveOptions(gridPane);

        primaryStage.show();

    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25)); //distanta dintre marginea unui obiect si continutul sau

    }

    public TableView getUserTableView(){return userTableView;}

    private void initTableView(GridPane gridPane){
        userTableView = new TableView<UserDTO>();

        userTableView.setPlaceholder(new Label("No users to display"));

        TableColumn<UserDTO, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<UserDTO, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<UserDTO, String> roleColumn = new TableColumn<>("Roles");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));

        userTableView.getColumns().addAll(usernameColumn, passwordColumn,roleColumn);

        userTableView.setItems(usersObservableList);

        gridPane.add(userTableView, 0, 0, 6, 1);
    }

    private void initSaveOptions(GridPane gridPane){
        usernameLabel = new Label("Username");
        gridPane.add(usernameLabel, 1, 1);

        usernameTextField = new TextField();
        gridPane.add(usernameTextField,2, 1);

        passwordLabel = new Label("Password");
        gridPane.add(passwordLabel, 3, 1);

        passwordField = new PasswordField();
        gridPane.add(passwordField, 4, 1);

        saveButton = new Button("Save");
        gridPane.add(saveButton, 1, 2);

        updateButton = new Button("Update Account");
        gridPane.add(updateButton,2,2);

        generatePdfButton = new Button("Generate Report");
        gridPane.add(generatePdfButton, 4, 2);

        String roles[] = {"Administrator", "Employee", "Customer"};
        rolesComboBox = new ComboBox(FXCollections.observableArrayList(roles));
        rolesComboBox.setValue("Employee");
        gridPane.add(rolesComboBox, 3, 2);

        actionTarget = new Text();
        actionTarget.setFill(Color.FIREBRICK);
        gridPane.add(actionTarget, 1, 3);

    }

    public String getUsername() {
        return usernameTextField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public void addSaveButtonListener(EventHandler<ActionEvent> saveButtonListener) {
        saveButton.setOnAction(saveButtonListener);
    }

    public void addGeneratePDFButtonListener(EventHandler<ActionEvent> generatePdfButtonListener){
        generatePdfButton.setOnAction(generatePdfButtonListener);
    }

    public void addUpdateButtonListener(EventHandler<ActionEvent> updateButtonListener){
        updateButton.setOnAction(updateButtonListener);
    }

    public void setActionTargetText(String text){ this.actionTarget.setText(text);}

    public String getRole() {
        return rolesComboBox.getValue().toString();
    }

    public void addUserToObservableList(UserDTO userDTO) {
        this.usersObservableList.add(userDTO);
    }
}
