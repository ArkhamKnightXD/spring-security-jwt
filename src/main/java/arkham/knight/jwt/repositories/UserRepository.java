package arkham.knight.jwt.repositories;

import arkham.knight.jwt.models.AuthenticationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AuthenticationRequest, Long> {

    AuthenticationRequest findByUsername(String username);
}
