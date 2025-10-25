package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository;

import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.Cita;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByClienteId(Long clienteId);
    List<Cita> findByCliente(Cliente cliente);
    List<Cita> findByClienteIdAndEstado(Long clienteId, String estado);
    @Query("SELECT c FROM Cita c JOIN FETCH c.cliente JOIN FETCH c.servicio")
    List<Cita> findAllWithRelations();
}
