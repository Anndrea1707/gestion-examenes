package com.gestionExamenes.app.servicio;

import com.gestionExamenes.app.entidad.Coordinador;
import com.gestionExamenes.app.entidad.Estudiante;
import com.gestionExamenes.app.repositorio.CoordinadorRepositorio;
import com.gestionExamenes.app.repositorio.EstudianteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EstudianteRepositorio estudianteRepository;

    @Autowired
    private CoordinadorRepositorio coordinadorRepository;

    public CustomUserDetailsService() {
        System.out.println("CustomUserDetailsService inicializado");
    }

    @Override
    public UserDetails loadUserByUsername(String documento) throws UsernameNotFoundException {
        System.out.println("Intentando autenticar usuario con documento: " + documento);

        Coordinador coordinador = coordinadorRepository.findByDocumento(documento);
        if (coordinador != null) {
            System.out.println("Coordinador encontrado: " + coordinador.getDocumento());
            UserDetails user = User.withUsername(coordinador.getDocumento())
                    .password(coordinador.getPassword())
                    .roles("COORDINADOR")
                    .build();
            System.out.println("Usuario creado: " + user.getUsername() + ", roles: " + user.getAuthorities());
            return user;
        } else {
            System.out.println("No se encontró coordinador con documento: " + documento);
        }

        Estudiante estudiante = estudianteRepository.findByDocumento(documento);
        if (estudiante != null) {
            System.out.println("Estudiante encontrado: " + estudiante.getDocumento());
            UserDetails user = User.withUsername(estudiante.getDocumento())
                    .password(estudiante.getContrasena())
                    .roles("ESTUDIANTE")
                    .build();
            System.out.println("Usuario creado: " + user.getUsername() + ", roles: " + user.getAuthorities());
            return user;
        } else {
            System.out.println("No se encontró estudiante con documento: " + documento);
        }

        System.out.println("Usuario no encontrado para documento: " + documento);
        throw new UsernameNotFoundException("Usuario no encontrado: " + documento);
    }
}