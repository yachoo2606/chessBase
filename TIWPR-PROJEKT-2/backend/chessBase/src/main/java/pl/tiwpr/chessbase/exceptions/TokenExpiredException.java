package pl.tiwpr.chessbase.exceptions;

public class TokenExpiredException extends RuntimeException{

    public TokenExpiredException(String message){
        super(message);
    }

}
