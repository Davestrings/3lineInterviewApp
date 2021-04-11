package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/card-scheme/")
public class cardController {

    @Autowired
    private RestTemplate restTemplate;

    private static String url = "https://lookup.binlist.net/";

    @GetMapping("/verify/{cardNumber}")
    public ResponseEntity<?> verify(@PathVariable Long cardNumber, HttpServletRequest request){
        Map<String, Object> response = new HashMap<>();
        String key = request.getHeader("appKey");
        String auth = request.getHeader("authorization");
        String timestamp = request.getHeader("timestamp");

        String hash = "";
        String hashed = "";

        try{
            hash = generateHash(timestamp, key);

        }catch(NoSuchAlgorithmException ex){
            log.error(ex.getMessage());
        }
        if(StringUtils.hasText(auth) && auth.startsWith("3line")){
            hashed = auth.substring(6);
            if(!hashed.equals(hash)) {
                response.put("success", false);
                response.put("message", "invalid auth token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            try {
                Object object = restTemplate.getForObject(url + cardNumber, Object.class);
                response.put("success", true);
                response.put("payload", object);
            }catch(RestClientException ex){
                log.warn(ex.getMessage());
                response.put("success", false);
                response.put("message", ex.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }


            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            response.put("success", false);
            response.put("message", "invalid header parameters");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

    }
    public String generateHash(String timestamp, String appKey) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        String hash;
        String stringToHash = timestamp + appKey;
        digest.update(stringToHash.getBytes());
        byte[] bytes = digest.digest();

        StringBuilder builder = new StringBuilder();
        for(int i =0; i< bytes.length; i++){
            builder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        hash = builder.toString();
        return hash;
    }
    public String generateHash2(String timestamp, String appKey){
        String input = timestamp + appKey;
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


}
