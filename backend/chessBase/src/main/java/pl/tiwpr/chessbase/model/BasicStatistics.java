package pl.tiwpr.chessbase.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasicStatistics{
    public Integer white;
    public Integer black;
    public Integer draw;
    public Integer games;
}
