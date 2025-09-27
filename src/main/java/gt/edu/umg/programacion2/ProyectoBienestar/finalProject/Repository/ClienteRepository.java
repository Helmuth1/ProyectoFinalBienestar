/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository;

import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.cliente;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author helmu
 */
public interface ClienteRepository extends JpaRepository<cliente, Long> {
    cliente findByNit(String nit);

}
