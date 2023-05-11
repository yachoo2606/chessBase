package pl.tiwpr.chessbase.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="clubs")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codeName;

    @Column(nullable = false)
    private String name;

    private Address address;

    @Override
    public String toString(){
        return "{" +
                "id=\""+id+"\", "+
                "name=\""+name+"\", "+
                "codeName=\""+codeName+"\", "+
                "address: \""+address.toString()+"\""+
                "}";
    }

}
