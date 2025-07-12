package com.pep.taskmanagement.Dtos;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private Set<String> roles;     
    // Or use RoleDto but not Role beacuse when try to  serialize Role its conatines 
    // User "roles": [ { "id": 1, "name": "ROLE_TEAM_MEMBER", "users": [...] }  
    // agian user have Role so infinte Recurtion so for safe site use jsonIgnore

}
