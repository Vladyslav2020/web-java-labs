package com.kpi.lab2.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User implements Entity {
    private Long id;
    private String name;
    private String password;
    private Boolean isAdmin;

    @Override
    public Long getId() {
        return id;
    }
}
