
package com.pasteleria.usuario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.pasteleria.usuario.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
    List<Usuario> findByEstatus(Integer  estatus);
    Optional<Usuario> findById(Integer id);
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.roles WHERE u.correo = :correo")
    Optional<Usuario> findByCorreoWithRoles(String correo);
}
