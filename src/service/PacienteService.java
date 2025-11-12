package service;

import domain.*;
import infra.dao.*;
import infra.dao.impl.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestión de pacientes y sus pautas de medicación.
 *
 * Responsabilidades:
 * - CRUD de pacientes
 * - Asignación de cuidadores
 * - Gestión de preferencias de accesibilidad
 * - Consulta de historial de medicación
 */
public class PacienteService {

    private final PacienteDAO pacienteDAO;
    private final CuidadorDAO cuidadorDAO;
    private final PacienteMedicamentoDAO pmDAO;

    public PacienteService() {
        this.pacienteDAO = new PacienteDAOImpl();
        this.cuidadorDAO = new CuidadorDAOImpl();
        this.pmDAO = new PacienteMedicamentoDAOImpl();
    }

    /**
     * Crea un nuevo paciente en el sistema.
     */
    public Long crearPaciente(String nombre, String preferenciasAccesibilidad, Long cuidadorId) {
        // Validar que el cuidador existe
        if (cuidadorId != null) {
            Optional<Cuidador> cuidador = cuidadorDAO.findById(cuidadorId);
            if (!cuidador.isPresent()) {
                throw new IllegalArgumentException("El cuidador con ID " + cuidadorId + " no existe");
            }
        }

        Paciente paciente = new Paciente();
        paciente.setNombre(nombre);
        paciente.setPreferenciasAccesibilidad(preferenciasAccesibilidad);
        paciente.setCuidadorId(cuidadorId);

        return pacienteDAO.save(paciente);
    }

    /**
     * Obtiene un paciente por su ID.
     */
    public Optional<Paciente> obtenerPaciente(Long id) {
        return pacienteDAO.findById(id);
    }

    /**
     * Lista todos los pacientes del sistema.
     */
    public List<Paciente> listarTodos() {
        return pacienteDAO.findAll();
    }

    /**
     * Actualiza las preferencias de accesibilidad de un paciente.
     */
    public void actualizarPreferencias(Long pacienteId, String nuevasPreferencias) {
        Optional<Paciente> pacienteOpt = pacienteDAO.findById(pacienteId);
        if (!pacienteOpt.isPresent()) {
            throw new IllegalArgumentException("Paciente no encontrado");
        }

        Paciente paciente = pacienteOpt.get();
        paciente.setPreferenciasAccesibilidad(nuevasPreferencias);
        pacienteDAO.update(paciente);
    }

    /**
     * Asigna o cambia el cuidador de un paciente.
     */
    public void asignarCuidador(Long pacienteId, Long cuidadorId) {
        Optional<Paciente> pacienteOpt = pacienteDAO.findById(pacienteId);
        if (!pacienteOpt.isPresent()) {
            throw new IllegalArgumentException("Paciente no encontrado");
        }

        Optional<Cuidador> cuidadorOpt = cuidadorDAO.findById(cuidadorId);
        if (!cuidadorOpt.isPresent()) {
            throw new IllegalArgumentException("Cuidador no encontrado");
        }

        Paciente paciente = pacienteOpt.get();
        paciente.setCuidadorId(cuidadorId);
        pacienteDAO.update(paciente);
    }

    /**
     * Obtiene todas las pautas de medicación activas de un paciente.
     */
    public List<PacienteMedicamento> obtenerPautasActivas(Long pacienteId) {
        return pmDAO.findActivosByPaciente(pacienteId);
    }

    /**
     * Elimina un paciente del sistema (soft delete recomendado en producción).
     */
    public void eliminarPaciente(Long pacienteId) {
        // En un sistema real, aquí deberías verificar que no haya
        // recordatorios pendientes o hacer un soft delete
        pacienteDAO.delete(pacienteId);
    }
}

