/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;


/**
 *
 * @author helmu
 */
public class Notificacion {
    @Async // 👈 Para que la notificación no bloquee el servicio
    @EventListener
    public void manejarEventoCliente(ClienteCreadoEvent event) {
        // Lógica para enviar email, registrar en DB de auditoría, etc.
        System.out.println("Auditoría: Cliente " + event.getCliente().getId() + " - Acción: " + event.getAccion());
    }
}
