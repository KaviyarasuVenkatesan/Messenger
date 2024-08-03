package com.messenger_application.messenger.utils;

import com.messenger_application.messenger.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

@Component
public class JwtUtils {

    private static final String  SECRET_KEY = "67rY67oQMsPXrS8GiVuC9EOVh6ssphRsAB6Wsrva5B1vueL5VqLJY59G3VZe8/0OO1e5a0zmGHFMkO374iDUQ34qy8aSf6fduJcG1m6hQcQ/364Uta7Q6ilzbdKpBRouJEuqKp36G8zs6SUuJmOiuFz4tWrJWaWFoVVeGlceuDcGgUlGS79dYzmRR73Qqo2IhDc1DB0g2J6d7POF89HOql+CX45DRhfmhQFhcDecCTztXTucngSxPD63V0LUkDSMO5foHpTAE3yaB+09xrBaEgAooORLWFpkRe/oNhFcTzUBZNKhKpOWaGn8ljcQxnLcxeBTvzfiZO6xwbhYwA+LZf+MoUhHe0wbewKwbeymsrE=";

    public String generateToken (User user){
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken (HashMap<String, Object> extraClaims, User user ){

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUserName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    private Key getSigninKey (){
        byte[] byteKey = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(byteKey);
    }

}
