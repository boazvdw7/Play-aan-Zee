package app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;


@Configuration
public class APIConfig implements WebMvcConfigurer {
    // JWT configuration that can be adjusted from application.properties
    @Value("HvA")
    private String issuer;

    @Value("${jwt.pass-phrase: This is very secret information for my private encryption key.")
    private String passphrase;

    @Value("1200") // default 20 minutes;
    public int tokenDuration0fValidity;

    /////////////////////////////////////////////////////////////
    // Paths added here require a valid bearer token to access //
    /////////////////////////////////////////////////////////////
    public final Set<String> SECURED_PATHS = Set.of(
            "/cabins"
    );

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, HttpHeaders.ACCEPT, HttpHeaders.ACCEPT_LANGUAGE)
                .exposedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, HttpHeaders.ACCEPT, HttpHeaders.ACCEPT_LANGUAGE)
                .allowCredentials(true);
    }

    public String getPassphrase() {
        return this.passphrase;
    }

    public String getIssuer() {
        return this.issuer;
    }

    public int getTokenDuration0fValidity() {
        return this.tokenDuration0fValidity;
    }
}
