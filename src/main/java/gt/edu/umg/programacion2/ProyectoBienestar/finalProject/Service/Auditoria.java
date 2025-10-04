/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Service;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 *
 * @author helmu
 */
@Aspect
@Component
public class Auditoria {
    // Se ejecuta después de que cualquier método 'guardarCliente' en el servicio regrese exitosamente
    @AfterReturning(pointcut = "execution(* gt.edu.umg..ClienteService.guardarCliente(..))", returning = "result")
    public void logCreacionCliente(JoinPoint joinPoint, Object result) {
        // Lógica para registrar el evento de auditoría en la tabla 'auditoria'
        // [Obtener usuario autenticado] + [Registrar acción CREAR/ACTUALIZAR]
    }
}
