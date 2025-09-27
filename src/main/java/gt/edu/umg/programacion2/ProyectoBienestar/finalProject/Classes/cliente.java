/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class cliente {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID de cliente

    @Column(nullable = false, unique = true) 
    private String nit; // Por ejemplo, un campo único para el cliente

    @Column(nullable = false)
    private String nombreCompleto;
    
    private String email;
    private String telefono;

    public cliente(String nit, String nombreCompleto, String email, String telefono) {
        this.nit = nit;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.telefono = telefono;
    }

}
