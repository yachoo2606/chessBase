package pl.tiwpr.chessbase.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
    private String lastName;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birtDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "title_id")
    private Title title;

    @ManyToOne
    @JoinColumn(name = "club_id")
    @JsonBackReference
    private Club club;

    @Override
    public String toString() {
        return "{ " +
                "id="+ id.toString()+ ", " +
                "name= " + name + ", " +
                "surname= " + lastName +
                "}";
    }
}