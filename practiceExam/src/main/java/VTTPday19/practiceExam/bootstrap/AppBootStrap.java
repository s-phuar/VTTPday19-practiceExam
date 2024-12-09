package VTTPday19.practiceExam.bootstrap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.io.Reader;
import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import VTTPday19.practiceExam.model.toDoList;
import VTTPday19.practiceExam.service.PracExamService;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Component
public class AppBootStrap implements CommandLineRunner {
    
    @Autowired
    private PracExamService pracSvc;

    @Override
    public void run(String... args) throws IOException, ParseException{


        Path listPath = Paths.get("todos.json");
        // File listFile = listPath.toFile();
        String listString = new String(Files.readAllBytes(listPath)); //read the whole json as a single string
        try {
            Reader reader = new StringReader(listString);
            JsonReader jsonReader = Json.createReader(reader);
            // System.out.println  (jsonReader);
            JsonArray toDoListArr = jsonReader.readArray();

            for(int i = 0; i < toDoListArr.size(); i++){
                JsonObject tempObj = toDoListArr.getJsonObject(i);
                //transform each json object into a toDoList object, with Data param as normal date
                toDoList tdl = toDoList.toListObj(tempObj);
                //convert toDoList object into json obj with epochmiliseconds string
                JsonObject jsonObj = toDoList.toJson(tdl);
                //store marshalled object to jsonStr in redis
                pracSvc.saveJson(tdl, jsonObj);
            }

        } catch (JsonException e) {
            e.printStackTrace();
            System.out.println("Not a json file");
        }






    }

}
