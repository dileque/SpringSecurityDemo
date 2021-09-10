package springsecurity.demo.SpringSecurityDemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springsecurity.demo.SpringSecurityDemo.util.JwtTokenProvider;
import springsecurity.demo.SpringSecurityDemo.util.LdapAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class DemoSecurityConfiguration extends WebSecurityConfigurerAdapter
{
    private Environment env;

    public DemoSecurityConfiguration(Environment env){
        this.env = env;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.authenticationProvider(new LdapAuthenticationProvider(env)).eraseCredentials(false);
               /* .ldapAuthentication()
                .passwordCompare()
                .passwordEncoder(new BCryptPasswordEncoder())
                .passwordAttribute("userPassword");*/
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/auth-server").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf()
                .disable()
                .httpBasic();

    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtTokenProvider provider(){
        return new JwtTokenProvider();
    }

}
