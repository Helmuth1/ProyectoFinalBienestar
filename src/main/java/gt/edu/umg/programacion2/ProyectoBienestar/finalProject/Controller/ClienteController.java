/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Controller;

import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.cliente;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    // 1. Inyección de dependencia (La forma correcta en Spring)
    private final ClienteService service;

    // 2. Constructor para Inyección (Spring lo hace automáticamente con @Autowired o final)
    public ClienteController(ClienteService service) {
        this.service = service;
    }

    // --- POST: Crear un nuevo cliente (Corregido) ---
    @PostMapping
    public ResponseEntity<cliente> crearCliente(@Valid @RequestBody cliente nuevoCliente) {
        // 3. Llamada correcta: Usar la instancia 'service', no la clase 'ClienteService'
        cliente clienteGuardado = service.guardarCliente(nuevoCliente);

        return new ResponseEntity<>(clienteGuardado, HttpStatus.CREATED);
    }

    // ==== GET: Listar todos ====
    @GetMapping
    public List<cliente> listAll() {
        return service.listar(); // O service.obtenerTodos() si lo refactorizaste
    }

    // ==== GET: Obtener uno ====
    @GetMapping("/{id}")
    public ResponseEntity<cliente> get(@PathVariable Long id) {
        // El servicio ya maneja la excepción si no lo encuentra.
        return ResponseEntity.ok(service.obtener(id));
    }

    // ==== PUT: Actualizar (CORREGIDO Y OPTIMIZADO PARA POJOS) ====
    @PutMapping("/{id}")
    public ResponseEntity<cliente> update(@PathVariable Long id,
                                          @Valid @RequestBody cliente clienteActualizado) {

        // 4. Llamada al servicio con el objeto Cliente (Mejor Práctica)
        // **NOTA:** Asumo que actualizaste el método 'actualizar' en ClienteService
        // para que acepte el objeto Cliente en lugar de campos individuales.
        cliente actualizado = service.actualizar(id, clienteActualizado);

        return ResponseEntity.ok(actualizado);
    }

    // ==== DELETE: Eliminar ====
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
