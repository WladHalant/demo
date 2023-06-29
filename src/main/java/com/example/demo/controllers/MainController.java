package com.example.demo.controllers;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.zip.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            Map<String, Object> model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {


        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();


            PDDocument document = PDDocument.load(file.getBytes());
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page)
            {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 400, ImageType.RGB);

                // suffix in filename will be used as the file format
                ImageIOUtil.writeImage(bim, uploadPath + "/" + resultFilename + "-" + (page+1) + ".png", 300);
            }
            var fileToZip = new File(uploadPath);
            try (
                    var fos = new FileOutputStream(uploadPath + ".zip");
                    var zipOut = new ZipOutputStream(fos)
            ) {
                zipFile(fileToZip, fileToZip.getName(), zipOut);
            }
            document.close();

        }

        return "index";
    }

    private void zipFile(File fileToZip, String name, ZipOutputStream zipOut) {
    }


    @GetMapping("/listFiles")
    public String listFiles(Model model){

        //Creating a File object for directory
        File directoryPath = new File(uploadPath);
        //List of all files and directories
        String contents[] = directoryPath.list();
        System.out.println("List of files and directories in the specified directory:");
        for(int i=0; i<contents.length; i++) {
            System.out.println(contents[i]);
        }

        model.addAttribute("file", contents[0]);
        return "listFiles";
    }
}
