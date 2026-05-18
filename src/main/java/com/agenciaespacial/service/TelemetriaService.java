package com.agenciaespacial.service;

import com.agenciaespacial.model.RegistroTelemetria;
import com.agenciaespacial.model.Satelite;
import com.agenciaespacial.repository.TelemetriaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la entidad {@link RegistroTelemetria}.
 * <p>
 * Es el único punto de acceso a datos de telemetría para la capa de
 * interfaz; ningún controlador debe llamar directamente al repositorio.
 * Aplica la siguiente regla de negocio antes de delegar en
 * {@link TelemetriaRepository}:
 * </p>
 * <ul>
 *   <li><b>RS-003</b>: el nivel de batería debe estar en el rango
 *       [0, 100].</li>
 * </ul>
 * <p>
 * Cuando la validación falla se lanza {@link IllegalArgumentException}
 * con un mensaje descriptivo que el controlador JavaFX mostrará en un
 * {@code Alert}.
 * </p>
 *
 * @author Carlos Fernández
 * @version 1.0
 */
public class TelemetriaService {

    /** Repositorio de acceso a datos de telemetría. */
    private final TelemetriaRepository telemetriaRepository;

    /**
     * Construye el servicio inicializando su repositorio asociado.
     */
    public TelemetriaService() {
        this.telemetriaRepository = new TelemetriaRepository();
    }

    /**
     * Valida y guarda un nuevo registro de telemetría en el sistema.
     * <p>
     * Aplica RS-003 (nivelBateria en rango 0–100).
     * </p>
     *
     * @param registro la entidad {@link RegistroTelemetria} a persistir;
     *                 no debe ser {@code null}
     * @return el registro guardado con su identificador asignado
     * @throws IllegalArgumentException si el nivel de batería está fuera
     *         del rango [0, 100]
     */
    public RegistroTelemetria guardar(RegistroTelemetria registro) {
        validarNivelBateria(registro.getNivelBateria());
        return telemetriaRepository.guardar(registro);
    }

    /**
     * Busca un registro de telemetría por su identificador único.
     *
     * @param id el identificador del registro
     * @return un {@link Optional} con el registro si existe, o vacío
     *         en caso contrario
     */
    public Optional<RegistroTelemetria> buscarPorId(Long id) {
        return telemetriaRepository.buscarPorId(id);
    }

    /**
     * Recupera todos los registros de telemetría asociados a un satélite.
     *
     * @param satelite el {@link Satelite} por el que filtrar; no debe ser
     *                 {@code null}
     * @return lista de registros de ese satélite; nunca {@code null}
     */
    public List<RegistroTelemetria> listarPorSatelite(Satelite satelite) {
        return telemetriaRepository.listarPorSatelite(satelite);
    }

    /**
     * Valida y actualiza los datos de un registro de telemetría existente.
     * <p>
     * Aplica RS-003 (nivelBateria en rango 0–100).
     * </p>
     *
     * @param registro la entidad {@link RegistroTelemetria} con los datos
     *                 actualizados; debe tener un identificador válido
     * @return el registro actualizado
     * @throws IllegalArgumentException si el nivel de batería está fuera
     *         del rango [0, 100]
     */
    public RegistroTelemetria actualizar(RegistroTelemetria registro) {
        validarNivelBateria(registro.getNivelBateria());
        return telemetriaRepository.actualizar(registro);
    }

    /**
     * Elimina el registro de telemetría con el identificador indicado.
     *
     * @param id el identificador del registro a eliminar
     */
    public void eliminar(Long id) {
        telemetriaRepository.eliminar(id);
    }

    // ── Métodos privados de validación ────────────────────────────────────

    /**
     * Comprueba que el nivel de batería esté en el rango [0, 100] (RS-003).
     *
     * @param nivelBateria el valor a validar
     * @throws IllegalArgumentException si el valor es nulo o está fuera del
     *         rango permitido
     */
    private void validarNivelBateria(Double nivelBateria) {
        if (nivelBateria == null) {
            throw new IllegalArgumentException(
                    "RS-003: El nivel de batería es obligatorio.");
        }
        if (nivelBateria < 0 || nivelBateria > 100) {
            throw new IllegalArgumentException(
                    "RS-003: El nivel de batería debe estar entre 0 y 100. "
                    + "Valor introducido: " + nivelBateria);
        }
    }
}
