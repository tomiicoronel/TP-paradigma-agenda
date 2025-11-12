package service;

import domain.*;
import infra.dao.*;
import infra.dao.impl.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestión de medicamentos y pautas de medicación.
 *
 * Responsabilidades:
 * - CRUD de medicamentos
 * - Creación y modificación de pautas (PacienteMedicamento)
 * - Programación de recordatorios basados en pautas
 * - Cálculo de próximas tomas
 */
public class MedicamentoService {

    private final MedicamentoDAO medicamentoDAO;
    private final PacienteMedicamentoDAO pmDAO;
    private final RecordatorioDAO recordatorioDAO;

    public MedicamentoService() {
        this.medicamentoDAO = new MedicamentoDAOImpl();
        this.pmDAO = new PacienteMedicamentoDAOImpl();
        this.recordatorioDAO = new RecordatorioDAOImpl();
    }

    /**
     * Crea un nuevo medicamento en el sistema.
     */
    public Long crearMedicamento(String nombre, String via, String unidadDosis, String notas) {
        Medicamento medicamento = new Medicamento();
        medicamento.setNombre(nombre);
        medicamento.setVia(via);
        medicamento.setUnidadDosis(unidadDosis);
        medicamento.setNotas(notas);

        return medicamentoDAO.save(medicamento);
    }

    /**
     * Obtiene un medicamento por su ID.
     */
    public Optional<Medicamento> obtenerMedicamento(Long id) {
        return medicamentoDAO.findById(id);
    }

    /**
     * Lista todos los medicamentos del sistema.
     */
    public List<Medicamento> listarTodos() {
        return medicamentoDAO.findAll();
    }

    /**
     * Crea una nueva pauta de medicación para un paciente.
     *
     * @param pacienteId ID del paciente
     * @param medicamentoId ID del medicamento
     * @param dosis Cantidad de dosis
     * @param unidad Unidad de medida (mg, ml, comprimidos, etc)
     * @param intervaloMin Intervalo entre tomas en minutos
     * @param ventanaMin Ventana de tolerancia en minutos
     * @param horaInicio Fecha y hora de la primera toma
     * @return ID de la pauta creada
     */
    public Long crearPauta(Long pacienteId, Long medicamentoId, int dosis, String unidad,
                           int intervaloMin, int ventanaMin, LocalDateTime horaInicio) {

        // Validar que el medicamento existe
        if (!medicamentoDAO.findById(medicamentoId).isPresent()) {
            throw new IllegalArgumentException("Medicamento no encontrado");
        }

        PacienteMedicamento pm = new PacienteMedicamento();
        pm.setPacienteId(pacienteId);
        pm.setMedicamentoId(medicamentoId);
        pm.setDosis(dosis);
        pm.setUnidad(unidad);
        pm.setIntervaloMin(intervaloMin);
        pm.setVentanaMin(ventanaMin);
        pm.setHoraInicio(horaInicio);
        pm.setProximaTomaAt(horaInicio);
        pm.setActivo(true);

        pmDAO.savePauta(pm);

        // Crear el primer recordatorio
        crearRecordatorioParaPauta(pm);

        return pacienteId; // Retornamos el ID del paciente como referencia
    }

    /**
     * Crea un recordatorio para una pauta de medicación.
     */
    private void crearRecordatorioParaPauta(PacienteMedicamento pm) {
        Recordatorio recordatorio = new Recordatorio();
        recordatorio.setPacienteId(pm.getPacienteId());
        recordatorio.setTipo("MEDICACION");
        recordatorio.setReferenciaTipo("PAC_MED");
        recordatorio.setReferenciaId(pm.getMedicamentoId());
        recordatorio.setProgramadoAt(pm.getProximaTomaAt());
        recordatorio.setVentanaMin(pm.getVentanaMin());

        recordatorioDAO.crearPendiente(recordatorio);
    }

    /**
     * Programa la siguiente toma de una pauta.
     * Se llama cuando una toma es completada.
     */
    public void programarSiguienteToma(Long pacienteId, Long medicamentoId) {
        Optional<PacienteMedicamento> pmOpt = pmDAO.findById(pacienteId, medicamentoId);
        if (!pmOpt.isPresent()) {
            throw new IllegalArgumentException("Pauta no encontrada");
        }

        PacienteMedicamento pm = pmOpt.get();

        if (!pm.isActivo()) {
            System.out.println("⚠️  Pauta inactiva, no se programa siguiente toma");
            return;
        }

        // Calcular próxima toma
        LocalDateTime proximaToma = pm.getProximaTomaAt().plusMinutes(pm.getIntervaloMin());
        pm.setProximaTomaAt(proximaToma);
        pmDAO.updateProximaToma(pacienteId, medicamentoId, proximaToma);

        // Crear nuevo recordatorio
        crearRecordatorioParaPauta(pm);

        System.out.println("✅ Próxima toma programada para: " + proximaToma);
    }

    /**
     * Desactiva una pauta de medicación.
     */
    public void desactivarPauta(Long pacienteId, Long medicamentoId) {
        pmDAO.marcarInactivo(pacienteId, medicamentoId);
    }

    /**
     * Reactiva una pauta de medicación.
     */
    public void activarPauta(Long pacienteId, Long medicamentoId, LocalDateTime nuevaHoraInicio) {
        Optional<PacienteMedicamento> pmOpt = pmDAO.findById(pacienteId, medicamentoId);
        if (!pmOpt.isPresent()) {
            throw new IllegalArgumentException("Pauta no encontrada");
        }

        PacienteMedicamento pm = pmOpt.get();
        pm.setActivo(true);
        pm.setProximaTomaAt(nuevaHoraInicio);
        pmDAO.updateProximaToma(pacienteId, medicamentoId, nuevaHoraInicio);

        // Crear recordatorio para la reactivación
        crearRecordatorioParaPauta(pm);
    }

    /**
     * Obtiene todas las pautas de un paciente.
     */
    public List<PacienteMedicamento> obtenerPautasPorPaciente(Long pacienteId) {
        return pmDAO.findActivosByPaciente(pacienteId);
    }
}

