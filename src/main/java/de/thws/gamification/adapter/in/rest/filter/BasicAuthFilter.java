package de.thws.gamification.adapter.in.rest.filter;

import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.domain.model.DriverProfile;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;
import java.util.Optional;

@Provider
@Priority(Priorities.AUTHENTICATION) // En önce bu çalışsın
public class BasicAuthFilter implements ContainerRequestFilter {

    private static final Logger LOG = Logger.getLogger(BasicAuthFilter.class);

    @Inject
    DriverProfileRepository driverProfileRepository;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        //reading Header
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.toLowerCase().startsWith("basic")) {
            return;
        }

        //decoding process
        String base64Credentials = authHeader.substring("Basic ".length()).trim();
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(decodedBytes, StandardCharsets.UTF_8);

        final String[] values = credentials.split(":", 2);
        if (values.length != 2) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        String username = values[0];
        String password = values[1];

        //confirming
        Optional<DriverProfile> userOpt = driverProfileRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            DriverProfile user = userOpt.get();

            //i wanted to see everything ok with login
            LOG.infof("LOGIN ATTEMPT -> User: %s | Role in DB: %s", user.getUsername(), user.getRole());

            if (user.getPassword() != null && user.getPassword().equals(password)) {

                requestContext.setSecurityContext(new SecurityContext() {
                    @Override
                    public Principal getUserPrincipal() {
                        return () -> username;
                    }
                    @Override
                    public boolean isUserInRole(String role) {
                        LOG.infof("ROLE CHECK -> User: %s | DB Role: '%s' | Required Role: '%s'",
                                username, user.getRole(), role);

                        // Null Check
                        if (user.getRole() == null) {
                            LOG.warn("Access Denied: User has NO ROLE assigned in Database.");
                            return false;
                        }

                        // role control
                        return user.getRole().equalsIgnoreCase(role);
                    }

                    @Override
                    public boolean isSecure() {
                        return requestContext.getSecurityContext().isSecure();
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return "BASIC";
                    }
                });
            } else {
                // 401 failed
                LOG.warn("Login Failed: Wrong Password");
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        } else {
            // 401 not exist
            LOG.warn("Login Failed: User not found");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}