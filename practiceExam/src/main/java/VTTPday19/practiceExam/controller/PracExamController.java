package VTTPday19.practiceExam.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import VTTPday19.practiceExam.model.User;
import VTTPday19.practiceExam.model.toDoList;
import VTTPday19.practiceExam.service.PracExamService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PracExamController {
    @Autowired
    PracExamService pracSvc;

    @GetMapping("/")
    public String landingPage(HttpSession sess, Model model){
        sess.invalidate();
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/loginprocess")
    public String firstMethod(@ModelAttribute("user") User user, HttpSession sess) {
        sess.setAttribute("user", user);

        return "redirect:/listing";
    }

    @GetMapping("/listing")
    public String getList(@RequestParam(defaultValue="all") String choice,
    HttpSession sess, Model model) throws ParseException{


        if(sess==null || ((User) sess.getAttribute("user"))==null){
            return "refused";
        }
        if(((User) sess.getAttribute("user")).getAge() < 10){
            return "underaged";
        }

        if(choice.equals("all")){
            List<toDoList> allList = pracSvc.getAllObj();
            model.addAttribute("filtered", allList); 
            return "listing";
        }

        List<toDoList> filteredList = pracSvc.getFilteredObj(choice);
        model.addAttribute("filtered", filteredList);
        return "listing";


    }

    @GetMapping("/newTask")
    public String newTask(Model model){
        toDoList tdl = new toDoList();
        model.addAttribute("tdl",tdl);
        return "add";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(
        @PathVariable(name="id", required=true) String id){

        pracSvc.deleteRecord(id);
        return "redirect:/listing";
    }

    @PostMapping("/update/{id}")
    public String updateTask(
        @PathVariable(name="id", required=true) String id, Model model) throws ParseException{

        toDoList tdl = pracSvc.getRecord(id); //holds full tdl object inlucing id, sends it to add html

        model.addAttribute("tdl",tdl);
        return "add";
    }


    @PostMapping("/add")
    public String addTask(@Valid @ModelAttribute ("tdl") toDoList tdl, BindingResult binding, Model model) throws ParseException{

        //we will hit the controller first before form page
            // tdl.setId(form.getFirst("id")); //set in service
            // tdl.setName(form.getFirst("name"));
            // tdl.setDescription(form.getFirst("description"));
            // tdl.setDueDate(dateMethods.strToDateNewTask(form.getFirst("due_date"))); //watch for method (create new one)
            // tdl.setPriority(form.getFirst("priority_level"));
            // tdl.setStatus(form.getFirst("status"));
            // tdl.setCreatedAt(dateMethods.strToDateNewTask(form.getFirst("created_at")));
            // tdl.setUpdatedAt(dateMethods.strToDateNewTask(form.getFirst("updated_at")));

        if(binding.hasErrors()){
            System.out.println("validation error detected");
            model.addAttribute("tdl", tdl); 
            model.addAttribute("taskID", tdl.getId());
            return"add";
        }

        if("lmaolmaolmao".equals(tdl.getName().toLowerCase())){ //any condition
            //custom error for 1 object param
            FieldError err = new FieldError("tdl", "name", "you cannot use lmaolmaolmao");
            binding.addError(err);

            //global error for multiple fields
                // ObjectError objErr = new ObjectError("globalError", "random error X");
                // binding.addError(objErr);

            System.out.println("custom task name error detected");
            model.addAttribute("tdl", tdl); 
            model.addAttribute("taskID", tdl.getId());
            return"add";
        }



        if(tdl.getId()==null || tdl.getId().equals("")){
            System.out.println(tdl);

            toDoList completeTdl = pracSvc.addUUID(tdl);
            pracSvc.saveJson(completeTdl, toDoList.toJson(completeTdl)); //save to redis, to Json converts date type to epoch string
            model.addAttribute("taskID", completeTdl.getId()); //triggers when saving new task
            System.out.println("NOT overriding record");
            return "add";
        }
        System.out.println(tdl);

        pracSvc.saveJson(tdl, toDoList.toJson(tdl)); //save to redis, to Json converts date type to epoch string
        model.addAttribute("taskID", tdl.getId()); //triggers when saving updates
        System.out.println("i AM overriding record");

        return "add";

    }




    


}
