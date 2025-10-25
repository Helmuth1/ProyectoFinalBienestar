package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Controller;

import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.Servicio;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.DTO.ServicioDTO;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.util.Optional;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ServicioController {

    private final ServicioRepository servicioRepository;

    @PostMapping("/registrar")
    public ResponseEntity<Servicio> crearServicio(@RequestBody ServicioDTO dto) {
        Servicio s = new Servicio();
        s.setNombre(dto.getNombre());
        s.setDescripcion(dto.getDescripcion());
        s.setPrecio(dto.getPrecio());
        s.setDuracionMinutos(dto.getDuracionMinutos());
        s.setActivo(dto.getActivo() != null ? dto.getActivo() : Boolean.TRUE);

        Servicio nuevo = servicioRepository.save(s);
        return ResponseEntity.ok(nuevo);
    }

    @GetMapping
    public List<Servicio> listarServicios() {
        return servicioRepository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarServicio(@PathVariable Long id, @RequestBody ServicioDTO dto) {
        return servicioRepository.findById(id)
                .map(servicio -> {
                    servicio.setNombre(dto.getNombre());
                    servicio.setDescripcion(dto.getDescripcion());
                    servicio.setPrecio(dto.getPrecio());
                    servicio.setDuracionMinutos(dto.getDuracionMinutos());
                    if (dto.getActivo() != null) servicio.setActivo(dto.getActivo());
                    return ResponseEntity.ok(servicioRepository.save(servicio));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarServicio(@PathVariable Long id) {
        if (servicioRepository.existsById(id)) {
            servicioRepository.deleteById(id);
            return ResponseEntity.ok("Servicio eliminado");
        }
        return ResponseEntity.notFound().build();
    }
}
