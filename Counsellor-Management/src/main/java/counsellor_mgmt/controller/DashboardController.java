package counsellor_mgmt.controller;


import counsellor_mgmt.entity.Enquiry;
import counsellor_mgmt.entity.User;
import counsellor_mgmt.repo.EnquiryRepository;
import counsellor_mgmt.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DashboardController {

    @Autowired
    private EnquiryRepository enquiryRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/dashboard")
    public String viewDashboard(Model model, HttpSession session) {

        String userName = (String) session.getAttribute("loggedUserName");
        model.addAttribute("userName", userName);
        long total = enquiryRepo.count();
        long open = enquiryRepo.countByStatus("Open");
        long enrolled = enquiryRepo.countByStatus("Enrolled");
        long lost = enquiryRepo.countByStatus("Lost");

        model.addAttribute("total", total);
        model.addAttribute("open", open);
        model.addAttribute("enrolled", enrolled);
        model.addAttribute("lost", lost);

        return "dashboard";
    }


    @GetMapping("/enquiry/add")
    public String showAddEnquiry(@ModelAttribute("enquiryForm") Enquiry enq) {
        return "addEnquiry";
    }

    @PostMapping("/enquiry/save")
    public String saveEnquiry(@ModelAttribute("enquiryForm") Enquiry enq) {
        enquiryRepo.save(enq);
        return "redirect:/enquiry/list";
    }

    @GetMapping("/enquiry/list")
    public String viewAllEnquiries(Model model) {
        model.addAttribute("enquiries", enquiryRepo.findAll());
        return "viewEnquiry";
    }

}
