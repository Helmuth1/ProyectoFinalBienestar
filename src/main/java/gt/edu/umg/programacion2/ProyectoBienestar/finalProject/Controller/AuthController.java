/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Controller;

import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Component.JwUtil;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Service.ClienteService;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Service.UserDetailsImpl;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.request.LoginRequest;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.request.RegisterRequest;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.response.JwtResponse;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Classes.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author helmu
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwUtil jwtUtils;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    ClienteService clienteService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {

        if (clienteService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Error: ¡El correo electrónico ya está en uso!");
        }

        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setEmail(request.getEmail());
        nuevoCliente.setNit(request.getNit());
        nuevoCliente.setNombreCompleto(request.getNombreCompleto());
        nuevoCliente.setTelefono(request.getTelefono());

        // Encriptar la contraseña
        String encodedPassword = encoder.encode(request.getPassword());
        nuevoCliente.setPassword(encodedPassword);

        clienteService.save(nuevoCliente);

        return ResponseEntity.ok("Usuario registrado exitosamente.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token, userDetails.getId(), userDetails.getUsername(), "Login exitoso"));
    }
}
