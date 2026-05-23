package com.agenciaespacial.ui.mision;

import com.agenciaespacial.model.Mision;
import com.agenciaespacial.model.Participacion;
import com.agenciaespacial.model.Satelite;
import com.agenciaespacial.model.VehiculoLanzamiento;
import com.agenciaespacial.service.MisionService;
import com.agenciaespacial.service.VehiculoService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para el CRUD de Misiones.
 *
 * Responsabilidades:
 * - Listar, buscar, crear, actualizar y eliminar {@code Mision}.
 * - Gestionar la asignación de {@code VehiculoLanzamiento} y el estado de la misión.
 * - Mostrar información relacionada (satélites, astronautas) mediante diálogos.
 * - Delegar validaciones y persistencia en {@link MisionService} y {@link VehiculoService}.
 *
 * Notas:
 * - Las validaciones complejas (p. ej. coherencia de fechas) deben implementarse en el servicio.
 * - El controlador solo se encarga de mapear formulario <-> entidad y de la interacción UI.
 *
 * Autor: Antonio Manuel Rodriguez Palenzuela
 */
public class MisionController {

    @FXML private TableView<Mision>              tablaMisiones;
    @FXML private TableColumn<Mision, Long>      colId;
    @FXML private TableColumn<Mision, String>    colTipo;
    @FXML private TableColumn<Mision, String>    colNombre;
    @FXML private TableColumn<Mision, String>    colEstado;
    @FXML private TableColumn<Mision, String>    colFechaLanzamiento;
    @FXML private TableColumn<Mision, String>    colVehiculo;
    @FXML private TextField                      txtBuscarId;
    @FXML private CheckBox                       chkTripulada;
    @FXML private TextField                      txtNombre;
    @FXML private TextField                      txtObjetivo;
    @FXML private DatePicker                     dpFechaLanzamiento;
    @FXML private DatePicker                     dpFechaFinPrevista;
    @FXML private ChoiceBox<String>              choEstado;
    @FXML private ComboBox<VehiculoLanzamiento>  cmbVehiculo;
    @FXML private Label                          lblMensaje;

    private final MisionService   misionService   = new MisionService();
    private final VehiculoService vehiculoService = new VehiculoService();
    private Mision seleccionado = null;

    @FXML public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTipo.setCellValueFactory(cd -> new SimpleStringProperty(
            Boolean.TRUE.equals(cd.getValue().getTripulada()) ? "Tripulada" : "No Tripulada"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colFechaLanzamiento.setCellValueFactory(cd -> new SimpleStringProperty(
            cd.getValue().getFechaLanzamiento() != null ? cd.getValue().getFechaLanzamiento().toString() : ""));
        colVehiculo.setCellValueFactory(cd -> new SimpleStringProperty(
            cd.getValue().getVehiculo() != null ? cd.getValue().getVehiculo().getNombre() : "—"));

        choEstado.setItems(FXCollections.observableArrayList("PLANIFICADA","EN_CURSO","COMPLETADA","ABORTADA"));
        choEstado.setValue("PLANIFICADA");

        cmbVehiculo.setConverter(new StringConverter<>() {
            public String toString(VehiculoLanzamiento v) { return v != null ? v.getNombre() : "— Sin vehículo —"; }
            public VehiculoLanzamiento fromString(String s) { return null; }
        });
        cargarVehiculos();
        tablaMisiones.getSelectionModel().selectedItemProperty()
            .addListener((obs, ant, nuevo) -> { if (nuevo != null) rellenarFormulario(nuevo); });
        cargarTabla();
    }

    private void cargarTabla() {
        try { tablaMisiones.getItems().setAll(misionService.listarTodas()); limpiarMsg(); }
        catch (Exception e) { mostrarError("Error al cargar: " + e.getMessage()); }
    }

    private void cargarVehiculos() {
        try { cmbVehiculo.setItems(FXCollections.observableArrayList(vehiculoService.listarTodos())); }
        catch (Exception ignored) {}
    }

    @FXML private void onActualizarLista() { cargarTabla(); cargarVehiculos(); }

    @FXML private void onBuscarId() {
        String raw = txtBuscarId.getText().trim();
        if (raw.isEmpty()) { cargarTabla(); return; }
        try {
            Mision m = misionService.buscarPorId(Long.parseLong(raw)).orElse(null);
            if (m == null) mostrarInfo("No existe misión con ID " + raw);
            else { tablaMisiones.getItems().setAll(m); rellenarFormulario(m); }
        } catch (NumberFormatException e) { mostrarError("El ID debe ser un número entero."); }
    }

    @FXML private void onGuardar() {
        try {
            if (seleccionado == null) {
                Mision nueva = new Mision(); rellenarEntidad(nueva);
                misionService.guardar(nueva); mostrarOk("Misión creada.");
            } else {
                rellenarEntidad(seleccionado);
                misionService.actualizar(seleccionado); mostrarOk("Misión actualizada.");
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
            misionService.eliminar(seleccionado.getId());
            cargarTabla(); limpiarFormulario(); mostrarOk("Misión eliminada.");
        }});
    }

