/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCita;
    private String fecha;
    private String hora;
    private String estado;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Servicio servicio;
}
