package VTTPday19.crypto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import VTTPday19.crypto.service.NewsService;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
public class NewsRESTController {

    @Autowired
    private NewsService newsSvc;

    @GetMapping(path="/news/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> retrieveArticle(
        @PathVariable(name="id", required=true) String id
    ){

        try{
            JsonObject jObj = newsSvc.getArticle(id);
            return ResponseEntity.ok(jObj.toString());
        } catch(NullPointerException e){
            JsonObject errorObj = Json.createObjectBuilder()
            .add("error", "Cannot find news article " + id)
            .build();

            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)// can be any number
                .body(errorObj.toString());
        }

    }


}
