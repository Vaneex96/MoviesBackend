package com.ivanhorlov.moviesbackend.controllers;

import com.ivanhorlov.moviesbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class OtherController {

    private final UserService userService;


    @GetMapping("/unsecured")
    public String unsecuredData(){
        return "Unsecured";
    }

    @GetMapping("/secured")
    public String securedData(){
        return "Secured";
    }


    @GetMapping("/img/{img}")
    public ResponseEntity<byte[]> getPicture(@PathVariable String img) throws IOException {
        BufferedImage bImage = ImageIO.read(new File("C:\\Users\\igorl\\OneDrive\\Рабочий стол\\pictures\\" + img));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos );
        byte [] data = bos.toByteArray();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setCacheControl(CacheControl.noCache().getHeaderValue());
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<byte[]>(data, httpHeaders, HttpStatus.OK);

    }

}
