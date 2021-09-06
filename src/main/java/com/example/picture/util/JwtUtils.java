package com.example.picture.util;

import com.example.picture.dto.JwtDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;


public class JwtUtils {


    //Sample method to construct a JWT

    /**
     * @param id        id
     * @param issuer    发行人
     * @param subject   内容JSON串
     * @param ttlMillis 有效时间
     * @return JWT
     */
    public static String getJWT(String id, String issuer, String subject, long ttlMillis) {
        return createJWT(id, issuer, subject, ttlMillis);
    }

    public static JwtDto getData(String jwt) {
        return parseJWT(jwt);
    }

    private static String createJWT(String id, String issuer, String subject, long ttlMillis) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Helper.myKey());

        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                .setIssuer(issuer)
                .setSubject(subject)
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    //Sample method to validate and read the JWT
    private static JwtDto parseJWT(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(Helper.myKey()))
                .parseClaimsJws(jwt).getBody();

        JwtDto jwtDto = new JwtDto();
        jwtDto.setId(claims.getId());
        jwtDto.setIssuer(claims.getIssuer());
        jwtDto.setSubject(claims.getSubject());

        return jwtDto;
    }
}