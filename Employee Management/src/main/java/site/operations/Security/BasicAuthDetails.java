package site.operations.Security;

import site.operations.Models.BasicAuth;
import site.operations.Repositories.BasicAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class BasicAuthDetails implements UserDetailsService {
    @Autowired
    private BasicAuthRepository basicAuthRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       BasicAuth basicAuth = this.basicAuthRepository.findByEmail(username).orElseThrow(null);
       return basicAuth ;
    }
}