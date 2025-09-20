/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Controller;

import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.cliente;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.IClienteRepository;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.InMemoryClienteRepository;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Service.ClienteService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteService service;

    public ClienteController() {
        IClienteRepository repo = new InMemoryClienteRepository();
        this.service = new ClienteService(repo);
    }

    // ==== POST: Crear ====
    @PostMapping
    public ResponseEntity<cliente> create(@RequestBody Map<String, Object> body) {
        String nombre = String.valueOf(body.get("nombre"));
        String email = String.valueOf(body.get("email"));
        String telefono = String.valueOf(body.get("telefono"));
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(service.crear(nombre, email, telefono));
    }

    // ==== GET: Listar todos ====
    @GetMapping
    public List<cliente> listAll() {
        return service.listar();
    }

    // ==== GET: Obtener uno ====
    @GetMapping("/{id}")
    public ResponseEntity<cliente> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    // ==== PUT: Actualizar ====
    @PutMapping("/{id}")
    public ResponseEntity<cliente> update(@PathVariable Long id,
                                          @RequestBody Map<String, Object> body) {
        String nombre = String.valueOf(body.get("nombre"));
        String email = String.valueOf(body.get("email"));
        return ResponseEntity.ok(service.actualizar(id, nombre, email));
    }

    // ==== DELETE: Eliminar ====
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
