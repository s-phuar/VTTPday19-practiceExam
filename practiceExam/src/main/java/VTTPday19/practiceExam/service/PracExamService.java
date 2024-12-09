package VTTPday19.practiceExam.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import VTTPday19.practiceExam.model.toDoList;
import VTTPday19.practiceExam.repository.PracExamRepository;
import VTTPday19.practiceExam.utils.dateMethods;
import jakarta.json.JsonObject;

@Service
public class PracExamService {

    @Autowired
    PracExamRepository pracRepo;

    public void saveJson(toDoList tdl, JsonObject json){
        pracRepo.saveJsonStr(tdl.getId(), json.toString());
    }


    //collection.stream()           // Create a Stream from the collection
        // .filter(e -> e.someCondition())   // Intermediate operation
        // .map(e -> e.transformedValue())   // Intermediate operation
        // .forEach(e -> System.out.println(e)); // Terminal operation
    public List<String> getJsonStr(){
        List<Object> listObj = pracRepo.getJsonStr();
        List<String> listStr = listObj.stream() //convert List<Object> to stream
                        .filter(streamElement -> streamElement instanceof String) //only allows StreamElements of type String to pass through the stream
                        .map(streamElement -> (String) streamElement) //transforms each list element by casting to String
                        .collect(Collectors.toList()); //collects stream results into a list
        return listStr;
    }


    public List<toDoList> getFilteredObj(String desiredStatus) throws ParseException{
        List<String> listStr = getJsonStr(); //get list of JsonString from redis

        List<toDoList> listJ = new ArrayList<>(); //to hold list off model objects with correct date
        for(int i = 0; i < listStr.size(); i++){
            JsonObject jsonObj = toDoList.toJson(listStr.get(i));//json Str to Obj
            //convert epochmilli to Date type before putting to object
            toDoList tdl = toDoList.toListObjFromRedis(jsonObj);
            if(tdl.getStatus().equals(desiredStatus)){
                listJ.add(tdl);
            }            
        }
        return listJ;
    }

    public List<toDoList> getAllObj() throws ParseException{
        List<String> listStr = getJsonStr(); //get list of JsonString from redis

        List<toDoList> listJ = new ArrayList<>(); //to hold list off model objects with correct date
        for(int i = 0; i < listStr.size(); i++){
            JsonObject jsonObj = toDoList.toJson(listStr.get(i));//json Str to Obj
            //convert epochmilli to Date type before putting to object
            toDoList tdl = toDoList.toListObjFromRedis(jsonObj);
            listJ.add(tdl);            
        }
        return listJ;
    }



    // public List<toDoList> getFilteredObj(String desiredStatus) throws ParseException{
    //     List<String> listStr = getJsonStr();
    //     List<toDoList> listJ = listStr.stream()
    //                     .map(streamElement -> toDoList.toJson(streamElement)) //jsonStr to jsonObj
    //                     .map(streamElement -> {
    //                         try {
    //                             dateMethods.epochStrToDate(streamElement.getString("due_date"));
    //                             dateMethods.epochStrToDate(streamElement.getString("created_at"));     
    //                             dateMethods.epochStrToDate(streamElement.getString("updated_at"));     
    //                             return toDoList.toListObj(streamElement);
    //                         } catch (ParseException e) {
    //                             System.out.println("Parsing non-json object");
    //                         }
    //                         return null;
    //                     }) //jsonObj to toDoList obj, with proper date format
    //                     .filter(streamElement -> streamElement.getStatus().equals(desiredStatus))
    //                     // .filter(streamElement -> streamElement.containsKey(status) &&
    //                     //                         streamElement.getString(status).equals(desiredStatus)) //check whether each json obj status is "completed/pending etc."
    //                     .collect(Collectors.toList());
    //     return listJ;
    // }

    // public List<toDoList> getFilteredObj() throws ParseException{
    //     List<String> listStr = getJsonStr();
    //     List<toDoList> listJ = listStr.stream()
    //                     .map(streamElement -> toDoList.toJson(streamElement)) //jsonStr to jsonObj
    //                     .map(streamElement -> {
    //                         try {
    //                             return toDoList.toListObj(streamElement); //jsonObj to toDoList obj
    //                         } catch (ParseException e) {
    //                             System.out.println("Parsing non-json object");
    //                         }
    //                         return null;
    //                     })
    //                     // .filter(streamElement -> streamElement.containsKey(status) &&
    //                     //                         streamElement.getString(status).equals(desiredStatus)) //check whether each json obj status is "completed/pending etc."
    //                     .collect(Collectors.toList());
    //     return listJ;
    // }


}
