package VTTPday19.practiceExam.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import VTTPday19.practiceExam.model.toDoList;
import VTTPday19.practiceExam.service.PracExamService;
import VTTPday19.practiceExam.utils.dateMethods;
import jakarta.json.JsonObject;

@Controller
public class PracExamController {
    @Autowired
    PracExamService pracSvc;

    @GetMapping("/listing")
    public String getList(@RequestParam(defaultValue="all") String choice, Model model) throws ParseException{

        if(choice.equals("all")){
            List<toDoList> allList = pracSvc.getAllObj();
            model.addAttribute("filtered", allList);
            return "listing";
        }

        List<toDoList> filteredList = pracSvc.getFilteredObj(choice);
        model.addAttribute("filtered", filteredList);
        return "listing";

    }

    @PostMapping("/add")
    public String addTask(@RequestParam MultiValueMap<String, String> form, Model model) throws ParseException{

        //create new tdl object
        toDoList tdl = new toDoList();
            tdl.setId(form.getFirst("id"));
            tdl.setName(form.getFirst("name"));
            tdl.setDescription(form.getFirst("description"));
            tdl.setDueDate(dateMethods.strToDate(form.getFirst("due_date")));
            tdl.setPriority(form.getFirst("priority_level"));
            tdl.setStatus(form.getFirst("status"));
            tdl.setCreatedAt(dateMethods.strToDate(form.getFirst("created_at")));
            tdl.setUpdatedAt(dateMethods.strToDate(form.getFirst("updated_at")));

        pracSvc.saveJson(tdl, toDoList.toJson(tdl)); //save to redis, to Json converts date type to epoch string

        model.addAttribute("tdl", tdl);
        return "add";
    }

}
