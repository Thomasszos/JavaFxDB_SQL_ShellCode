package org.example.javafxdb_sql_shellcode;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.example.javafxdb_sql_shellcode.db.ConnDbOps;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class DB_GUI_Controller implements Initializable {

    private final ObservableList<Person> data = FXCollections.observableArrayList();
    private static ConnDbOps cdbop;
    String uri;
    private DB_Application app;


    @FXML
    TextField first_name, last_name, department, major;
    @FXML
    private TableView<Person> tv;
    @FXML
    private TableColumn<Person, Integer> tv_id;
    @FXML
    private TableColumn<Person, String> tv_fn, tv_ln, tv_dept, tv_major;
    @FXML
    private MenuItem lightCSS;
    @FXML
    private MenuItem darkCSS;
    @FXML
    private MenuItem defaultCSS;

    @FXML
    ImageView img_view;

    /**
     * Added database connection, and downloading from database.
     * Also using platform to get scene for CSS updating.
     * @param url
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resourceBundle
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cdbop = new ConnDbOps();


        tv_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tv_fn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tv_ln.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tv_dept.setCellValueFactory(new PropertyValueFactory<>("dept"));
        tv_major.setCellValueFactory(new PropertyValueFactory<>("major"));

        data.addAll(cdbop.loadStudentsFromDB());

        tv.setItems(data);

    }


    @FXML
    protected void addNewRecord() {


        Person np = new Person(
                data.size()+1,
                first_name.getText(),
                last_name.getText(),
                department.getText(),
                major.getText()
        );


        data.add(np);
        cdbop.insertUser(np.getFirstName(), np.getLastName(), np.getDept(), np.getMajor(), np.getPhoto());
    }

    /**
     * Clears student form, added image clear.
     */

    @FXML
    protected void clearForm() {
        first_name.clear();
        last_name.setText("");
        department.setText("");
        major.setText("");
        img_view.setImage(null);
    }

    @FXML
    protected void closeApplication() {
        System.exit(0);
    }


    /**
     * Updates a student with all new information including photo
     */
    @FXML
    protected void editRecord() {
        Person p= tv.getSelectionModel().getSelectedItem();
        int c=data.indexOf(p);
        Person p2= new Person();
        p2.setId(c+1);
        p2.setFirstName(first_name.getText());
        p2.setLastName(last_name.getText());
        p2.setDept(department.getText());
        p2.setMajor(major.getText());
        p2.setPhoto(uri);
        data.remove(c);
        data.add(c,p2);
        tv.getSelectionModel().select(c);

        cdbop.editStudent(p.getId(), first_name.getText(), last_name.getText(), department.getText(), major.getText(), p2.getPhoto());
    }

    /**
     * Deletes Student locally and in database.
     */
    @FXML
    protected void deleteRecord() {
        Person p= tv.getSelectionModel().getSelectedItem();
        data.remove(p);
        cdbop.deleteStudent(p.getFirstName());
    }


    /**
     * Uploads image and saves it to person object so it can be saved when edited or insert is selected.
     */
    @FXML
    protected void showImage() {
        Person p= tv.getSelectionModel().getSelectedItem();
        File file= (new FileChooser()).showOpenDialog(img_view.getScene().getWindow());
        if(file!=null){
            uri=file.toURI().toString();
            img_view.setImage(new Image(uri));
            p.setPhoto(uri);
        }
    }


    /**
     * Added Image loading.
     * @param mouseEvent
     */

    @FXML
    protected void selectedItemTV(MouseEvent mouseEvent) {
        Person p= tv.getSelectionModel().getSelectedItem();
        first_name.setText(p.getFirstName());
        last_name.setText(p.getLastName());
        department.setText(p.getDept());
        major.setText(p.getMajor());

        if (p.getPhoto() != null && !p.getPhoto().isEmpty()) {
            img_view.setImage(new Image(p.getPhoto())); // assumes a file: or http: URI
        }

    }


    @FXML
    void changeToDark(ActionEvent event) {
        app.changeScene("styling/dark.css");
    }

    @FXML
    void changeToDefault(ActionEvent event) {
        app.changeScene("styling/style.css");
    }

    @FXML
    void changeToLight(ActionEvent event) {
        app.changeScene("styling/light.css");
    }


    public void setApp(DB_Application dbApplication) {

            this.app = app;

    }
}