package com.pep.springjpa.security;

import java.util.Base64.Decoder;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.io.Decoders;

@Service
public class JwtService {
    
    @Value("${jwt.secret}")   // injects from app.propeties
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;   // in millliseconds

    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return new SerectKeySpec(keyBytes, "HmacSHA256");
    }
}
