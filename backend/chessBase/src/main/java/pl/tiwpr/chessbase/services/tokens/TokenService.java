package pl.tiwpr.chessbase.services.tokens;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class TokenService {
    private final Set<String> tokenSet = new HashSet<>();

    public String generateToken(){
        String token = UUID.randomUUID().toString();
        tokenSet.add(token);
        return token;
    }

    public boolean isTokenValid(String token){
        return tokenSet.contains(token);
    }

    public void invalidateToken(String token){
        tokenSet.remove(token);
    }
}
