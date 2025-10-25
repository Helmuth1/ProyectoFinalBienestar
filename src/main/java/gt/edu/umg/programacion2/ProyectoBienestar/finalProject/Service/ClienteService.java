/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Service;

import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.Cliente;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Event.ClienteCreadoEvent;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.IClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author helmu
 */
@Service
public class ClienteService {

    private final IClienteRepository repo;

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public ClienteService(IClienteRepository repo, ApplicationEventPublisher eventPublisher) { // <-- Se inyecta la interfaz
        this.repo = repo;
        this.eventPublisher = eventPublisher;
    }

    @Autowired
    IClienteRepository clienteRepository;

    public boolean existsByEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }

    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente guardarCliente(Cliente cliente1) {

        String accion = (cliente1.getId() == null) ? "CREADO" : "ACTUALIZADO";

        Cliente clienteGuardado = repo.save(cliente1);

        String usuarioActual;
        try {
            usuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            // Fallback
            usuarioActual = "SYSTEM";
        }

        ClienteCreadoEvent evento = new ClienteCreadoEvent(
                this, // Fuente del evento
                clienteGuardado,
                accion
        );
        eventPublisher.publishEvent(evento);

        return clienteGuardado;
    }

    public List<Cliente> obtenerTodos() {
        return repo.findAll();
    }

    public Optional<Cliente> obtenerPorId(Long id) {
        return repo.findById(id);
    }

    // MÉTODOS CRUD
    public Cliente crear(String nit, String nombreCompleto, String email, String telefono, String password, boolean activo) {
        if (repo.findByEmail(email).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        Cliente nuevoCliente = new Cliente(nit, nombreCompleto, email, telefono, password, true);
        return repo.save(nuevoCliente);
    }

    public List<Cliente> listar() {
        return repo.findAll();
    }

    public Cliente obtener(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente con ID " + id + " no encontrado"));
    }

    // Método 'actualizar'
    public Cliente actualizar(Long id, Cliente clienteActualizado) {
        Cliente clienteExistente = obtener(id);

        clienteExistente.setNit(clienteActualizado.getNit());

        clienteExistente.setNombreCompleto(clienteActualizado.getNombreCompleto());
        clienteExistente.setEmail(clienteActualizado.getEmail());
        clienteExistente.setTelefono(clienteActualizado.getTelefono());

        return repo.save(clienteExistente);
    }

    public void eliminar(Long id) {
        // 7. Usar el método 'deleteById' de JpaRepository
        repo.deleteById(id);
    }

    public Cliente desactivarCliente(Long id) {
        Cliente cliente = obtener(id);
        cliente.setActivo(false);
        return repo.save(cliente);
    }
}
