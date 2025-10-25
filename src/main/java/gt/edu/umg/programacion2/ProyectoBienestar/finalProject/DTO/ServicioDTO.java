package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicioDTO {
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer duracionMinutos;
    private Boolean activo;
}
