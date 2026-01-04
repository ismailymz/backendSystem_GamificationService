package de.thws.gamification.adapter.out.persistence.mapper;

import de.thws.gamification.adapter.out.persistence.entity.DriverProfileEntity;
import de.thws.gamification.domain.model.DriverProfile;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

public class DriverProfileMapper {
    @ApplicationScoped
    //To Entity
    public DriverProfileEntity toEntity(DriverProfile driverProfile){
        final var returnValue = new DriverProfileEntity();
        returnValue.setId(driverProfile.getId());
        returnValue.setUsername(driverProfile.getUsername());
        returnValue.setTotalPoints(driverProfile.getTotalPoints());
        return returnValue;
    }

    public List<DriverProfileEntity> toEntities(List<DriverProfile> driverProfiles){
        return driverProfiles.stream().map(this::toEntity).toList();
    }
    //To Domain

    public DriverProfile toDomain(DriverProfileEntity driverProfileEntity){
        return new DriverProfile(
                driverProfileEntity.getId(),
                driverProfileEntity.getUsername(),
                driverProfileEntity.getTotalPoints()
        );
    }

    public List<DriverProfile> toDomains(List<DriverProfileEntity> driverProfileEntities){
        return driverProfileEntities.stream().map(this::toDomain).toList();
    }

}
