package com.agenciaespacial.ui.vehiculo;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controlador UI para CRUD de Vehículos de Lanzamiento.
 *
 * Responsabilidades:
 * - Listar, buscar por ID, crear, actualizar y eliminar {@code VehiculoLanzamiento}.
 * - Validar la capacidad en kg (campo {@code capacidadKg}) como número.
 * - Rellenar formulario y sincronizar selección en la tabla.
 *
 * Autor: Antonio Manuel Rodriguez Palenzuela
 */
public class VehiculoController {

    @FXML private TableView<VehiculoLanzamiento>             tablaVehiculos;
    @FXML private TableColumn<VehiculoLanzamiento, Long>     colId;
    @FXML private TableColumn<VehiculoLanzamiento, String>   colNombre;
    @FXML private TableColumn<VehiculoLanzamiento, String>   colModelo;
    @FXML private TableColumn<VehiculoLanzamiento, String>   colCapacidad;
    @FXML private TableColumn<VehiculoLanzamiento, String>   colPais;
    @FXML private TextField txtBuscarId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtModelo;
    @FXML private TextField txtCapacidadKg;
    @FXML private TextField txtPaisFabricacion;
    @FXML private Label     lblMensaje;

    private final VehiculoService service = new VehiculoService();
    private VehiculoLanzamiento seleccionado = null;

    @FXML public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colCapacidad.setCellValueFactory(cd -> new SimpleStringProperty(
            cd.getValue().getCapacidadKg() != null ? cd.getValue().getCapacidadKg() + " kg" : ""));
        colPais.setCellValueFactory(new PropertyValueFactory<>("paisFabricacion"));
        tablaVehiculos.getSelectionModel().selectedItemProperty()
            .addListener((obs, ant, nuevo) -> { if (nuevo != null) rellenarFormulario(nuevo); });
        cargarTabla();
    }

    private void cargarTabla() {
        try { tablaVehiculos.getItems().setAll(service.listarTodos()); limpiarMsg(); }
        catch (Exception e) { mostrarError("Error al cargar: " + e.getMessage()); }
    }

    @FXML private void onActualizarLista() { cargarTabla(); }

    @FXML private void onBuscarId() {
        String raw = txtBuscarId.getText().trim();
        if (raw.isEmpty()) { cargarTabla(); return; }
        try {
            VehiculoLanzamiento v = service.buscarPorId(Long.parseLong(raw)).orElse(null);            if (v == null) mostrarInfo("No existe vehículo con ID " + raw);
            else { tablaVehiculos.getItems().setAll(v); rellenarFormulario(v); }
        } catch (NumberFormatException e) { mostrarError("El ID debe ser un número entero."); }
    }

    @FXML private void onGuardar() {
        try {
            double cap = parseDouble(txtCapacidadKg.getText().trim(), "Capacidad (kg)");
            if (seleccionado == null) {
                VehiculoLanzamiento nuevo = new VehiculoLanzamiento(); rellenarEntidad(nuevo, cap);
                service.guardar(nuevo); mostrarOk("Vehículo creado.");
            } else {
                rellenarEntidad(seleccionado, cap);
                service.actualizar(seleccionado); mostrarOk("Vehículo actualizado.");
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
            cargarTabla(); limpiarFormulario(); mostrarOk("Vehículo eliminado.");
        }});
    }

    @FXML private void onNuevo()   { limpiarFormulario(); }
    @FXML private void onLimpiar() { limpiarFormulario(); }

    private void rellenarEntidad(VehiculoLanzamiento v, double cap) {
        v.setNombre(txtNombre.getText().trim()); v.setModelo(txtModelo.getText().trim());
        v.setCapacidadKg(cap); v.setPaisFabricacion(txtPaisFabricacion.getText().trim());
    }

    private void rellenarFormulario(VehiculoLanzamiento v) {
        seleccionado = v;
        txtNombre.setText(v.getNombre() != null ? v.getNombre() : "");
        txtModelo.setText(v.getModelo() != null ? v.getModelo() : "");
        txtCapacidadKg.setText(v.getCapacidadKg() != null ? v.getCapacidadKg().toString() : "");
        txtPaisFabricacion.setText(v.getPaisFabricacion() != null ? v.getPaisFabricacion() : "");
        limpiarMsg();
    }

    private void limpiarFormulario() {
        seleccionado = null; txtNombre.clear(); txtModelo.clear();
        txtCapacidadKg.clear(); txtPaisFabricacion.clear();
        tablaVehiculos.getSelectionModel().clearSelection(); limpiarMsg();
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
