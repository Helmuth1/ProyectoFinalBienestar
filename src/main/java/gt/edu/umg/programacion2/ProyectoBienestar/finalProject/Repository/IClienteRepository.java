/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository;


import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface IClienteRepository extends JpaRepository<cliente, Long> {

    Optional<cliente> findByEmail(String email);

    // Si la creaste en el paso anterior, esta debe existir:
    Optional<cliente> findByNit(String nit);
}

