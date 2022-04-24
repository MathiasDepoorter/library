package fr.limayrac.library.controller;

import fr.limayrac.library.model.Avis;
import fr.limayrac.library.model.Emprunts;
import fr.limayrac.library.model.Livres;
import fr.limayrac.library.model.Users;
import fr.limayrac.library.repository.AvisRepository;
import fr.limayrac.library.repository.EmpruntRepository;
import fr.limayrac.library.repository.LivreRepository;
import fr.limayrac.library.repository.UserRepository;
import fr.limayrac.library.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class AppController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private EmpruntRepository empruntRepository;

    @Autowired
    private AvisRepository avisRepository;

    @GetMapping("")
    public String index(){
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new Users());

        return "form/signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(Users user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        return "form/register_success";
    }

    @GetMapping("/livres")
    public String listLivres (Model model){
        List<Livres>  booksList = livreRepository.findAll();
        model.addAttribute("booksList", booksList);

        return "/livre/list";
    }

    @GetMapping("/livre/creerlivre")
    public String creerLivre(Model model) {
        model.addAttribute("livre", new Livres());

        return "form/createLivre";
    }

    @PostMapping("/livre/registerLivre")
    public RedirectView AddLivre (Livres livre){
        livreRepository.save(livre);

        return new RedirectView("/livres");
    }

    @GetMapping("/livre/delete/{id}")
    public RedirectView DeleteLivre (@PathVariable Long id){
        livreRepository.deleteById(id);

        return new RedirectView("/livres");
    }

    @GetMapping("/livre/detail/{id}")
    public String detailLivre (Model model,@PathVariable Long id){
        Livres livre = livreRepository.findById(id).orElse(null);
        List<Avis> listAvis = livre.getAvis();
        model.addAttribute("livre", livre);
        model.addAttribute("avis", new Avis());
        model.addAttribute("listAvis",listAvis);
        return "/livre/detail";
    }

    @PostMapping("/livre/detail/avis/{id}")
    public RedirectView AddLivre (Avis avis,@PathVariable Long id){
        avis.setLivre(livreRepository.findById(id).orElse(null));
        avisRepository.save(avis);
        String url = "/livre/detail/"+id.toString();
        return new RedirectView(url);
    }

    @GetMapping("/user/listEmprunt")
    public String listLivresEmprunt (Model model){
        List<Emprunts> emprunts = empruntRepository.findAll();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long idUser = ((CustomUserDetails)principal).getUser().getId();
        emprunts.removeIf(emprunt -> !Objects.equals(emprunt.getUser().getId(), idUser));

        /*List<Livres> livres = new ArrayList<>();
        for (Emprunts emprunt: emprunts )
        {
            livres.add(emprunt.getLivre());
        }*/
        model.addAttribute("emprunts", emprunts);
        return "/user/listEmprunt";
    }

    @GetMapping("/livre/emprunt/{id}")
    public RedirectView registerEmprunt(@PathVariable Long id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users User = ((CustomUserDetails)principal).getUser();
        Emprunts emprunt = new Emprunts();
        emprunt.setLivre(livreRepository.findById(id).orElse(null));
        emprunt.setUser(userRepository.findById(User.getId()).orElse(null));
        emprunt.setDateEmprunt(LocalDateTime.now().toString());
        empruntRepository.save(emprunt);

        return new RedirectView("/user/listEmprunt");
    }

    @GetMapping("/user/emprunt/restituer/{id}")
    public RedirectView DeleteEmprunt (@PathVariable Long id){
        empruntRepository.deleteById(id);
        return new RedirectView("/user/listEmprunt");
    }

}
