package pl.tiwpr.chessbase.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String country;
    private String city;
    private String street;
    private String number;
    private String postCode;
    private String longitude;
    private String latitude;

    @Override
    public String toString(){
        return "{" +
                "country= \""+country+"\", "+
                "city=\""+city+"\", "+
                "street=\""+street+"\", "+
                "number=\""+number+"\", "+
                "postCode=\""+postCode+"\", "+
                "longitude=\""+longitude+"\", "+
                "latitude=\""+latitude+"\", "+
                "}";
    }
}
