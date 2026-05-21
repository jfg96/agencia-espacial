package com.agenciaespacial.ui.satelite;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import java.util.List;

/**
 * Controlador JavaFX para la gestión de satélites en la interfaz gráfica.
 * Proporciona funcionalidades de CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre la entidad {@link Satelite}, así como la visualización de sus
 * registros de telemetría asociados. Este controlador interactúa con
 * los servicios {@link SateliteService} y {@link MisionService}.
 *
 * Responsabilidades:
 * - Listar, buscar, crear, actualizar y eliminar satélites.
 * - Validar campos numéricos (altitudOrbital) y parsear fecha de órbita.
 * - Permitir asignar una misión a un satélite.
 *
 * Autor: Antonio Manuel Rodriguez Palenzuela
 */
public class SateliteController {

    @FXML private TableView<Satelite>           tablaSatelites;
    @FXML private TableColumn<Satelite, Long>   colId;
    @FXML private TableColumn<Satelite, String> colNombre;
    @FXML private TableColumn<Satelite, String> colTipo;
    @FXML private TableColumn<Satelite, String> colAltitud;
    @FXML private TableColumn<Satelite, String> colFechaOrbita;
    @FXML private TableColumn<Satelite, String> colMision;
    @FXML private TextField        txtBuscarId;
    @FXML private TextField        txtNombre;
    @FXML private TextField        txtTipo;
    @FXML private TextField        txtAltitudOrbital;
    @FXML private DatePicker       dpFechaOrbita;
    @FXML private ComboBox<Mision> cmbMision;
    @FXML private Label            lblMensaje;

    private final SateliteService sateliteService = new SateliteService();
    private final MisionService   misionService   = new MisionService();
    private Satelite seleccionado = null;

    @FXML public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colAltitud.setCellValueFactory(cd -> new SimpleStringProperty(
            cd.getValue().getAltitudOrbital() != null ? cd.getValue().getAltitudOrbital() + " km" : ""));
        colFechaOrbita.setCellValueFactory(cd -> new SimpleStringProperty(
            cd.getValue().getFechaOrbita() != null ? cd.getValue().getFechaOrbita().toString() : ""));
        colMision.setCellValueFactory(cd -> new SimpleStringProperty(
            cd.getValue().getMision() != null ? cd.getValue().getMision().getNombre() : "—"));

        cmbMision.setConverter(new StringConverter<>() {
            public String toString(Mision m) { return m != null ? m.getNombre() : "— Sin misión —"; }
            public Mision fromString(String s) { return null; }
        });
        cargarMisiones();
        tablaSatelites.getSelectionModel().selectedItemProperty()
            .addListener((obs, ant, nuevo) -> { if (nuevo != null) rellenarFormulario(nuevo); });
        cargarTabla();
    }

    private void cargarTabla() {
        try { tablaSatelites.getItems().setAll(sateliteService.listarTodos()); limpiarMsg(); }
        catch (Exception e) { mostrarError("Error al cargar: " + e.getMessage()); }
    }

    private void cargarMisiones() {
        try { cmbMision.setItems(FXCollections.observableArrayList(misionService.listarTodas())); }
        catch (Exception ignored) {}
    }

    @FXML private void onActualizarLista() { cargarTabla(); cargarMisiones(); }

    @FXML private void onBuscarId() {
        String raw = txtBuscarId.getText().trim();
        if (raw.isEmpty()) { cargarTabla(); return; }
        try {
            Satelite s = sateliteService.buscarPorId(Long.parseLong(raw));
            if (s == null) mostrarInfo("No existe satélite con ID " + raw);
            else { tablaSatelites.getItems().setAll(s); rellenarFormulario(s); }
        } catch (NumberFormatException e) { mostrarError("El ID debe ser un número entero."); }
    }

    @FXML private void onGuardar() {
        try {
            double altitud = parseDouble(txtAltitudOrbital.getText().trim(), "Altitud orbital");
            if (seleccionado == null) {
                Satelite nuevo = new Satelite(); rellenarEntidad(nuevo, altitud);
                sateliteService.guardar(nuevo); mostrarOk("Satélite creado.");
            } else {
                rellenarEntidad(seleccionado, altitud);
                sateliteService.actualizar(seleccionado); mostrarOk("Satélite actualizado.");
            }
            cargarTabla(); limpiarFormulario();
        } catch (IllegalArgumentException e) { mostrarError(e.getMessage()); }
    }

    @FXML private void onEliminar() {
        if (seleccionado == null) { mostrarError("Selecciona una fila primero."); return; }
        Alert c = new Alert(Alert.AlertType.CONFIRMATION,
            "¿Eliminar \"" + seleccionado.getNombre() + "\"?", ButtonType.YES, ButtonType.NO);
        c.setHeaderText(null);
        c.showAndWait().ifPresent(bt -> { if (bt == ButtonType.YES) {
            sateliteService.eliminar(seleccionado.getId());
            cargarTabla(); limpiarFormulario(); mostrarOk("Satélite eliminado.");
        }});
    }

    @FXML private void onNuevo()   { limpiarFormulario(); }
    @FXML private void onLimpiar() { limpiarFormulario(); }

    @FXML private void onVerTelemetria() {
        if (seleccionado == null) { mostrarError("Selecciona un satélite."); return; }
        List<RegistroTelemetria> lista = seleccionado.getRegistrosTelemetria();
        if (lista.isEmpty()) { mostrarInfo("Sin registros de telemetría."); return; }
        StringBuilder sb = new StringBuilder("Telemetría de \"" + seleccionado.getNombre() + "\":\n\n");
        lista.forEach(r -> sb.append("• ").append(r).append("\n"));
        mostrarInfo(sb.toString());
    }

    private void rellenarEntidad(Satelite s, double altitud) {
        s.setNombre(txtNombre.getText().trim()); s.setTipo(txtTipo.getText().trim());
        s.setAltitudOrbital(altitud); s.setFechaOrbita(dpFechaOrbita.getValue());
        s.setMision(cmbMision.getValue());
    }

    private void rellenarFormulario(Satelite s) {
        seleccionado = s;
        txtNombre.setText(s.getNombre() != null ? s.getNombre() : "");
        txtTipo.setText(s.getTipo() != null ? s.getTipo() : "");
        txtAltitudOrbital.setText(s.getAltitudOrbital() != null ? s.getAltitudOrbital().toString() : "");
        dpFechaOrbita.setValue(s.getFechaOrbita()); cmbMision.setValue(s.getMision());
        limpiarMsg();
    }

    private void limpiarFormulario() {
        seleccionado = null; txtNombre.clear(); txtTipo.clear(); txtAltitudOrbital.clear();
        dpFechaOrbita.setValue(null); cmbMision.setValue(null);
        tablaSatelites.getSelectionModel().clearSelection(); limpiarMsg();
    }

    private double parseDouble(String s, String campo) {
        try { return Double.parseDouble(s); }
        catch (NumberFormatException e) { throw new IllegalArgumentException(campo + " debe ser un número."); }
    }

    private void mostrarError(String msg) { lblMensaje.setStyle("-fx-text-fill:#c62828;"); lblMensaje.setText("\u26a0 " + msg); new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait(); }
    private void mostrarOk(String msg)   { lblMensaje.setStyle("-fx-text-fill:#2e7d32;"); lblMensaje.setText("\u2714 " + msg); }
    private void mostrarInfo(String msg) { Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK); a.setHeaderText(null); a.showAndWait(); }
    private void limpiarMsg()            { lblMensaje.setText(""); }
}
