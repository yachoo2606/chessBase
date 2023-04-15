package pl.tiwpr.chessbase.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "players")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Override
    public String toString() {
        return "{ " +
                "id="+ id.toString()+ ", " +
                "name= " + name + ", " +
                "surname= " + surname +
                "}";
    }
}
