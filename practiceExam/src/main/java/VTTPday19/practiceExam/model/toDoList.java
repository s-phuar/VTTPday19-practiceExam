package VTTPday19.practiceExam.model;

import java.io.StringReader;
import java.text.ParseException;
import java.util.Date;

import VTTPday19.practiceExam.utils.dateMethods;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class toDoList {

    private String id;
    private String name;
    private String description;
    private Date dueDate;
    private String priority;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public Date getDueDate() {return dueDate;}
    public void setDueDate(Date dueDate) {this.dueDate = dueDate;}
    public String getPriority() {return priority;}
    public void setPriority(String priority) {this.priority = priority;}
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    public Date getCreatedAt() {return createdAt;}
    public void setCreatedAt(Date createdAt) {this.createdAt = createdAt;}
    public Date getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(Date updatedAt) {this.updatedAt = updatedAt;}

    @Override
    public String toString() {
        return "toDoList [id=" + id + ", name=" + name + ", description=" + description + ", dueDate=" + dueDate
                + ", priority=" + priority + ", status=" + status + ", createdAt=" + createdAt + ", updatedAt="
                + updatedAt + "]";
    }


    //convert base json object into toDoList object (for store prep)
    public static toDoList toListObj(JsonObject json) throws ParseException{
        toDoList tdl = new toDoList();

        tdl.setId(json.getString("id"));
        tdl.setName(json.getString("name"));
        tdl.setDescription(json.getString("description"));
        tdl.setDueDate(dateMethods.strToDate(json.getString("due_date")));
        tdl.setPriority(json.getString("priority_level"));
        tdl.setStatus(json.getString("status"));
        tdl.setCreatedAt(dateMethods.strToDate(json.getString("created_at")));
        tdl.setUpdatedAt(dateMethods.strToDate(json.getString("updated_at")));

        return tdl;
    }

    public static toDoList toListObjFromRedis(JsonObject json) throws ParseException{
        toDoList tdl = new toDoList();

        tdl.setId(json.getString("id"));
        tdl.setName(json.getString("name"));
        tdl.setDescription(json.getString("description"));
        tdl.setDueDate(dateMethods.epochStrToDate(json.getString("due_date")));
        tdl.setPriority(json.getString("priority_level"));
        tdl.setStatus(json.getString("status"));
        tdl.setCreatedAt(dateMethods.epochStrToDate(json.getString("created_at")));
        tdl.setUpdatedAt(dateMethods.epochStrToDate(json.getString("updated_at")));

        return tdl;
    }

    //convert toDoList object into custom json object.toString() (for redis store)
    public static JsonObject toJson(toDoList tdl){
        JsonObject jsonObj = Json.createObjectBuilder()
            .add("id", tdl.getId())
            .add("name", tdl.getName())
            .add("description", tdl.getDescription())
            .add("due_date", dateMethods.dateToEpochStr(tdl.getDueDate())) //Date object to epochmili String
            .add("priority_level", tdl.getPriority())
            .add("status", tdl.getStatus())
            .add("created_at", dateMethods.dateToEpochStr(tdl.getCreatedAt())) //Date object to epochmili String
            .add("updated_at", dateMethods.dateToEpochStr(tdl.getUpdatedAt())) //Date object to epochmili String
            .build();
        return jsonObj;
    }



    //convert custom json string back into custom json object
    public static JsonObject toJson(String jsonStr) {
        JsonReader reader = Json.createReader(new StringReader(jsonStr));
        JsonObject jsonObj = reader.readObject();
        return jsonObj;   
    }



}
