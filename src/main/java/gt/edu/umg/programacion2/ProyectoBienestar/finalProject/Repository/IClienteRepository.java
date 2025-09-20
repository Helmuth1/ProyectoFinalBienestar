/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Repository;


import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.cliente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IClienteRepository {
     cliente save(cliente cliente);
     cliente findById(Long id);
     List<cliente> findAll();
     void delete(Long id);
}

