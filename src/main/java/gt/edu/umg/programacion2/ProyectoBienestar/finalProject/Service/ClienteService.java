/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Service;

import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.cliente;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.IClienteRepository; // Usaremos esta como la interfaz de JpaRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author helmu
 */
@Service
public class ClienteService {

    // 1. Uso de la interfaz (IClienteRepository) en lugar de la clase de implementación
    private final IClienteRepository repo;

    @Autowired
    public ClienteService(IClienteRepository repo) { // <-- Se inyecta la interfaz
        this.repo = repo;
    }

    // --- MÉTODOS CRUD BÁSICOS (Corregidos de los errores iniciales) ---

    // Método para crear/actualizar un cliente
    public cliente guardarCliente(cliente cliente1) {
        // 2. Uso correcto de la variable de instancia 'repo' y no la clase 'ClienteRepository'
        return repo.save(cliente1);
    }

    // Método para obtener todos
    public List<cliente> obtenerTodos() {
        // Uso correcto de la variable de instancia 'repo'
        return repo.findAll();
    }

    // Método para obtener por ID
    public Optional<cliente> obtenerPorId(Long id) {
        // Uso correcto de la variable de instancia 'repo'
        return repo.findById(id);
    }

    // --- MÉTODOS CRUD ANTIGUOS/PERSONALIZADOS (Refactorizados a JPA) ---

    // Método 'crear' refactorizado para usar la entidad Cliente y el Repositorio
    public cliente crear(String nit, String nombreCompleto, String email, String telefono) {
        // **IMPORTANTE:** En lugar de buscar en memoria, buscamos usando un método del Repositorio.
        // Asumiendo que has añadido: 'Optional<cliente> findByEmail(String email);' en IClienteRepository
        if (repo.findByEmail(email).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        // 3. Crear una nueva instancia del cliente (Necesitarás un constructor adecuado)
        cliente nuevoCliente = new cliente(nit, nombreCompleto, email, telefono);
        return repo.save(nuevoCliente);
    }

    // Método 'listar' (Ahora es el mismo que obtenerTodos)
    public List<cliente> listar() {
        return repo.findAll();
    }

    // Método 'obtener' (Buscar por ID con manejo de excepciones)
    public cliente obtener(Long id) {
        // 4. Se usa .orElseThrow() para manejar el Optional de forma moderna
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente con ID " + id + " no encontrado"));
    }

    // Método 'actualizar'
    public cliente actualizar(Long id, cliente clienteActualizado) {
        // 1. Busca el cliente existente. El método 'obtener' maneja la excepción si no existe.
        cliente clienteExistente = obtener(id);

        // 2. Actualización de campos
        // Se actualizan los campos con los valores del objeto clienteActualizado

        // **Nota:** Si el NIT debe ser inmutable, puedes omitir esta línea
        // o incluir lógica de validación extra.
        clienteExistente.setNit(clienteActualizado.getNit());

        clienteExistente.setNombreCompleto(clienteActualizado.getNombreCompleto());
        clienteExistente.setEmail(clienteActualizado.getEmail());
        clienteExistente.setTelefono(clienteActualizado.getTelefono());

        // 3. Guarda el cliente modificado.
        return repo.save(clienteExistente);
    }

    // Método 'eliminar'
    public void eliminar(Long id) {
        // 7. Usar el método 'deleteById' de JpaRepository
        repo.deleteById(id);
    }
}
