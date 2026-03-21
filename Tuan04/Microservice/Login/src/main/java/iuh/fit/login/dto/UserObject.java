package iuh.fit.login.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserObject {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String password;
    private String role;
}