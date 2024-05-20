package org.luxeloot.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.luxeloot.backend.models.Role;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private Long id;
    private String logo;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
}