package com.capital.bmo.reconciliationDemo.controller;

import com.capital.bmo.reconciliationDemo.impl.CsvDataSourceService;
import com.capital.bmo.reconciliationDemo.impl.ExcelDataSourceService;
import com.capital.bmo.reconciliationDemo.model.ReconciliationRecord;
import com.capital.bmo.reconciliationDemo.service.ReconciliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/reconcile")
public class ReconciliationController {

    @Autowired
    private ExcelDataSourceService excelService;

    @Autowired
    private CsvDataSourceService csvService;

    @Autowired
    private ReconciliationService reconciliationService;

    @GetMapping
    public String showForm() {
        return "reconcile";
    }

    @PostMapping("/upload")
    public String uploadAndReconcile(
            @RequestParam("file1") MultipartFile file1,
            @RequestParam("file2") MultipartFile file2,
            @RequestParam("key") String keyField,
            Model model) {

        try {
            saveFile(file1, "file1.xlsx");
            saveFile(file2, "file2.xlsx");
            model.addAttribute("file1", "file1.xlsx");
            model.addAttribute("file2", "file2.xlsx");
            // Simulate reading data from files
            List<ReconciliationRecord> data1 = excelService.readData(file1);
            List<ReconciliationRecord> data2 = excelService.readData(file2);

            List<ReconciliationRecord> missingInFirst = reconciliationService.findMissingInFirst(data1, data2, keyField);
            List<ReconciliationRecord> missingInSecond = reconciliationService.findMissingInSecond(data1, data2, keyField);
            List<ReconciliationRecord> matches = reconciliationService.findMatches(data1, data2, keyField);

            model.addAttribute("missingInFirstCount", missingInFirst.size()); // Records missing in File 1
            model.addAttribute("missingInSecondCount", missingInSecond.size()); // Records missing in File 2
            model.addAttribute("matchesCount", matches.size()); // Matched records

            model.addAttribute("data1", data1); // Data from File 1
            model.addAttribute("data2", data2); // Data from File 2
            model.addAttribute("missingInFirst", missingInFirst); // Records missing in File 1
            model.addAttribute("missingInSecond", missingInSecond); // Records missing in File 2
            model.addAttribute("matches", matches); // Matched records

           /* model.addAttribute("missingInFirst", missingInFirst.size());
            model.addAttribute("missingInSecond", missingInSecond.size());
            model.addAttribute("matches", matches.size());*/
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
        }

        return "result";
    }
    public void saveFile(MultipartFile file, String fileName) throws IOException {
        Path uploadPath = getUploadPath().resolve(fileName);

        // If the file already exists, delete it
        if (Files.exists(uploadPath)) {
            Files.delete(uploadPath);
            System.out.println("Existing file deleted: " + uploadPath);
        }

        // Save the new file (upload or replace)
        file.transferTo(uploadPath);
        System.out.println("File uploaded successfully: " + uploadPath);
    }
    public Path getUploadPath() throws IOException {
        // Get the current working directory (this is where your app is run from)
        String currentDir = System.getProperty("user.dir");

        // Define the upload directory path relative to the current working directory
        Path path = Paths.get(currentDir, "uploads");

        // Ensure the directory exists, create it if necessary
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        return path;
    }
}
