package pl.tiwpr.chessbase.model.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserView {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}
