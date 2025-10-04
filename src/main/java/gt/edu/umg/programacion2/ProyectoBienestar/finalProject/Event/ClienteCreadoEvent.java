/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Event;

import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.cliente;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
/**
 *
 * @author helmu
 */
@Getter
public class ClienteCreadoEvent extends ApplicationEvent {

    private final cliente cliente;
    private final String accion; // "CREADO", "ACTUALIZADO", "ELIMINADO"

    public ClienteCreadoEvent(Object source, cliente cliente, String accion ) {
        super(source); // Esto es obligatorio al extender ApplicationEvent
        this.cliente = cliente;
        this.accion = accion;

    }
    
}
