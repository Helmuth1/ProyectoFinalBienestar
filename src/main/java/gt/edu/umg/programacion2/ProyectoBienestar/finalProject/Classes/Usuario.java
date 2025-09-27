/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

/**
 *
 * @author helmu
 */
@Entity
@Data
public class Usuario {

    @Id
    private Long id;
    @Column(nullable = false, unique = true)
    private String nombreUsuario;


    @Column(nullable = false)
    private String email;
}
