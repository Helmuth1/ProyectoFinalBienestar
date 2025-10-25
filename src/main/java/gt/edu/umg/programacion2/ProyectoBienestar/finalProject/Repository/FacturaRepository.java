package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository;

import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.Cliente;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findByClienteId(Long clienteId);
    List<Factura> findByCliente(Cliente cliente);
    List<Factura> findAllByFechaEmisionBetween(LocalDate inicio, LocalDate fin);
}
