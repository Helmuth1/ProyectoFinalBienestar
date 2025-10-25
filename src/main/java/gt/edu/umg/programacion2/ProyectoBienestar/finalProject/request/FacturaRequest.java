package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.request;

import lombok.Data;

import java.util.List;

@Data
public class FacturaRequest {
    private Long idCliente;
    private List<Long> idsCitas;
}
