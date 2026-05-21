package com.agenciaespacial.ui.astronauta;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
/**
 * Controlador para la gestión de Astronautas: crear, buscar por ID, actualizar y eliminar.
 * RS-004: Validación de datos (nombre completo no vacío, fecha de nacimiento no
 * futura, nacionalidad no vacía, especialidad no vacía) se realiza en la capa de servicio
 * {@link AstronautaService}.
 * <p>
 * El controlador se comunica con la capa de servicio para realizar las operaciones
 * de negocio y actualiza la interfaz de usuario en consecuencia. Cualquier error de
 * validación o excepción se muestra al usuario mediante un {@code Alert}.
 * </p>
 */

/** CRUD de Astronautas. RS-004 validado en AstronautaService. */
public class AstronautaController {

    @FXML private TableView<Astronauta>           tablaAstronautas;
    @FXML private TableColumn<Astronauta, Long>   colId;
    @FXML private TableColumn<Astronauta, String> colNombre;
    @FXML private TableColumn<Astronauta, String> colNacionalidad;
    @FXML private TableColumn<Astronauta, String> colFechaNacimiento;
    @FXML private TableColumn<Astronauta, String> colEspecialidad;
    @FXML private TextField  txtBuscarId;
    @FXML private TextField  txtNombreCompleto;
    @FXML private TextField  txtNacionalidad;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private TextField  txtEspecialidad;
    @FXML private Label      lblMensaje;

    private final AstronautaService service = new AstronautaService();
    private Astronauta seleccionado = null;

    @FXML public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colNacionalidad.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        colFechaNacimiento.setCellValueFactory(cd -> new SimpleStringProperty(
            cd.getValue().getFechaNacimiento() != null ? cd.getValue().getFechaNacimiento().toString() : ""));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        tablaAstronautas.getSelectionModel().selectedItemProperty()
            .addListener((obs, ant, nuevo) -> { if (nuevo != null) rellenarFormulario(nuevo); });
        cargarTabla();
    }

    private void cargarTabla() {
        try { tablaAstronautas.getItems().setAll(service.listarTodos()); limpiarMsg(); }
        catch (Exception e) { mostrarError("Error al cargar: " + e.getMessage()); }
    }

    @FXML private void onActualizarLista() { cargarTabla(); }

    @FXML private void onBuscarId() {
        String raw = txtBuscarId.getText().trim();
        if (raw.isEmpty()) { cargarTabla(); return; }
        try {
            Astronauta a = service.buscarPorId(Long.parseLong(raw)).orElse(null);
            if (a == null) mostrarInfo("No existe astronauta con ID " + raw);
            else { tablaAstronautas.getItems().setAll(a); rellenarFormulario(a); }
        } catch (NumberFormatException e) { mostrarError("El ID debe ser un número entero."); }
    }

    @FXML private void onGuardar() {
        try {
            if (seleccionado == null) {
                Astronauta nuevo = new Astronauta(); rellenarEntidad(nuevo);
                service.guardar(nuevo); mostrarOk("Astronauta creado.");
            } else {
                rellenarEntidad(seleccionado);
                service.actualizar(seleccionado); mostrarOk("Astronauta actualizado.");
            }
            cargarTabla(); limpiarFormulario();
        } catch (IllegalArgumentException e) { mostrarError(e.getMessage()); }
    }

    @FXML private void onEliminar() {
        if (seleccionado == null) { mostrarError("Selecciona una fila primero."); return; }
        Alert c = new Alert(Alert.AlertType.CONFIRMATION,
            "¿Eliminar a " + seleccionado.getNombreCompleto() + "?", ButtonType.YES, ButtonType.NO);
        c.setHeaderText(null);
        c.showAndWait().ifPresent(bt -> { if (bt == ButtonType.YES) {
            service.eliminar(seleccionado.getId());
            cargarTabla(); limpiarFormulario(); mostrarOk("Astronauta eliminado.");
        }});
    }

    @FXML private void onNuevo()   { limpiarFormulario(); }
    @FXML private void onLimpiar() { limpiarFormulario(); }

    private void rellenarEntidad(Astronauta a) {
        a.setNombreCompleto(txtNombreCompleto.getText().trim());
        a.setNacionalidad(txtNacionalidad.getText().trim());
        a.setFechaNacimiento(dpFechaNacimiento.getValue());
        a.setEspecialidad(txtEspecialidad.getText().trim());
    }

    private void rellenarFormulario(Astronauta a) {
        seleccionado = a;
        txtNombreCompleto.setText(a.getNombreCompleto() != null ? a.getNombreCompleto() : "");
        txtNacionalidad.setText(a.getNacionalidad() != null ? a.getNacionalidad() : "");
        dpFechaNacimiento.setValue(a.getFechaNacimiento());
        txtEspecialidad.setText(a.getEspecialidad() != null ? a.getEspecialidad() : "");
        limpiarMsg();
    }

    private void limpiarFormulario() {
        seleccionado = null; txtNombreCompleto.clear(); txtNacionalidad.clear();
        dpFechaNacimiento.setValue(null); txtEspecialidad.clear();
        tablaAstronautas.getSelectionModel().clearSelection(); limpiarMsg();
    }

    private void mostrarError(String msg) { lblMensaje.setStyle("-fx-text-fill:#c62828;"); lblMensaje.setText("\u26a0 " + msg); new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait(); }
    private void mostrarOk(String msg)   { lblMensaje.setStyle("-fx-text-fill:#2e7d32;"); lblMensaje.setText("\u2714 " + msg); }
    private void mostrarInfo(String msg) { Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK); a.setHeaderText(null); a.showAndWait(); }
    private void limpiarMsg()            { lblMensaje.setText(""); }
}
