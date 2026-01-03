package de.thws.gamification.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Achievement {

    private final UUID  id;
    private final String code;
    private final String name;
    private String description;

    public Achievement(UUID id, String code, String name, String description){
        if(id==null){
            throw new IllegalArgumentException("id must not be null");
        }
        if(code==null||code.isBlank()){
            throw new IllegalArgumentException("code must not be blank");
        }
        if(name==null||name.isBlank()){
            throw new IllegalArgumentException("name must not be blank");
        }
        this.id=id;
        this.code=code;
        this.name=name;
        this.description=description;
    }


    public static Achievement of(String code, String name, String description){
        return new Achievement(UUID.randomUUID(),code,name,description);
    }
    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Achievement that)) return false;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "Achievement{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
