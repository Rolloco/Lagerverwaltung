package test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * View-Controller for the person table.
 * 
 * @author Collin Kempkes
 */
public class PersonTableController {

	@FXML
	private TextField filterField;
	@FXML
	private TableView<Person> personTable;
	@FXML
	private TableColumn<Person, String> barcodeColumn;
	@FXML
	private TableColumn<Person, String> bezeichnungColumn;
	@FXML
	private TableColumn<Person, String> stueckzahlColumn;
	@FXML
	private TableColumn<Person, String> datumColumn;
	@FXML
	private TableColumn<Person, String> ablaufDatumColumn;
	@FXML
	private TableColumn<Person, String> preisColumn;
	@FXML
	private TableColumn<Person, String> kundennummerColumn;

	@FXML
	private TextField addBarcode;
	@FXML
	private TextField addBezeichnung;
	@FXML
	private Spinner addStueckzahl;
	@FXML
	private DatePicker addAblaufdatum;
	@FXML
	private TextField addPreis;
	@FXML
	private TextField addKundennummer;
	@FXML
	private Button myButton;

	private ObservableList<Person> masterData = FXCollections.observableArrayList();

	/**
	 * Just add some sample data in the constructor.
	 */
	public PersonTableController() {
		masterData.addAll(Schnittstelle.datenbankverbindungSelect());
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 * 
	 * Initializes the table columns and sets up sorting and filtering.
	 */
	@FXML
	private void initialize() {
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		String today = format.format(new Date());

		// 0. Initialize the columns.
		barcodeColumn.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
		bezeichnungColumn.setCellValueFactory(cellData -> cellData.getValue().bezeichnungProperty());
		stueckzahlColumn.setCellValueFactory(cellData -> cellData.getValue().stueckzahlProperty());
		datumColumn.setCellValueFactory(cellData -> cellData.getValue().datumProperty());
		ablaufDatumColumn.setCellValueFactory(cellData -> cellData.getValue().ablaufDatumProperty());
		preisColumn.setCellValueFactory(cellData -> cellData.getValue().preisProperty());
		kundennummerColumn.setCellValueFactory(cellData -> cellData.getValue().kundennummerProperty());

		addEditability();
		addFilterOpportunity();
		addAblaufdatum.setConverter(converter);

		myButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				masterData.add(new Person(addBarcode.getText(), //
						addBezeichnung.getText(), //
						addStueckzahl.getValue().toString(), //
						today, //
						String.valueOf((addAblaufdatum.getValue())), //
						addPreis.getText(), //
						addKundennummer.getText()));
				addBarcode.clear();
				addBezeichnung.clear();
				addPreis.clear();
				addKundennummer.clear();
			}
		});

		addStueckzahl.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000));
		addStueckzahl.setEditable(true);

		// GET SELECTED ROW
		// Person person = personTable.getSelectionModel().getSelectedItem();
		// if (person != null)
		// System.out.print(person.getFirstName());
	}

	private void addEditability() {
		// FOR FIRSTNAME
		barcodeColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setBarcode(t.getNewValue());
			}
		});
		barcodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		// FOR LASTNAME
		bezeichnungColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setBezeichnung(t.getNewValue());
			}
		});
		bezeichnungColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		// FOR STUECKZAHL
		stueckzahlColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setStueckzahl(t.getNewValue());
			}
		});
		stueckzahlColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		// FOR DATUM
		datumColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDatum(t.getNewValue());
			}
		});
		datumColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		// FOR ABLAUFDATUM
		ablaufDatumColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setAblaufDatum(t.getNewValue());
			}
		});
		ablaufDatumColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		// FOR PREIS
		preisColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPreis(t.getNewValue());
			}
		});
		preisColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		// FOR KUNDENNUMMER
		kundennummerColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setKundennummer(t.getNewValue());
			}
		});
		kundennummerColumn.setCellFactory(TextFieldTableCell.forTableColumn());
	}

	private void addFilterOpportunity() {
		// 1. Wrap the ObservableList in a FilteredList (initially display all
		// data).
		FilteredList<Person> filteredData = new FilteredList<>(masterData, p -> true);

		// 2. Set the filter Predicate whenever the filter changes.
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(person -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				// Compare first name and last name of every person with filter
				// text.
				String lowerCaseFilter = newValue.toLowerCase();

				if (person.getBarcode().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches first name.
				} else if (person.getBezeichnung().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				} else if (person.getStueckzahl().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				} else if (person.getDatum().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				} else if (person.getAblaufDatum().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				} else if (person.getPreis().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				} else if (person.getKundennummer().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				}
				return false; // Does not match.
			});
		});

		// 3. Wrap the FilteredList in a SortedList.
		SortedList<Person> sortedData = new SortedList<>(filteredData);

		// 4. Bind the SortedList comparator to the TableView comparator.
		// Otherwise, sorting the TableView would have no effect.
		sortedData.comparatorProperty().bind(personTable.comparatorProperty());

		// 5. Add sorted (and filtered) data to the table.
		personTable.setItems(sortedData);
	}

	StringConverter converter = new StringConverter<LocalDate>() {
        DateTimeFormatter dateFormatter = 
            DateTimeFormatter.ofPattern("dd.MM.yyyy");
        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return "";
            }
        }
        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dateFormatter);
            } else {
                return null;
            }
        }
    };
}
