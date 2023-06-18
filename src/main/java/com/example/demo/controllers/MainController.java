package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Value;


import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
@Controller
public class MainController {
    @GetMapping("/")
    public String helloPage(Model model){
        model.addAttribute("name", "username");
        return "index";

    }
    @Value("${upload.path}")
    private String uploadPath;



    //http://localhost:8080/hellouser?name=Alex
    @GetMapping("/hellouser")
    public String helloPage(Model model, @RequestParam String name){
        System.out.println(name);
        model.addAttribute("name", name);
        return "index";
    }
    @PostMapping("/")
    public String add(
            @RequestParam String text,
            @RequestParam String tag, Map<String, Object> model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {


        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));


        }

        return "index";
    }

}
