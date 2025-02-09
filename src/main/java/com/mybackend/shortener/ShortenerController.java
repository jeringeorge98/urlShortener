package com.mybackend.shortener;


import com.mybackend.shortener.model.ApiRequest;
import com.mybackend.shortener.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class ShortenerController {

    private final ShortyService service ;

    public ShortenerController(ShortyService service) {
        this.service = service;
    }

    @PostMapping(
            value = "/shorten"
    )
    public ApiResponse shorten(@RequestBody ApiRequest request) {
      String hash = service.shorten(request.getUrl());
    return new ApiResponse(hash);
}

@GetMapping("/{hash}")
    public ResponseEntity<HttpStatus> resolve(@PathVariable String hash){
        String target = service.resolve(hash);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).location(URI.create(target)).header(HttpHeaders.CONNECTION,"close").build();
}
   @DeleteMapping("/{hash}")
    public ResponseEntity<HttpStatus> delete(@PathVariable String hash){
   try{
        if(service.remove(hash)){
            return ResponseEntity.status(HttpStatus.OK).build();
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
   } catch (HashUnknownException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
       }
}
}
