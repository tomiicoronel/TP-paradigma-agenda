package service;

import domain.Medicamento;
import domain.Paciente;
import domain.PacienteMedicamento;
import infra.dao.MedicamentoDAO;
import infra.dao.PacienteDAO;
import infra.dao.PacienteMedicamentoDAO;
import infra.dao.impl.MedicamentoDAOImpl;
import infra.dao.impl.PacienteDAOImpl;
import infra.dao.impl.PacienteMedicamentoDAOImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar pautas de medicación.
 *
 * Responsabilidades:
 * - Asignar medicamentos a pacientes
 * - Configurar horarios y dosis
 * - Gestionar el estado activo/inactivo de pautas
 * - Validar que paciente y medicamento existan
 */
public class PautaMedicacionService {

    private final PacienteMedicamentoDAO pautaDAO;
    private final PacienteDAO pacienteDAO;
    private final MedicamentoDAO medicamentoDAO;

    public PautaMedicacionService() {
        this.pautaDAO = new PacienteMedicamentoDAOImpl();
        this.pacienteDAO = new PacienteDAOImpl();
        this.medicamentoDAO = new MedicamentoDAOImpl();
    }

    /**
     * Crea una nueva pauta de medicación.
     */
    public boolean crearPauta(Long pacienteId, Long medicamentoId, double dosis,
                             String unidad, int intervaloHoras, LocalDateTime horaInicio) {

        // Validar que el paciente existe
        Optional<Paciente> paciente = pacienteDAO.findById(pacienteId);
        if (!paciente.isPresent()) {
            throw new IllegalArgumentException("El paciente con ID " + pacienteId + " no existe");
        }

        // Validar que el medicamento existe
        Optional<Medicamento> medicamento = medicamentoDAO.findById(medicamentoId);
        if (!medicamento.isPresent()) {
            throw new IllegalArgumentException("El medicamento con ID " + medicamentoId + " no existe");
        }

        // Validar parámetros
        if (dosis <= 0) {
            throw new IllegalArgumentException("La dosis debe ser mayor a 0");
        }

        if (intervaloHoras <= 0) {
            throw new IllegalArgumentException("El intervalo debe ser mayor a 0 horas");
        }

        // Crear la pauta
        PacienteMedicamento pauta = new PacienteMedicamento();
        pauta.setPacienteId(pacienteId);
        pauta.setMedicamentoId(medicamentoId);
        pauta.setDosis(dosis);
        pauta.setUnidad(unidad);
        pauta.setIntervaloMin(intervaloHoras * 60);
        pauta.setHoraInicio(horaInicio != null ? horaInicio : LocalDateTime.now());
        pauta.setProximaTomaAt(pauta.getHoraInicio());
        pauta.setActivo(true);

        pautaDAO.save(pauta);
        return true;
    }

    /**
     * Obtiene todas las pautas activas de un paciente.
     */
    public List<PacienteMedicamento> obtenerPautasActivasPaciente(Long pacienteId) {
        return pautaDAO.findActivosByPaciente(pacienteId);
    }

    /**
     * Obtiene todas las pautas (activas e inactivas).
     */
    public List<PacienteMedicamento> listarTodasLasPautas() {
        ArrayList<PacienteMedicamento> todasLasPautas = new ArrayList<>();
        List<Paciente> pacientes = pacienteDAO.findAll();

        for (Paciente p : pacientes) {
            List<PacienteMedicamento> pautasPaciente = pautaDAO.findByPacienteId(p.getId());
            todasLasPautas.addAll(pautasPaciente);
        }

        return todasLasPautas;
    }

    /**
     * Desactiva una pauta de medicación.
     */
    public boolean desactivarPauta(Long pacienteId, Long medicamentoId) {
        pautaDAO.marcarInactivo(pacienteId, medicamentoId);
        return true;
    }

    /**
     * Actualiza la próxima toma de una pauta después de registrar una toma.
     */
    public void actualizarProximaToma(Long pacienteId, Long medicamentoId) {
        PacienteMedicamento pauta = pautaDAO.findByPacienteYMedicamento(pacienteId, medicamentoId);

        if (pauta != null && pauta.isActivo()) {
            LocalDateTime proximaToma = pauta.getProximaTomaAt().plusMinutes(pauta.getIntervaloMin());
            pautaDAO.updateProximaToma(pacienteId, medicamentoId, proximaToma);
        }
    }
}

