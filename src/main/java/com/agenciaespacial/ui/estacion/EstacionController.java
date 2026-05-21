package com.agenciaespacial.ui.estacion;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.stream.Collectors;

/** CRUD de Estaciones de Seguimiento + ver misiones monitorizadas. */
public class EstacionController {

    @FXML private TableView<EstacionSeguimiento>             tablaEstaciones;
    @FXML private TableColumn<EstacionSeguimiento, Long>     colId;
    @FXML private TableColumn<EstacionSeguimiento, String>   colNombre;
    @FXML private TableColumn<EstacionSeguimiento, String>   colPais;
    @FXML private TableColumn<EstacionSeguimiento, String>   colCiudad;
    @FXML private TableColumn<EstacionSeguimiento, String>   colLatitud;
    @FXML private TableColumn<EstacionSeguimiento, String>   colLongitud;
    @FXML private TextField txtBuscarId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtPais;
    @FXML private TextField txtCiudad;
    @FXML private TextField txtLatitud;
    @FXML private TextField txtLongitud;
    @FXML private Label     lblMensaje;

    private final EstacionService service = new EstacionService();
    private EstacionSeguimiento seleccionado = null;

    @FXML public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPais.setCellValueFactory(new PropertyValueFactory<>("pais"));
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        colLatitud.setCellValueFactory(cd -> new SimpleStringProperty(
            cd.getValue().getLatitud() != null ? String.format("%.4f", cd.getValue().getLatitud()) : ""));
        colLongitud.setCellValueFactory(cd -> new SimpleStringProperty(
            cd.getValue().getLongitud() != null ? String.format("%.4f", cd.getValue().getLongitud()) : ""));
        tablaEstaciones.getSelectionModel().selectedItemProperty()
            .addListener((obs, ant, nuevo) -> { if (nuevo != null) rellenarFormulario(nuevo); });
        cargarTabla();
    }

    private void cargarTabla() {
        try { tablaEstaciones.getItems().setAll(service.listarTodas()); limpiarMsg(); }
        catch (Exception e) { mostrarError("Error al cargar: " + e.getMessage()); }
    }

    @FXML private void onActualizarLista() { cargarTabla(); }

    @FXML private void onBuscarId() {
        String raw = txtBuscarId.getText().trim();
        if (raw.isEmpty()) { cargarTabla(); return; }
        try {
            EstacionSeguimiento e = service.buscarPorId(Long.parseLong(raw)).orElse(null);
            if (e == null) mostrarInfo("No existe estación con ID " + raw);
            else { tablaEstaciones.getItems().setAll(e); rellenarFormulario(e); }
        } catch (NumberFormatException e) { mostrarError("El ID debe ser un número entero."); }
    }

    @FXML private void onGuardar() {
        try {
            double lat = parseCoord(txtLatitud.getText().trim(),  "Latitud");
            double lon = parseCoord(txtLongitud.getText().trim(), "Longitud");
            if (seleccionado == null) {
                EstacionSeguimiento nueva = new EstacionSeguimiento(); rellenarEntidad(nueva, lat, lon);
                service.guardar(nueva); mostrarOk("Estación creada.");
            } else {
                rellenarEntidad(seleccionado, lat, lon);
                service.actualizar(seleccionado); mostrarOk("Estación actualizada.");
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
            service.eliminar(seleccionado.getId());
            cargarTabla(); limpiarFormulario(); mostrarOk("Estación eliminada.");
        }});
    }

    @FXML private void onNuevo()   { limpiarFormulario(); }
    @FXML private void onLimpiar() { limpiarFormulario(); }

    /** Muestra las misiones monitorizadas: cada satélite de la estación aporta su misión. */
    @FXML private void onVerMisiones() {
        if (seleccionado == null) { mostrarError("Selecciona una estación."); return; }
        List<Satelite> satelites = seleccionado.getSatelites();
        if (satelites.isEmpty()) { mostrarInfo("Sin satélites ni misiones asociadas."); return; }
        List<Mision> misiones = satelites.stream()
            .map(Satelite::getMision).filter(m -> m != null)
            .distinct().collect(Collectors.toList());
        if (misiones.isEmpty()) { mostrarInfo("Los satélites no tienen misión asignada."); return; }
        StringBuilder sb = new StringBuilder("Misiones de \"" + seleccionado.getNombre() + "\":\n\n");
        misiones.forEach(m -> sb.append("• ").append(m.getNombre()).append(" [").append(m.getEstado()).append("]\n"));
        mostrarInfo(sb.toString());
    }

    private void rellenarEntidad(EstacionSeguimiento e, double lat, double lon) {
        e.setNombre(txtNombre.getText().trim()); e.setPais(txtPais.getText().trim());
        e.setCiudad(txtCiudad.getText().trim()); e.setLatitud(lat); e.setLongitud(lon);
    }

    private void rellenarFormulario(EstacionSeguimiento e) {
        seleccionado = e;
        txtNombre.setText(e.getNombre() != null ? e.getNombre() : "");
        txtPais.setText(e.getPais() != null ? e.getPais() : "");
        txtCiudad.setText(e.getCiudad() != null ? e.getCiudad() : "");
        txtLatitud.setText(e.getLatitud() != null ? e.getLatitud().toString() : "");
        txtLongitud.setText(e.getLongitud() != null ? e.getLongitud().toString() : "");
        limpiarMsg();
    }

    private void limpiarFormulario() {
        seleccionado = null; txtNombre.clear(); txtPais.clear(); txtCiudad.clear();
        txtLatitud.clear(); txtLongitud.clear();
        tablaEstaciones.getSelectionModel().clearSelection(); limpiarMsg();
    }

    private double parseCoord(String s, String campo) {
        try { return Double.parseDouble(s); }
        catch (NumberFormatException e) { throw new IllegalArgumentException(campo + " debe ser un número."); }
    }

    private void mostrarError(String msg) { lblMensaje.setStyle("-fx-text-fill:#c62828;"); lblMensaje.setText("\u26a0 " + msg); new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait(); }
    private void mostrarOk(String msg)   { lblMensaje.setStyle("-fx-text-fill:#2e7d32;"); lblMensaje.setText("\u2714 " + msg); }
    private void mostrarInfo(String msg) { Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK); a.setHeaderText(null); a.showAndWait(); }
    private void limpiarMsg()            { lblMensaje.setText(""); }
}
