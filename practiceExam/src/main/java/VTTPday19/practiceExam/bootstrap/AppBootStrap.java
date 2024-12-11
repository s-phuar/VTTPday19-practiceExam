package VTTPday19.practiceExam.bootstrap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.io.Reader;
import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import VTTPday19.practiceExam.model.Customer;
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


        //reading csv
        String file = "customer-100.csv";

        public List<Customer> withStream() throws FileNotFoundException, IOException{
            try(FileReader fr = new FileReader(file)){
                BufferedReader br = new BufferedReader(fr);
                
                //access the stream
                return br.lines() //converts each line from br into a stream, creating a lazy iterator
                    .skip(1)                //throw headers, file lines [0]
                    .limit(10) //limit to 10 lines
                    .map(line -> line.split(",")) //string -> string[]
                    .filter(fields -> "hungary".equals(fields[6].trim().toLowerCase())) //everything thats hungary gets through
                    .map(fields ->{
                        Customer customer = new Customer();
                        customer.setCustomerId(fields[1]);
                        customer.setFirstName(fields[2]);
                        customer.setLastName(fields[3]);
                        customer.setCompany(fields[4]);
                        customer.setCity(fields[5]);
                        customer.setCountry(fields[6]);
                        return customer;
                    }) //string -> customer object
                    .toList();
            }
        }


        public List<Customer> withLoop(String file) throws FileNotFoundException, IOException{
            //list to hold customer objects
            List<Customer> customers = new LinkedList<>();

            try (FileReader fr = new FileReader(file)){
                BufferedReader br = new BufferedReader(fr);
                //discard the headers
                br.readLine();
                String line;
                while(null!= (line = br.readLine())){
                    String[] fields = line.split(",");

                    //filter and store only country=iceland
                    if(!"hungary".equals(fields[6].trim().toLowerCase())){
                        continue;
                    }
                    Customer customer = new Customer();
                    customer.setCustomerId(fields[1]);
                    customer.setFirstName(fields[2]);
                    customer.setLastName(fields[3]);
                    customer.setCompany(fields[4]);
                    customer.setCity(fields[5]);
                    customer.setCountry(fields[6]);

                    customers.add(customer);
                }
            }
            return customers;
        }





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
