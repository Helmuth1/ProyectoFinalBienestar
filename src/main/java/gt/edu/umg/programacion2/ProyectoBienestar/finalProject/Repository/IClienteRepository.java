/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository;


import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface IClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByEmail(String email);
    Optional<Cliente> findByEmail(String email);
}

