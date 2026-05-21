package com.agenciaespacial.ui.telemetria;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * @author Antonio Manuel Rodriguez Palenzuela
 * Crear/buscar/actualizar/eliminar RegistroTelemetria.
 * RS-003: nivelBateria 0-100 (validado en TelemetriaService).
 * La tabla carga los registros del satélite seleccionado en el ComboBox de filtro.
 */
public class TelemetriaController {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // ── Tabla ─────────────────────────────────────────────────────────────────
    @FXML private TableView<RegistroTelemetria>             tablaRegistros;
    @FXML private TableColumn<RegistroTelemetria, String>   colId;
    @FXML private TableColumn<RegistroTelemetria, String>   colFechaHora;
    @FXML private TableColumn<RegistroTelemetria, String>   colTemperatura;
    @FXML private TableColumn<RegistroTelemetria, String>   colVelocidad;
    @FXML private TableColumn<RegistroTelemetria, String>   colBateria;
    @FXML private TableColumn<RegistroTelemetria, String>   colSatelite;

    // ── Filtro de satélite (para listarPorSatelite) ───────────────────────────
    @FXML private ComboBox<Satelite> cmbFiltroSatelite;

    // ── Búsqueda ──────────────────────────────────────────────────────────────
    @FXML private TextField txtBuscarId;

    // ── Formulario ────────────────────────────────────────────────────────────
    @FXML private TextField          txtFechaHora;      // formato: yyyy-MM-dd HH:mm
    @FXML private TextField          txtTemperatura;
    @FXML private TextField          txtVelocidad;
    @FXML private TextField          txtNivelBateria;
    @FXML private ComboBox<Satelite> cmbSatelite;       // satélite del nuevo registro
    @FXML private Label              lblMensaje;

    // ── Estado ────────────────────────────────────────────────────────────────
    private final TelemetriaService service         = new TelemetriaService();
    private final SateliteService   sateliteService = new SateliteService();
    private RegistroTelemetria seleccionado = null;

    @FXML
    public void initialize() {
        // Columnas
        colId.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getId() != null ? cd.getValue().getId().toString() : ""));
        colFechaHora.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getFechaHora() != null ? cd.getValue().getFechaHora().format(FMT) : ""));
        colTemperatura.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getTemperatura() != null ? cd.getValue().getTemperatura() + " C" : ""));
        colVelocidad.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getVelocidad() != null ? cd.getValue().getVelocidad() + " km/s" : ""));
        colBateria.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getNivelBateria() != null ? cd.getValue().getNivelBateria() + " %" : ""));
        colSatelite.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getSatelite() != null ? cd.getValue().getSatelite().getNombre() : "—"));

        // Converter reutilizable
        StringConverter<Satelite> conv = new StringConverter<>() {
            public String toString(Satelite s) { return s != null ? s.getNombre() : "—"; }
            public Satelite fromString(String str) { return null; }
        };
        cmbFiltroSatelite.setConverter(conv);
        cmbSatelite.setConverter(conv);



        // Cambiar satélite filtro → recargar tabla
        cmbFiltroSatelite.valueProperty().addListener((obs, ant, nuevo) -> cargarTabla());

        // Selección en tabla → rellena formulario
        tablaRegistros.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, nuevo) -> { if (nuevo != null) rellenarFormulario(nuevo); });
    }

    private void cargarTabla() {
        Satelite filtro = cmbFiltroSatelite.getValue();
        if (filtro == null) {
            tablaRegistros.getItems().clear();
            mostrarOk("Selecciona un satélite para ver su telemetría.");
            return;
        }
        try {
            List<RegistroTelemetria> lista = service.listarPorSatelite(filtro);
            tablaRegistros.getItems().setAll(lista);
            limpiarMsg();
        } catch (Exception e) { mostrarError("Error al cargar: " + e.getMessage()); }
    }

    @FXML private void onBuscarId() {
        String raw = txtBuscarId.getText().trim();
        if (raw.isEmpty()) { cargarTabla(); return; }
        try {
            // buscarPorId devuelve Optional<RegistroTelemetria>
            Optional<RegistroTelemetria> opt = service.buscarPorId(Long.parseLong(raw));
            if (opt.isEmpty()) {
                mostrarInfo("No existe registro con ID " + raw + ".");
            } else {
                RegistroTelemetria r = opt.get();
                tablaRegistros.getItems().setAll(r);
                rellenarFormulario(r);
            }
        } catch (NumberFormatException e) { mostrarError("El ID debe ser un número entero."); }
    }

    @FXML private void onGuardar() {
        try {
            LocalDateTime fh = parseFechaHora(txtFechaHora.getText().trim());
            if (fh == null) return;
            double temp    = parseDouble(txtTemperatura.getText().trim(),  "Temperatura");
            double vel     = parseDouble(txtVelocidad.getText().trim(),    "Velocidad");
            double bateria = parseDouble(txtNivelBateria.getText().trim(), "Nivel de batería");

            if (seleccionado == null) {
                RegistroTelemetria nuevo = new RegistroTelemetria();
                rellenarEntidad(nuevo, fh, temp, vel, bateria);
                service.guardar(nuevo);
                mostrarOk("Registro creado.");
            } else {
                rellenarEntidad(seleccionado, fh, temp, vel, bateria);
                service.actualizar(seleccionado);
                mostrarOk("Registro actualizado.");
            }
            cargarTabla(); limpiarFormulario();
        } catch (IllegalArgumentException e) { mostrarError(e.getMessage()); }
    }

    @FXML private void onEliminar() {
        if (seleccionado == null) { mostrarError("Selecciona una fila primero."); return; }
        Alert c = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar el registro ID " + seleccionado.getId() + "?", ButtonType.YES, ButtonType.NO);
        c.setHeaderText(null);
        c.showAndWait().ifPresent(bt -> { if (bt == ButtonType.YES) {
            service.eliminar(seleccionado.getId());
            cargarTabla(); limpiarFormulario(); mostrarOk("Registro eliminado.");
        }});
    }

    @FXML private void onNuevo()   { limpiarFormulario(); }
    @FXML private void onLimpiar() { limpiarFormulario(); }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private void rellenarEntidad(RegistroTelemetria r, LocalDateTime fh,
                                 double t, double v, double b) {
        r.setFechaHora(fh); r.setTemperatura(t); r.setVelocidad(v); r.setNivelBateria(b);
        r.setSatelite(cmbSatelite.getValue());
    }

    private void rellenarFormulario(RegistroTelemetria r) {
        seleccionado = r;
        txtFechaHora.setText(r.getFechaHora() != null ? r.getFechaHora().format(FMT) : "");
        txtTemperatura.setText(r.getTemperatura() != null ? r.getTemperatura().toString() : "");
        txtVelocidad.setText(r.getVelocidad() != null ? r.getVelocidad().toString() : "");
        txtNivelBateria.setText(r.getNivelBateria() != null ? r.getNivelBateria().toString() : "");
        cmbSatelite.setValue(r.getSatelite());
        limpiarMsg();
    }

    private void limpiarFormulario() {
        seleccionado = null;
        txtFechaHora.clear(); txtTemperatura.clear();
        txtVelocidad.clear(); txtNivelBateria.clear(); cmbSatelite.setValue(null);
        tablaRegistros.getSelectionModel().clearSelection(); limpiarMsg();
    }

    private LocalDateTime parseFechaHora(String s) {
        try { return LocalDateTime.parse(s, FMT); }
        catch (DateTimeParseException e) { mostrarError("Formato: yyyy-MM-dd HH:mm"); return null; }
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