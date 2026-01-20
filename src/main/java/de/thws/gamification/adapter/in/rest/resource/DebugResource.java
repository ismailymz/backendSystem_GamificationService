package de.thws.gamification.adapter.in.rest.resource;

import de.thws.gamification.adapter.out.persistence.entity.DriverProfileEntity;
// @PermitAll importunu silebilirsin, çünkü kontrol koymadığımız için zaten public oldu.
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/debug")
public class DebugResource {

    @Inject
    EntityManager em;

    @GET
    // @PermitAll <-- i think doesnt need still allow all the users.
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> dumpUsers() {
        // i wanted to see if system and seeder works correctly
        //we can delete later
        List<DriverProfileEntity> users = em.createQuery("SELECT d FROM DriverProfileEntity d", DriverProfileEntity.class).getResultList();

        return users.stream()
                .map(u -> "User: " + u.getUsername() + " | Pass: " + u.getPassword() + " | Role: " + u.getRole())
                .collect(Collectors.toList());
    }
}