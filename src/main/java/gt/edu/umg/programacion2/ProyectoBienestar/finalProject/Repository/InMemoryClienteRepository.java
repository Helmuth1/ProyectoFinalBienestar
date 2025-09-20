/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository;

import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.cliente;
import java.util.List;
import java.util.*;

public class InMemoryClienteRepository implements IClienteRepository {
    
    private final Map<Long, cliente> store = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public cliente save(cliente cliente) {
        if (cliente.getId() == null) {
            // Genera un nuevo ID
            cliente = new cliente(nextId++, cliente.getNombre(), cliente.getEmail(), cliente.getTelefono());
        }
        store.put(cliente.getId(), cliente);
        return cliente;
    }

    @Override
    public cliente findById(Long id) {
        return store.get(id);
    }

    @Override
    public List<cliente> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }
    
}
