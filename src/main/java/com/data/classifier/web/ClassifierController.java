package com.data.classifier.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.data.classifier.model.Defaultdata;
import com.data.classifier.model.Highconfidential;
import com.data.classifier.service.DataclassifierService;
import com.data.classifier.utility.Utility;

@Controller
@RequestMapping("/classifier")
public class ClassifierController
{

    @Autowired private DataclassifierService dataclassifierService;

    @GetMapping("/ping")
    public @ResponseBody String ping()
    {
        return "Classifier is pinging -" + System.currentTimeMillis();
    }
    
    @GetMapping("/verifydata/{input}")
    public @ResponseBody String verifydata(@PathVariable("input") String input)
    {
        return dataclassifierService.verifyData(input);
    }

    @GetMapping("/{type}")
    public String index(@PathVariable("type") String type, Model model)
    {
        model.addAttribute("columnmaps", Utility.getDefaultColumns());
        List<Defaultdata> defaultDataList = Utility.getDefaultData();
        model.addAttribute("datasets", defaultDataList);
        if (type.equals("highconfidential") && defaultDataList.size() > 0)
        {
            model.addAttribute("message", "High Confidential data");
            model.addAttribute("columnmaps", Utility.getHighConfidentialColumns());
            model.addAttribute("datasets", Utility.getHighConfidentialData());
        }
        else if (type.equals("confidential") && defaultDataList.size() > 0)
        {
            model.addAttribute("message", "Confidential data");
            model.addAttribute("columnmaps", Utility.getConfidentialColumns());
            model.addAttribute("datasets", Utility.getConfidentialData());
        }
        else if(type.equals("encryptdata") && defaultDataList.size() > 0)
        {
            model.addAttribute("message", "High Confidential Encrypted Data");
            model.addAttribute("columnmaps", Utility.getHighConfidentialColumns());
            model.addAttribute("datasets", Utility.getHighConfidentialEncryptedData());
        }
        else if(defaultDataList.size() > 0)
        {
            model.addAttribute("message", "Default data");
        }
        model.addAttribute("type", type);
        return "upload";
    }

    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   Model model)
    {
        //Save the uploaded file to this folder
        String UPLOADED_FOLDER = "D://temp//";

        if (file.isEmpty())
        {
            model.addAttribute("message", "Please select a file to upload");
            return "upload";
        }

        try
        {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            String filePath = UPLOADED_FOLDER + file.getOriginalFilename();
            Path path = Paths.get(filePath);
            Files.write(path, bytes);
            Utility.readCsv(filePath);

            model.addAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
            model.addAttribute("columnmaps", Utility.getDefaultColumns());
            model.addAttribute("datasets", Utility.getDefaultData());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return "upload";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus()
    {
        return "uploadStatus";
    }

    @GetMapping("/highconfidentials")
    public @ResponseBody List<Highconfidential> highconfidentials()
    {
        return dataclassifierService.highconfidentials();
    }

    @GetMapping("/getDefaultData")
    public @ResponseBody List<Defaultdata> getDefaultData()
    {
        return Utility.getDefaultData();
    }
}
