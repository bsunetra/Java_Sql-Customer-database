package practice_java;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;

public class CustomerUI extends Application {

    private TextField idField, nameField, phoneField, locationField, searchField;
    private TableView<Customer> tableView;
    private ObservableList<Customer> customerList;

    // ---------- Database Connection ----------
    private Connection connect() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/billing_details", "root", "root");
        } catch (SQLException e) {
            showAlert("Database Error", e.getMessage());
            return null;
        }
    }

    @Override
    public void start(Stage stage) {
        // ---------- Search Bar ----------
        searchField = new TextField();
        searchField.setPromptText("Search by Name or Location");
        searchField.setPrefWidth(250);
        Button searchBtn = new Button("🔍 Search");
        searchBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        searchBtn.setOnAction(e -> searchCustomer());

        HBox searchBox = new HBox(10, searchField, searchBtn);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(10));

        // ---------- Input Fields ----------
        idField = new TextField(); idField.setPromptText("Customer ID");
        nameField = new TextField(); nameField.setPromptText("Customer Name");
        phoneField = new TextField(); phoneField.setPromptText("Phone Number");
        locationField = new TextField(); locationField.setPromptText("Location");

        // ---------- Buttons ----------
        Button addBtn = new Button("Add");
        Button updBtn = new Button("Update");
        Button delBtn = new Button("Delete");
        Button viewBtn = new Button("View All");
        Button clrBtn = new Button("Clear");

        addBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        updBtn.setStyle("-fx-background-color: #f1c40f; -fx-text-fill: black;");
        delBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        viewBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        clrBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;");

        HBox btnBox = new HBox(10, addBtn, updBtn, delBtn, viewBtn, clrBtn);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(10));

        // ---------- Grid for input fields ----------
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.setPadding(new Insets(10));
        inputGrid.addRow(0, idField, nameField);
        inputGrid.addRow(1, phoneField, locationField);

        // ---------- Table ----------
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tableView.setPrefHeight(350);

        TableColumn<Customer, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("custId"));

        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("custName"));

        TableColumn<Customer, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Customer, String> locCol = new TableColumn<>("Location");
        locCol.setCellValueFactory(new PropertyValueFactory<>("location"));

        tableView.getColumns().addAll(idCol, nameCol, phoneCol, locCol);
        customerList = FXCollections.observableArrayList();
        tableView.setItems(customerList);

        // ---------- Layout ----------
        VBox layout = new VBox(10, searchBox, inputGrid, btnBox, tableView);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f7f9fc;");

        // ---------- Scene ----------
        Scene scene = new Scene(layout, 850, 600);
        stage.setScene(scene);
        stage.setTitle("🧾 Customer Management System");
        stage.show();

        // ---------- Button actions ----------
        addBtn.setOnAction(e -> addCustomer());
        updBtn.setOnAction(e -> updateCustomer());
        delBtn.setOnAction(e -> deleteCustomer());
        viewBtn.setOnAction(e -> loadCustomers());
        clrBtn.setOnAction(e -> clearFields());

        // ---------- Load Data on Start ----------
        loadCustomers();
    }

    // ---------- Database CRUD ----------
    private void addCustomer() {
        String sql = "INSERT INTO cust_details VALUES (?,?,?,?)";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(idField.getText()));
            ps.setString(2, nameField.getText());
            ps.setLong(3, Long.parseLong(phoneField.getText()));
            ps.setString(4, locationField.getText());
            ps.executeUpdate();
            showAlert("Success", "Customer added successfully!");
            loadCustomers();
            clearFields();
        } catch (Exception e) { showAlert("Error", e.getMessage()); }
    }

    private void updateCustomer() {
        String sql = "UPDATE cust_details SET cust_name=?, ph_no=?, location=? WHERE cust_id=?";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nameField.getText());
            ps.setLong(2, Long.parseLong(phoneField.getText()));
            ps.setString(3, locationField.getText());
            ps.setInt(4, Integer.parseInt(idField.getText()));
            ps.executeUpdate();
            showAlert("Updated", "Customer updated successfully!");
            loadCustomers();
            clearFields();
        } catch (Exception e) { showAlert("Error", e.getMessage()); }
    }

    private void deleteCustomer() {
        String sql = "DELETE FROM cust_details WHERE cust_id=?";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(idField.getText()));
            ps.executeUpdate();
            showAlert("Deleted", "Customer deleted successfully!");
            loadCustomers();
            clearFields();
        } catch (Exception e) { showAlert("Error", e.getMessage()); }
    }

    private void loadCustomers() {
        customerList.clear();
        String sql = "SELECT * FROM cust_details";
        try (Connection c = connect(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                customerList.add(new Customer(
                        rs.getInt("cust_id"),
                        rs.getString("cust_name"),
                        rs.getString("ph_no"),
                        rs.getString("location")
                ));
            }
        } catch (Exception e) { showAlert("Error", e.getMessage()); }
    }

    private void searchCustomer() {
        customerList.clear();
        String key = searchField.getText();
        String sql = "SELECT * FROM cust_details WHERE cust_name LIKE ? OR location LIKE ?";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + key + "%");
            ps.setString(2, "%" + key + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                customerList.add(new Customer(
                        rs.getInt("cust_id"),
                        rs.getString("cust_name"),
                        rs.getString("ph_no"),
                        rs.getString("location")
                ));
            }
        } catch (Exception e) { showAlert("Error", e.getMessage()); }
    }

    // ---------- Helpers ----------
    private void clearFields() {
        idField.clear();
        nameField.clear();
        phoneField.clear();
        locationField.clear();
        searchField.clear();
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setTitle(title);
        a.setContentText(msg);
        a.showAndWait();
    }

    // ---------- Customer Model ----------
    public static class Customer {
        private final int custId;
        private final String custName;
        private final String phone;
        private final String location;

        public Customer(int custId, String custName, String phone, String location) {
            this.custId = custId;
            this.custName = custName;
            this.phone = phone;
            this.location = location;
        }

        public int getCustId() { return custId; }
        public String getCustName() { return custName; }
        public String getPhone() { return phone; }
        public String getLocation() { return location; }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
