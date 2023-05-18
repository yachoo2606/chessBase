package pl.tiwpr.chessbase.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "titles")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Title {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codeName;

    @Column(unique = true,nullable = false)
    private String name;

    @Column(unique = true,nullable = false, length = 10000)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
