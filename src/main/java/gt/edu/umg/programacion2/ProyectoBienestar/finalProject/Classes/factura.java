/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFactura;
    private String fecha;
    private Double monto;

    @ManyToOne
    private Cliente cliente;

    @OneToOne
    private Cita cita;
}
