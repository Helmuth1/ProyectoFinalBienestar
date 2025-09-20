/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Service;

import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.cliente;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.IClienteRepository;

import java.util.List;

/**
 *
 * @author helmu
 */
public class ClienteService {
    private final IClienteRepository repo;

    public ClienteService(IClienteRepository repo) {
        this.repo = repo;
    }

    public cliente crear(String nombre, String email, String telefono) {
        // Validar unicidad del email
        boolean existe = repo.findAll().stream()
                             .anyMatch(c -> c.getEmail().equalsIgnoreCase(email));
        if (existe) throw new RuntimeException("El email ya está registrado");

        return repo.save(new cliente(null, nombre, email, telefono));
    }

    public List<cliente> listar() {
        return repo.findAll();
    }

    public cliente obtener(Long id) {
        cliente c = repo.findById(id);
        if (c == null) throw new RuntimeException("Cliente no encontrado");
        return c;
    }

    public cliente actualizar(Long id, String nombre, String email) {
        cliente c = repo.findById(id);
        if (c == null) throw new RuntimeException("Cliente no encontrado");

        c.setNombre(nombre);
        c.setEmail(email);
        return repo.save(c);
    }

    public void eliminar(Long id) {
        repo.delete(id);
    }
}
