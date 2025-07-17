package indeedcoder;

import java.io.File;
import java.util.Optional;

import indeedcoder.model.Person;
import indeedcoder.serializer.PersonSerializer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class SerializationDemo extends Application {

	private TextField fNameTf = new TextField();
	private TextField lNameTf = new TextField();
	private TextField emailTf = new TextField();
	private Stage primaryStage;
	private ObservableList<Person> persons = FXCollections.observableArrayList();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Serialization Application");
		primaryStage.setScene(new Scene(getDesign(), 900, 400));
		primaryStage.show();
	}

	private Parent getDesign() {
		BorderPane bp = new BorderPane();
		bp.setTop(new Label("Person ke records"));
		bp.setCenter(getListPersons());
		bp.setRight(getRightPane());
		return bp;
	}

	@SuppressWarnings("unchecked")
	private Node getListPersons() {
		TableView<Person> personTable = new TableView<Person>();
		personTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		TableColumn<Person, String> col1 = new TableColumn<>("First Name");
		col1.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		TableColumn<Person, String> col3 = new TableColumn<>("Last Name");
		col3.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		TableColumn<Person, String> col5 = new TableColumn<>("E-mail Id");
		col5.setCellValueFactory(new PropertyValueFactory<>("email"));
		TableColumn<Person, String> col6 = new TableColumn<>(" ");
		col6.setCellFactory(col -> new TableCell<Person, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					Button serializeBtn = new Button("Serialize");
					Button deleteBtn = new Button("Delete");
					serializeBtn.setOnAction(e -> serializePerson(getTableView().getItems().get(getIndex())));
					deleteBtn.setOnAction(e -> persons.remove(getIndex()));
					setGraphic(new HBox(5, serializeBtn, deleteBtn));
				}
				setAlignment(Pos.CENTER);
			}
		});
		personTable.getColumns().addAll(col1, col3, col5, col6);
		personTable.setItems(persons);
		return personTable;
	}

	private Node getRightPane() {
		Button browse = new Button("Browse");
		browse.setOnAction(e -> browse());
		Button clear = new Button("Clear");
		clear.setOnAction(e -> clear());
		Button add = new Button("Add");
		add.setOnAction(e -> add());
		GridPane gp = new GridPane();
		gp.setPadding(new Insets(10));
		int i = 0;
		gp.addRow(i++, new Label("First Name :"), fNameTf);
		gp.addRow(i++, new Label("Last Name :"), lNameTf);
		gp.addRow(i++, new Label("Email Id:"), emailTf);
		gp.addRow(i++, browse, clear, add);
		return gp;
	}

	private void serializePerson(Person person) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save serialized object of person");

		fileChooser.getExtensionFilters().add(new ExtensionFilter("Serial File", "*.ser"));
		File personFile = fileChooser.showSaveDialog(primaryStage);
		if (personFile == null) {
			return;
		}
		boolean result = PersonSerializer.serializePerson(person, personFile.getPath());
		Alert a = null;
		if (result) {
			a = new Alert(AlertType.INFORMATION);
			a.setHeaderText("Success");
			a.setHeaderText("Serialized file saved at '" + personFile.getPath() + "'.");
		} else {
			a = new Alert(AlertType.ERROR);
			a.setHeaderText("Failed");
			a.setHeaderText("Couldn't serialize or save file.");
		}
		a.showAndWait();
	}

	private void browse() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select person object");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Serial File", "*.ser"));
		File personFile = fileChooser.showOpenDialog(primaryStage);
		if (personFile == null) {
			return;
		}
		Optional<Person> deSerializePerson = PersonSerializer.deSerializePerson(personFile);
		if (deSerializePerson.isPresent()) {
			fillForm(deSerializePerson.get());
		}
	}

	private void clear() {
		fNameTf.clear();
		fNameTf.clear();
		lNameTf.clear();
		emailTf.clear();
	}

	private void add() {
		Person p = new Person();
		p.setFirstName(fNameTf.getText());
		p.setLastName(lNameTf.getText());
		p.setEmail(emailTf.getText());
		persons.add(p);
	}

	private void fillForm(Person person) {
		fNameTf.setText(person.getFirstName());
		lNameTf.setText(person.getLastName());
		emailTf.setText(person.getEmail());
	}
}
