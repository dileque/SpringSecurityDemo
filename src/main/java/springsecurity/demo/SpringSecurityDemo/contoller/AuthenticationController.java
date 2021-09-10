package springsecurity.demo.SpringSecurityDemo.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springsecurity.demo.SpringSecurityDemo.model.AuthenticationResponse;
import springsecurity.demo.SpringSecurityDemo.util.JwtTokenProvider;
import springsecurity.demo.SpringSecurityDemo.util.LdapAuthenticationProvider;

@RestController
@RequestMapping("/auth-server")
public class AuthenticationController
{
    private static final String USER_DISABLED = "USER DISABLED";
    private static final String INVALID_CREDENTIALS = "INVALID CREDENTIALS";

    @Autowired
    private LdapAuthenticationProvider authenticationProvider;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<?> authenticateRequest() throws Exception{

        authenticate(SecurityContextHolder.getContext().getAuthentication());

        final String token = JwtTokenProvider.generateToken(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    private void authenticate(Authentication auth) throws Exception {
        try {
            authenticationProvider.authenticate(auth);
        } catch (DisabledException e) {
            throw new Exception(USER_DISABLED, e);
        } catch (BadCredentialsException e) {
            throw new Exception(INVALID_CREDENTIALS, e);
        }
    }

}