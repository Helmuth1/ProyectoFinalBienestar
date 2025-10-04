/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import gt.edu.umg.programacion2.ProyectoBienestar.finalProject.Component.JwUtil;



/**
 *
 * @author helmu
 */
@Service
public class AutenticacionFacade {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwUtil jwtUtil;

    @Autowired
    public AutenticacionFacade(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwUtil jwtUtil
    ) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public String autenticarYGenerarToken(String username, String password) {
        try {
            // 1. Autentica las credenciales
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Credenciales inválidas para el usuario: " + username, e);
        }

        // 2. Carga los detalles del usuario usando la dependencia inyectada
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 3. Genera el token usando la dependencia inyectada
        return jwtUtil.generateToken(userDetails);
    }


}

