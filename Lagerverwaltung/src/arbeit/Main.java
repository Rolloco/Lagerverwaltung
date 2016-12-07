package arbeit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Main extends Application {

	private TextField filterField;

	private TableView<Person> table = new TableView<Person>();

	private final static ObservableList<Person> data = FXCollections.observableArrayList(Schnittselle.datenbankverbindungSelect());

	public static final ObservableList<String> names = FXCollections.observableArrayList();

	final HBox hb = new HBox();
	final HBox hbFilter = new HBox();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		Scene scene = new Scene(new Group());
		stage.setTitle("Lagerverwaltung");
		stage.setWidth(1050);
		stage.setHeight(900);

		final Label label = new Label("Lagerverwaltung");
		label.setFont(new Font("Arial", 20));

		// Initialisierung
		names.addAll("Adam", "Personalnummer", "Alfred", "Albert", "Brenda", "Connie", "Derek", "Donny", "Lynne", "Myrtle", "Rose", "Rudolph",
				"Tony", "Trudy", "Williams", "Zach");

		// ============================ Haupttabelle ===================================================
		table.setEditable(true);
		Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
			public TableCell call(TableColumn p) {
				return new EditingCell();
			}
		};

		TableColumn barcodeCol = new TableColumn("Barcode");
		barcodeCol.setMinWidth(200);
		barcodeCol.setCellValueFactory(new PropertyValueFactory<Person, String>("barcode"));
		barcodeCol.setCellFactory(cellFactory);
		barcodeCol.setOnEditCommit(new EventHandler<CellEditEvent<Person, String>>() {
			@Override
			public void handle(CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setBarcode(t.getNewValue());
			}
		});

		TableColumn bezeichnungCol = new TableColumn("Bezeichnung");
		bezeichnungCol.setMinWidth(200);
		bezeichnungCol.setCellValueFactory(new PropertyValueFactory<Person, String>("bezeichnung"));
		bezeichnungCol.setCellFactory(cellFactory);
		bezeichnungCol.setOnEditCommit(new EventHandler<CellEditEvent<Person, String>>() {
			@Override
			public void handle(CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setBezeichnung(t.getNewValue());
			}
		});

		TableColumn stueckCol = new TableColumn("Stückzahl");
		stueckCol.setMinWidth(200);
		stueckCol.setCellValueFactory(new PropertyValueFactory<Person, String>("stueckzahl"));
		stueckCol.setCellFactory(cellFactory);
		stueckCol.setOnEditCommit(new EventHandler<CellEditEvent<Person, String>>() {
			@Override
			public void handle(CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setStueckzahl(t.getNewValue());
			}
		});

		TableColumn datumCol = new TableColumn("Datum");
		datumCol.setMinWidth(200);
		datumCol.setCellValueFactory(new PropertyValueFactory<Person, String>("datum"));
		datumCol.setCellFactory(cellFactory);
		datumCol.setOnEditCommit(new EventHandler<CellEditEvent<Person, String>>() {
			@Override
			public void handle(CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDatum(t.getNewValue());
			}
		});

		TableColumn ablaufdatumCol = new TableColumn("Ablaufdatum");
		ablaufdatumCol.setMinWidth(200);
		ablaufdatumCol.setCellValueFactory(new PropertyValueFactory<Person, String>("ablaufdatum"));
		ablaufdatumCol.setCellFactory(cellFactory);
		ablaufdatumCol.setOnEditCommit(new EventHandler<CellEditEvent<Person, String>>() {
			@Override
			public void handle(CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAblaufdatum(t.getNewValue());
			}
		});

		table.setItems(data);
		table.getColumns().addAll(barcodeCol, bezeichnungCol, stueckCol, datumCol, ablaufdatumCol);

		// ============================ ZUR TABELLE HINZUFÜGEN ===================================================
		final TextField addBarcode = new TextField();
		addBarcode.setPromptText("Barcode");
		addTextLimiter(addBarcode, 20);
		addBarcode.setMaxWidth(barcodeCol.getPrefWidth());
		final TextField addBezeichnung = new TextField();
		addBezeichnung.setMaxWidth(bezeichnungCol.getPrefWidth() + 10);
		addTextLimiter(addBezeichnung, 20);
		addBezeichnung.setPromptText("Bezeichnung");

		final TextField addStueckzahl = new TextField();
		addStueckzahl.setMaxWidth(stueckCol.getPrefWidth());
		addTextLimiter(addStueckzahl, 20);
		addStueckzahl.setPromptText("Stückzahl");

		final DatePicker addAblaufdatum = new DatePicker();
		addAblaufdatum.setMaxWidth(stueckCol.getPrefWidth() + 50);
		addAblaufdatum.setPromptText("Ablaufdatum");

		final Button addButton = new Button("Add");
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				String today = formatter.format(new Date());

				Person neu = new Person(addBarcode.getText(), //
						addBezeichnung.getText(), //
						addStueckzahl.getText(), //
						today, //
						addAblaufdatum.getValue().toString());

				data.add(neu);
				Schnittselle.datenbankverbindungInsert(neu);
				addBarcode.clear();
				addBezeichnung.clear();
			}
		});

		// ============================ FILTER SUCHMOEGLICHKEITEN===================================================
		final Label filter = new Label("\n Filter \n ");
		final Label result = null;
		filter.setFont(new Font("Arial", 20));

		final TextField searchBarcode = new TextField();
		searchBarcode.setPromptText("Barcode");
		addTextLimiter(searchBarcode, 20);
		searchBarcode.setMaxWidth(barcodeCol.getPrefWidth());
		final TextField searchBezeichnung = new TextField();
		searchBezeichnung.setMaxWidth(bezeichnungCol.getPrefWidth() + 10);
		addTextLimiter(searchBezeichnung, 20);
		searchBezeichnung.setPromptText("Bezeichnung");
		final TextField searchStueckzahl = new TextField();
		searchStueckzahl.setMaxWidth(stueckCol.getPrefWidth());
		addTextLimiter(searchStueckzahl, 20);
		searchStueckzahl.setPromptText("Stückzahl");
		final DatePicker searchAblaufdatum = new DatePicker();
		searchAblaufdatum.setMaxWidth(stueckCol.getPrefWidth() + 50);
		searchAblaufdatum.setPromptText("Ablaufdatum");

		final Button searchButton = new Button("Search");
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (true) {
					final Label result = new Label("\n Keine Ergebnisse");
					result.setFont(new Font("Arial", 20));
				}
			}
		});

		hbFilter.getChildren().addAll(searchBarcode, searchBezeichnung, searchStueckzahl, searchAblaufdatum, searchButton);
		hbFilter.setSpacing(5);

		hb.getChildren().addAll(addBarcode, addBezeichnung, addStueckzahl, addAblaufdatum, addButton);
		hb.setSpacing(5);

		final VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));
		vbox.getChildren().addAll(label, table, hb, filter, hbFilter);

		((Group) scene.getRoot()).getChildren().addAll(vbox);

		stage.setScene(scene);
		stage.show();
	}

	class EditingCell extends TableCell<Person, String> {

		private TextField textField;

		public EditingCell() {
		}

		@Override
		public void startEdit() {
			if (!isEmpty()) {
				super.startEdit();
				createTextField();
				setText(null);
				setGraphic(textField);
				textField.selectAll();
			}
		}

		@Override
		public void cancelEdit() {
			super.cancelEdit();

			setText((String) getItem());
			setGraphic(null);
		}

		@Override
		public void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);

			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				if (isEditing()) {
					if (textField != null) {
						textField.setText(getString());
					}
					setText(null);
					setGraphic(textField);
				} else {
					setText(getString());
					setGraphic(null);
				}
			}
		}

		private void createTextField() {
			textField = new TextField(getString());
			textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
			textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
					if (!arg2) {
						commitEdit(textField.getText());
					}
				}
			});
		}

		private String getString() {
			return getItem() == null ? "" : getItem().toString();
		}
	}

	public static void addTextLimiter(final TextField tf, final int maxLength) {
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				if (tf.getText().length() > maxLength) {
					String s = tf.getText().substring(0, maxLength);
					tf.setText(s);
				}
			}
		});
	}

}
