package test;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import oracle.jdbc.AdditionalDatabaseMetaData;

/**
 * View-Controller for the person table.
 * 
 * @author Collin Kempkes
 */
public class TableController {
	
	private static Logger logger = Logger.getRootLogger();

	@FXML
	private TextField filterField;
	@FXML
	private TableView<Artikel> personTable;
	@FXML
	private TableColumn<Artikel, String> barcodeColumn;
	@FXML
	private TableColumn<Artikel, String> bezeichnungColumn;
	@FXML
	private TableColumn<Artikel, Number> stueckzahlColumn;
	@FXML
	private TableColumn<Artikel, String> datumColumn;
	@FXML
	private TableColumn<Artikel, String> ablaufDatumColumn;
	@FXML
	private TableColumn<Artikel, Number> preisColumn;
	@FXML
	private TableColumn<Artikel, String> kundennummerColumn;
	@FXML
	private TableColumn<Artikel, String> lieferantColumn;

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
	private Button addToTable;

	private ObservableList<Artikel> masterData = FXCollections.observableArrayList();

	private Schnittstelle schnittstelle;

	private Validation validator = new Validation();

	/**
	 * Just add some sample data in the constructor.
	 */
	public TableController() {
		masterData.addAll(schnittstelle.datenbankverbindungSelect());
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 * 
	 * Initializes the table columns and sets up sorting and filtering.
	 */
	@FXML
	private void initialize() {

		// 0. Initialize the columns.
		barcodeColumn.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
		bezeichnungColumn.setCellValueFactory(cellData -> cellData.getValue().bezeichnungProperty());
		stueckzahlColumn.setCellValueFactory(cellData -> cellData.getValue().stueckzahlProperty());
		datumColumn.setCellValueFactory(cellData -> cellData.getValue().datumProperty());
		ablaufDatumColumn.setCellValueFactory(cellData -> cellData.getValue().ablaufDatumProperty());
		preisColumn.setCellValueFactory(cellData -> cellData.getValue().preisProperty());
		kundennummerColumn.setCellValueFactory(cellData -> cellData.getValue().kundennummerProperty());
		lieferantColumn.setCellValueFactory(cellData -> cellData.getValue().lieferantProperty());

		addEditability();
		addFilterOpportunity();

		addToTable.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (addAblaufdatum.getValue() == null)
					addAblaufdatum.setValue(LocalDate.now());

				SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
				String today = format.format(new Date());
				Instant instant = addAblaufdatum.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
				Date aDatum = Date.from(instant);

				//Validation der Eingaben
				try {
					validator.validate(addBarcode.getText(), //
							addBezeichnung.getText(), //
							addStueckzahl.getValue().toString(), //
							format.format(aDatum), //
							addPreis.getText(), //
							addKundennummer.getText());
				} catch (Exception e1) {
					System.out.println(e1.getMessage());
					// e1.printStackTrace();
					return;
				}
				
				Artikel artikel = new Artikel(addBarcode.getText(), //
						addBezeichnung.getText(), //
						Integer.parseInt(addStueckzahl.getValue().toString()), //
						today, //
						format.format(aDatum), //
						Integer.parseInt(addPreis.getText()), //
						addKundennummer.getText(), //
						"");

				try {
					artikel = schnittstelle.datenbankverbindungInsert(artikel);
				} catch (Exception e2) {
					System.out.println(e2.getMessage());
					// e2.printStackTrace();
					return;
				}
				masterData.add(artikel);
				addBarcode.clear();
				addBezeichnung.clear();
				addPreis.clear();
				addKundennummer.clear();
				validator.showExpiredSingle(artikel);
			}
		});

		addStueckzahl.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000));
		addStueckzahl.setEditable(true);

		// DELETE ROW ON DELETE BUTTON
		personTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(final KeyEvent keyEvent) {
				final Artikel selectedItem = personTable.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					if (keyEvent.getCode().equals(KeyCode.DELETE)) {
						int delete = validator.showDelete(selectedItem);
						if (delete == 1) {
							schnittstelle.datenbankverbindungDelete(selectedItem);
							masterData.remove(selectedItem);
						} else {
							return;
						}
					}
				}
			}
		});

		Timer timer = new java.util.Timer();

		timer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						validator.showExpired();
					}
				});
			}
		}, 100, 86400000); // Alle 24 Stunden poppt eine Erinnerung auf
		
		// Erstellung des Loggers
		try {
		      SimpleLayout layout = new SimpleLayout();
		      ConsoleAppender consoleAppender = new ConsoleAppender( layout );
		      logger.addAppender( consoleAppender );
		      FileAppender fileAppender = new FileAppender( layout, "logs/Table.log", true );
		      logger.addAppender( fileAppender );
		      logger.setLevel( Level.ALL );
		    } catch( Exception ex ) {
		      System.out.println( ex );
		    }
	}

	private void addEditability() {
		// FOR BARCODE
		barcodeColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Artikel, String>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<Artikel, String> t) {
				logger.info("Bestehender Barcode wurde angepasst");
				logger.info("Alt: " + t.getTableView().getItems().get(t.getTablePosition().getRow()).getBarcode());
				schnittstelle.datenbankverbindungInsertOnEditPrimary(
						t.getTableView().getItems().get(t.getTablePosition().getRow()), t.getNewValue(), "BARCODE");
				((Artikel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setBarcode(t.getNewValue());
				logger.info("Neu: " + t.getNewValue());
			}
		});
		barcodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		addTextLimiter(addBarcode, 20);

		// FOR BEZEICHNUNG
		bezeichnungColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Artikel, String>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<Artikel, String> t) {
				logger.info("Bestehende Bezeichnung wurde angepasst");
				logger.info("Alt: " + t.getTableView().getItems().get(t.getTablePosition().getRow()).getBezeichnung());
				((Artikel) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setBezeichnung(t.getNewValue());
				schnittstelle.datenbankverbindungInsertOnEdit(
						t.getTableView().getItems().get(t.getTablePosition().getRow()));
				logger.info("Neu: " + t.getNewValue());
			}
		});
		bezeichnungColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		addTextLimiter(addBezeichnung, 20);

		// FOR STUECKZAHL
		stueckzahlColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Artikel, Number>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<Artikel, Number> t) {
				logger.info("Bestehende Stückzahl wurde angepasst");
				logger.info("Alt: " + t.getTableView().getItems().get(t.getTablePosition().getRow()).getStueckzahl());
				((Artikel) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setStueckzahl(t.getNewValue().intValue());
				schnittstelle.datenbankverbindungInsertOnEdit(
						t.getTableView().getItems().get(t.getTablePosition().getRow()));
				logger.info("Neu: " + t.getNewValue());
			}
		});
		stueckzahlColumn.setCellValueFactory(new Callback<CellDataFeatures<Artikel, Number>, ObservableValue<Number>>() {
		    @Override
		    public ObservableValue<Number> call(CellDataFeatures<Artikel, Number> p) {
		        return new SimpleIntegerProperty(p.getValue().getStueckzahl());
		} 
		});

		stueckzahlColumn.setCellFactory(TextFieldTableCell.<Artikel, Number>forTableColumn(new NumberStringConverter()));

		// FOR ABLAUFDATUM
		ablaufDatumColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Artikel, String>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<Artikel, String> t) {
				logger.info("Bestehendes Ablaufdatum wurde angepasst");
				logger.info("Alt: " + t.getTableView().getItems().get(t.getTablePosition().getRow()).getAblaufDatum());
				((Artikel) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setAblaufDatum(t.getNewValue());
				schnittstelle.datenbankverbindungInsertOnEdit(
						t.getTableView().getItems().get(t.getTablePosition().getRow()));
				validator.showExpiredSingle(t.getTableView().getItems().get(t.getTablePosition().getRow()));
				logger.info("Neu: " + t.getNewValue());
			}
		});
		ablaufDatumColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		// FOR PREIS
		preisColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Artikel, Number>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<Artikel, Number> t) {
				logger.info("Bestehender Preis wurde angepasst");
				logger.info("Alt: " + t.getTableView().getItems().get(t.getTablePosition().getRow()).getPreis());
				((Artikel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPreis(t.getNewValue().intValue());
				schnittstelle.datenbankverbindungInsertOnEdit(
						t.getTableView().getItems().get(t.getTablePosition().getRow()));
				logger.info("Neu: " + t.getNewValue());
			}
		});
		preisColumn.setCellValueFactory(new Callback<CellDataFeatures<Artikel, Number>, ObservableValue<Number>>() {
		    @Override
		    public ObservableValue<Number> call(CellDataFeatures<Artikel, Number> p) {
		        return new SimpleIntegerProperty(p.getValue().getPreis());
		} 
		});
		addTextLimiter(addPreis, 20);
		addTextLimiter(addKundennummer, 20);
	}

	private void addFilterOpportunity() {
		// 1. Wrap the ObservableList in a FilteredList (initially display all
		// data).
		FilteredList<Artikel> filteredData = new FilteredList<>(masterData, p -> true);

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
				} else if (String.valueOf(person.getStueckzahl()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				} else if (person.getDatum().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				} else if (person.getAblaufDatum().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				} else if (String.valueOf(person.getPreis()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				} else if (person.getKundennummer().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				} else if (person.getLieferant().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				}
				return false; // Does not match.
			});
		});

		// 3. Wrap the FilteredList in a SortedList.
		SortedList<Artikel> sortedData = new SortedList<>(filteredData);

		// 4. Bind the SortedList comparator to the TableView comparator.
		// Otherwise, sorting the TableView would have no effect.
		sortedData.comparatorProperty().bind(personTable.comparatorProperty());

		// 5. Add sorted (and filtered) data to the table.
		personTable.setItems(sortedData);
	}

	public static void addTextLimiter(final TextField tf, final int maxLength) {
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue) {
				if (tf.getText().length() > maxLength) {
					String s = tf.getText().substring(0, maxLength);
					tf.setText(s);
				}
			}
		});
	}
}