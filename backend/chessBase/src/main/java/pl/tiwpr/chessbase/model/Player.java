package pl.tiwpr.chessbase.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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

    @Column(nullable = false,columnDefinition = "DATE")
    private LocalDate birthDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column()
    private Integer ELO = 1000;

    @Version
    @Column(name="OPTCLOCK", nullable = false, columnDefinition = "integer DEFAULT 1")
    private Long version;

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