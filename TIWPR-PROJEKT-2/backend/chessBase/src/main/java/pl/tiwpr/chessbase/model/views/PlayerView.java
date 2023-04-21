package pl.tiwpr.chessbase.model.views;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerView {
    long id;
    String name;
}
