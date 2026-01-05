package de.thws.gamification.adapter.out.persistence;

import de.thws.gamification.adapter.out.persistence.mapper.DriverProfileMapper;
import de.thws.gamification.application.ports.out.DriverProfileRepository;
import de.thws.gamification.domain.model.DriverProfile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class DriverProfileRepositoryJpa implements DriverProfileRepository {
@Inject
EntityManager entityManager;
@Inject
DriverProfileMapper mapper;

    @Override
    public Optional<DriverProfile> findById(UUID id){

    }

    @Override
    public List<DriverProfile> findAllOrderByTotalPointsDesc(){

    }

    @Override
    public DriverProfile save(DriverProfile driverProfile){

    }
}
