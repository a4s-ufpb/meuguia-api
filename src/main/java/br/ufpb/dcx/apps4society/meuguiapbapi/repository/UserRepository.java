package br.ufpb.dcx.apps4society.meuguiapbapi.repository;

import br.ufpb.dcx.apps4society.meuguiapbapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByEmail(String email);
}
