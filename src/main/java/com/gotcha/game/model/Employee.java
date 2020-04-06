package com.gotcha.game.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Employee extends User {
    @NotNull
    @Getter
    @Setter
    private String address;
    @NotNull @Getter @Setter
    private String phoneNumber;

    @NotNull @Getter @Setter
    private String name;
}
