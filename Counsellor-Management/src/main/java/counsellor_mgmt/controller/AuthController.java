package counsellor_mgmt.controller;

import counsellor_mgmt.entity.User;
import counsellor_mgmt.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/register")
    public String showRegister(@ModelAttribute("userForm") User user) {
        return "register";
    }

    @GetMapping("/login")
    public String showLogin(@ModelAttribute("userForm") User user) {
        return "login";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userForm") User user, RedirectAttributes ra) {

        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            return "redirect:/register";
        }

        user.setRole("COUNSELLOR");  //this role will be default for the registration. if u want admin role to be set, use db.
        userRepo.save(user);
        return "redirect:/login";
    }


    @PostMapping("/login")
    public String doLogin(@ModelAttribute("userForm") User formUser,
                          HttpSession session) {

        Optional<User> optUser = userRepo.findByEmail(formUser.getEmail());

        if (optUser.isPresent() && optUser.get().getPasswordHash().equals(formUser.getPasswordHash())) {

            User u = optUser.get();

            session.setAttribute("loggedUserId", u.getId());
            session.setAttribute("loggedUserName", u.getName());
            session.setAttribute("loggedUserRole", u.getRole());

            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }



    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }


}
