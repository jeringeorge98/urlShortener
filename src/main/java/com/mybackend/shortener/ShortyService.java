package com.mybackend.shortener;


import lombok.Getter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class ShortyService {
    private final RedisTemplate<String, String> redis;
    private final MessageDigest digest = MessageDigest.getInstance("SHA-256") ;

    public ShortyService(RedisTemplate<String, String> redis) throws NoSuchAlgorithmException {
        this.redis = redis;
    }

    public String shorten(String url){
        String hash = hash(url,6);
        redis.opsForValue().set(hash,url);
        return hash;
    }

    private String hash(String url , Integer length  ){
        byte [] hashBytes = digest.digest(url.getBytes(StandardCharsets.UTF_8));
        String hash = String.format("%32x" , new BigInteger(1,hashBytes));
        return hash.substring(0,Math.min(length,hash.length())) ;
    }

    public String resolve(String hash){
        if(hash == null || hash.isEmpty()){
            throw new HashUnknownException(hash);
        }
        return redis.opsForValue().get(hash);
    }

    public Boolean remove(String hash){
        String url = redis.opsForValue().get(hash);
        if( url == null || url.isEmpty()){
            throw new HashUnknownException(hash);
        }
         return redis.delete(hash);
    }

}

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
class HashUnknownException extends RuntimeException {
    // Getter for the hash
    private final String hash;

    public HashUnknownException(String hash) {
        super("Unknown hash: " + hash); // Add descriptive message to the exception
        this.hash = hash;
    }

}
