package de.thws.gamification.adapter.out.persistence.mapper;

import de.thws.gamification.adapter.out.persistence.entity.DriverProfileEntity;
import de.thws.gamification.domain.model.DriverProfile;

public class DriverProfileMapper {

    //To Entity
    public DriverProfileEntity toEntity(DriverProfile driverProfile){
        final var returnValue = new DriverProfileEntity();
        returnValue.setId(driverProfile.getId());
        returnValue.setUsername(driverProfile.getUsername());
        returnValue.setTotalPoints(driverProfile.getTotalPoints());
        return returnValue;
    }

}
