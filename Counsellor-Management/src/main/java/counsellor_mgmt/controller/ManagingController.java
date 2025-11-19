package counsellor_mgmt.controller;

import counsellor_mgmt.entity.Enquiry;
import counsellor_mgmt.entity.User;
import counsellor_mgmt.repo.EnquiryRepository;
import counsellor_mgmt.repo.UserRepository;
import counsellor_mgmt.service.CounsellorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
    public class ManagingController {

    @Autowired
    private CounsellorService counsellorService;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/manage/counsellors")
    public String manageCounsellors(Model m, HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("loggedUserRole"))) {
            return "redirect:/access-denied";
        }

        m.addAttribute("counsellors", counsellorService.getDashboardInfo());
        return "manageCounsellors";
    }

    @PostMapping("/deletecounsellor")
    public String delete(@RequestParam("counsellorId") Long counsellorId,
                         HttpSession session) {

        String role = (String) session.getAttribute("loggedUserRole");

        if (!"ADMIN".equals(role)) {
            return "redirect:/accessDenied";
        }

        User user = userRepo.findById(counsellorId).orElse(null);
        if (user != null) {
            userRepo.delete(user);
        }
        return "redirect:/manageCounsellors";
    }
}

