package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Service;

import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.Servicio;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository.ServicioRepository;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.response.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    // LISTAR: Método para obtener todos (ya lo tenías)
    public List<Servicio> listarTodos() {
        return servicioRepository.findAll();
    }

    // GUARDAR: Método para crear (ya lo tenías)
    public Servicio guardarServicio(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    // 🚨 OBTENER (GET) - IMPLEMENTACIÓN REQUERIDA
    public Servicio obtener(Long id) {
        return servicioRepository.findById(id)
                // Lanza la excepción si no encuentra el servicio por ID
                .orElseThrow(() -> new ResourceNotFoundException("Servicio", "id", id));
    }

    // 🚨 ACTUALIZAR (PUT) - IMPLEMENTACIÓN REQUERIDA
    public Servicio actualizar(Long id, Servicio servicioActualizado) {
        // 1. Verificar si existe (usando el método obtener que maneja la excepción)
        Servicio servicioExistente = obtener(id);

        // 2. Aplicar los cambios al objeto existente
        servicioExistente.setNombre(servicioActualizado.getNombre());
        servicioExistente.setDescripcion(servicioActualizado.getDescripcion());
        servicioExistente.setPrecio(servicioActualizado.getPrecio());

        // 3. Guardar y devolver el objeto actualizado
        return servicioRepository.save(servicioExistente);
    }

    // 🚨 ELIMINAR (DELETE) - IMPLEMENTACIÓN REQUERIDA
    public void eliminar(Long id) {
        // Opcional: Verificar si existe antes de eliminar
        Servicio servicioExistente = obtener(id);

        servicioRepository.delete(servicioExistente);
    }
}