    @FXML private void onNuevo()   { limpiarFormulario(); }
    @FXML private void onLimpiar() { limpiarFormulario(); }

    @FXML private void onVerSatelites() {
        if (seleccionado == null) { mostrarError("Selecciona una misión."); return; }
        List<Satelite> lista = seleccionado.getSatelites();
        if (lista.isEmpty()) { mostrarInfo("Sin satélites asociados."); return; }
        StringBuilder sb = new StringBuilder("Satélites de \"" + seleccionado.getNombre() + "\":\n\n");
        lista.forEach(s -> sb.append("• ").append(s).append("\n"));
        mostrarInfo(sb.toString());
    }

    @FXML private void onVerAstronautas() {
        if (seleccionado == null) { mostrarError("Selecciona una misión."); return; }
        List<Participacion> lista = seleccionado.getParticipaciones();
        if (lista.isEmpty()) { mostrarInfo("Sin astronautas asignados."); return; }
        StringBuilder sb = new StringBuilder("Astronautas de \"" + seleccionado.getNombre() + "\":\n\n");
        lista.forEach(p -> sb.append("• ").append(p.getAstronauta().getNombreCompleto())
                             .append(" — ").append(p.getRol()).append("\n"));
        mostrarInfo(sb.toString());
    }

    private void rellenarEntidad(Mision m) {
        m.setTripulada(chkTripulada.isSelected());
        m.setNombre(txtNombre.getText().trim());
        m.setObjetivo(txtObjetivo.getText().trim());
        m.setFechaLanzamiento(dpFechaLanzamiento.getValue());
        m.setFechaFinPrevista(dpFechaFinPrevista.getValue());
        m.setEstado(choEstado.getValue());
        m.setVehiculo(cmbVehiculo.getValue());
    }

    private void rellenarFormulario(Mision m) {
        seleccionado = m;
        chkTripulada.setSelected(Boolean.TRUE.equals(m.getTripulada()));
        txtNombre.setText(m.getNombre() != null ? m.getNombre() : "");
        txtObjetivo.setText(m.getObjetivo() != null ? m.getObjetivo() : "");
        dpFechaLanzamiento.setValue(m.getFechaLanzamiento());
        dpFechaFinPrevista.setValue(m.getFechaFinPrevista());
        choEstado.setValue(m.getEstado() != null ? m.getEstado() : "PLANIFICADA");
        cmbVehiculo.setValue(m.getVehiculo());
        limpiarMsg();
    }

    private void limpiarFormulario() {
        seleccionado = null;
        chkTripulada.setSelected(false); txtNombre.clear(); txtObjetivo.clear();
        dpFechaLanzamiento.setValue(LocalDate.now()); dpFechaFinPrevista.setValue(null);
        choEstado.setValue("PLANIFICADA"); cmbVehiculo.setValue(null);
        tablaMisiones.getSelectionModel().clearSelection(); limpiarMsg();
    }

    private void mostrarError(String msg) { lblMensaje.setStyle("-fx-text-fill:#c62828;"); lblMensaje.setText("\u26a0 " + msg); new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait(); }
    private void mostrarOk(String msg)   { lblMensaje.setStyle("-fx-text-fill:#2e7d32;"); lblMensaje.setText("\u2714 " + msg); }
    private void mostrarInfo(String msg) { Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK); a.setHeaderText(null); a.showAndWait(); }
    private void limpiarMsg()            { lblMensaje.setText(""); }
}
