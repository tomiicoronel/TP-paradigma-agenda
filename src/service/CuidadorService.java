package service;

import domain.Cuidador;
import infra.dao.CuidadorDAO;
import infra.dao.impl.CuidadorDAOImpl;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestión de cuidadores.
 *
 * Responsabilidades:
 * - CRUD de cuidadores
 * - Consulta de pacientes asignados
 * - Gestión de información de contacto
 */
public class CuidadorService {

    private final CuidadorDAO cuidadorDAO;

    public CuidadorService() {
        this.cuidadorDAO = new CuidadorDAOImpl();
    }

    /**
     * Crea un nuevo cuidador en el sistema.
     */
    public Long crearCuidador(String nombre, String contacto) {
        return crearCuidador(nombre, contacto, null);
    }

    /**
     * Crea un nuevo cuidador con todos los detalles.
     */
    public Long crearCuidador(String nombre, String telefono, String relacion) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cuidador es obligatorio");
        }

        Cuidador cuidador = new Cuidador();
        cuidador.setNombre(nombre);
        cuidador.setTelefono(telefono);
        cuidador.setRelacion(relacion);

        return cuidadorDAO.save(cuidador);
    }

    /**
     * Obtiene un cuidador por su ID.
     */
    public Optional<Cuidador> obtenerCuidador(Long id) {
        return cuidadorDAO.findById(id);
    }

    /**
     * Lista todos los cuidadores del sistema.
     */
    public List<Cuidador> listarTodos() {
        return cuidadorDAO.findAll();
    }

    /**
     * Actualiza la información de contacto de un cuidador.
     */
    public void actualizarContacto(Long cuidadorId, String nuevoContacto) {
        Optional<Cuidador> cuidadorOpt = cuidadorDAO.findById(cuidadorId);
        if (!cuidadorOpt.isPresent()) {
            throw new IllegalArgumentException("Cuidador no encontrado");
        }

        Cuidador cuidador = cuidadorOpt.get();
        cuidador.setContacto(nuevoContacto);
        cuidadorDAO.update(cuidador);
    }

    /**
     * Actualiza el nombre de un cuidador.
     */
    public void actualizarNombre(Long cuidadorId, String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        Optional<Cuidador> cuidadorOpt = cuidadorDAO.findById(cuidadorId);
        if (!cuidadorOpt.isPresent()) {
            throw new IllegalArgumentException("Cuidador no encontrado");
        }

        Cuidador cuidador = cuidadorOpt.get();
        cuidador.setNombre(nuevoNombre);
        cuidadorDAO.update(cuidador);
    }

    /**
     * Elimina un cuidador del sistema.
     * NOTA: En producción, verificar que no tenga pacientes asignados.
     */
    public void eliminarCuidador(Long cuidadorId) {
        // En un sistema real, verificar relaciones antes de eliminar
        cuidadorDAO.delete(cuidadorId);
    }
}

