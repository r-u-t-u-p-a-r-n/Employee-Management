package site.operations.Repositories;

import site.operations.Models.BasicAuth;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BasicAuthRepository extends JpaRepository<BasicAuth,String>
{
	Optional<BasicAuth> findByEmail(String email);
}