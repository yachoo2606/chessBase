package pl.tiwpr.chessbase.model.views;

import lombok.*;
import pl.tiwpr.chessbase.model.Gender;
import pl.tiwpr.chessbase.model.Title;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerView {
    Long id;
    String name;
    String lastName;
    LocalDate birthDate;
    Gender gender;
    Title title;
    ClubView club;
    Integer ELO;

    @Override
    public String toString() {
        return "{ " +
                "id="+ id.toString()+ ", " +
                "name= " + name +
                "lastName" +lastName+
                "birthDate=" +birthDate+
                "gender=" +gender+
                "title="+ title+
                "club=" +club+
                "ELO="+ ELO+
                "}";
    }
}
