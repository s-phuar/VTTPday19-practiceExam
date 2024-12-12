package VTTPday19.crypto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import VTTPday19.crypto.model.Articles;
import VTTPday19.crypto.service.NewsService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class NewsController {
    
    @Autowired
    private NewsService newsSvc;

    @GetMapping("/")
    public String getArticles(HttpSession sess, Model model){
        List<Articles> artList = newsSvc.getArticles();

        sess.setAttribute("allArticles", artList);
        
        model.addAttribute("artList", artList);
        return "crypto-news";
    }


    @PostMapping("/articles")
    public String saveArticles(
        // @RequestParam MultiValueMap<String, String> saveArticleIds, 
        @RequestParam (required=true) List<String> saveArticleIds, 
        HttpSession sess,
        Model model){
            
        // List<String> filteredList= saveArticlesIds.get("saveArticleIds");
        List<String> conditionList = saveArticleIds;
        List<Articles> artList = (List<Articles>) sess.getAttribute("allArticles"); //all articles

        
        List<Articles> filtered = artList.stream()//list of filtered article objects
            .filter(article -> conditionList.contains(article.getId())) //contains iterates through a list of condition strings
            .toList();

    
        newsSvc.saveArticles(filtered);


            
        return "redirect:/";
        


    }

}
