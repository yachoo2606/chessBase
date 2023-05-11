package pl.tiwpr.chessbase.model.views;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerView {
    Long id;
    String name;


    @Override
    public String toString() {
        return "{ " +
                "id="+ id.toString()+ ", " +
                "name= " + name +
                "}";
    }
}
