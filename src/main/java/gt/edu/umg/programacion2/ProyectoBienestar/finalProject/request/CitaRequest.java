package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.request;

import lombok.Data;

@Data
public class CitaRequest {
    private String fecha;
    private String hora;
    private Long clienteId;
    private Long servicioId;
}
