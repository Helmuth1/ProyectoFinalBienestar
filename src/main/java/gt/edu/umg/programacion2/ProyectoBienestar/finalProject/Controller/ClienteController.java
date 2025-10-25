/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Controller;

import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.Cliente;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.ClienteRepository;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private ClienteRepository clienteRepository;
    private final ClienteService service;

    @Autowired
    private ClienteService clienteService;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@Valid @RequestBody Cliente nuevoCliente) {

        Cliente clienteGuardado = service.guardarCliente(nuevoCliente);

        return new ResponseEntity<>(clienteGuardado, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Cliente> listAll() {
        return service.listar(); // O service.obtenerTodos() si lo refactorizaste
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(
            @PathVariable Long id,
            @RequestBody Cliente clienteActualizado) {

        Cliente clienteExistente = clienteService.obtener(id);
        clienteExistente.setNombreCompleto(clienteActualizado.getNombreCompleto());
        clienteExistente.setEmail(clienteActualizado.getEmail());
        clienteExistente.setTelefono(clienteActualizado.getTelefono());
        clienteExistente.setNit(clienteActualizado.getNit());

        Cliente actualizado = clienteService.save(clienteExistente);
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/perfil")
    public ResponseEntity<Cliente> obtenerPerfil(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername(); // viene del token JWT
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con el email: " + email));
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Cliente> cambiarEstado(@PathVariable Long id, @RequestParam boolean activo) {
        Cliente cliente = clienteService.obtener(id);
        cliente.setActivo(activo);
        Cliente actualizado = clienteService.save(cliente);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCliente(@PathVariable Long id) {
        try {
            clienteService.eliminar(id);
            return ResponseEntity.ok("Cliente eliminado");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
