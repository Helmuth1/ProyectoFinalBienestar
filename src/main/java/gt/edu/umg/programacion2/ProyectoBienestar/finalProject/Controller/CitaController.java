package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Controller;

import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.Cita;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.Cliente;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.EstadoCita;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.Servicio;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.CitaRepository;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.ClienteRepository;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.ServicioRepository;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.request.CitaRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaRepository citaRepository;
    private final ClienteRepository clienteRepository;
    private final ServicioRepository servicioRepository;

    @PostMapping("/registrar")
    public ResponseEntity<?> crearCita(@RequestBody Cita cita) {
        Cliente cliente = clienteRepository.findById(cita.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Servicio servicio = servicioRepository.findById(cita.getServicio().getId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        cita.setCliente(cliente);
        cita.setServicio(servicio);
        cita.setEstado(EstadoCita.PENDIENTE);

        Cita nueva = citaRepository.save(cita);
        return ResponseEntity.ok(nueva);
    }

    @GetMapping
    public List<Cita> listarCitas() {
        return citaRepository.findAll();
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Cita>> obtenerCitasPorCliente(@PathVariable Long idCliente) {
        List<Cita> citas = citaRepository.findByClienteId(idCliente);
        if(citas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(citas);
    }

    @PutMapping("/{id}/atender")
    public ResponseEntity<?> marcarComoAtendida(@PathVariable Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        cita.setEstado(EstadoCita.ATENDIDA);
        return ResponseEntity.ok(citaRepository.save(cita));
    }
}
