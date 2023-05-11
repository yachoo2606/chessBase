package pl.tiwpr.chessbase.model.views;

import lombok.*;
import pl.tiwpr.chessbase.model.Address;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubView {
    String name;
    String codeName;
    Address address;

    @Override
    public String toString(){
        return "{" +
                "name="+name+", "+
                "codeName="+codeName+", "+
                address.toString()+
                "}";
    }
}
