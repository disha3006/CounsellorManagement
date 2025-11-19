package counsellor_mgmt.controller;

import counsellor_mgmt.entity.Enquiry;
import counsellor_mgmt.entity.User;
import counsellor_mgmt.repo.EnquiryRepository;
import counsellor_mgmt.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class DashboardController {

    @Autowired
    private EnquiryRepository enquiryRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/dashboard")
    public String viewDashboard(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedUserId");
        String role = (String) session.getAttribute("loggedUserRole");

        if (userId == null) return "redirect:/login";

        model.addAttribute("userName", session.getAttribute("loggedUserName"));

        if (role.equals("ADMIN")) {

            long total = enquiryRepo.count();
            long open = enquiryRepo.countByStatus("Open");
            long enrolled = enquiryRepo.countByStatus("Enrolled");
            long lost = enquiryRepo.countByStatus("Lost");

            model.addAttribute("total", total);
            model.addAttribute("open", open);
            model.addAttribute("enrolled", enrolled);
            model.addAttribute("lost", lost);
        }
        else {

            long total = enquiryRepo.countByCounsellorId(userId);
            long open = enquiryRepo.countByStatusAndCounsellorId("Open", userId);
            long enrolled = enquiryRepo.countByStatusAndCounsellorId("Enrolled", userId);
            long lost = enquiryRepo.countByStatusAndCounsellorId("Lost", userId);

            model.addAttribute("total", total);
            model.addAttribute("open", open);
            model.addAttribute("enrolled", enrolled);
            model.addAttribute("lost", lost);
        }

        return "dashboard";
    }



    @GetMapping("/enquiry/add")
    public String showAddEnquiry(@ModelAttribute("enquiryForm") Enquiry enq,
                                 HttpSession session,
                                 Model model) {

        String role = (String) session.getAttribute("loggedUserRole");

        if ("ADMIN".equals(role)) {
            model.addAttribute("counsellors", userRepo.findAll());
        }

        return "addEnquiry";
    }


    @GetMapping("/enquiry/edit/{id}")
    public String editEnquiry(@PathVariable Long id,
                              Model model,
                              HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedUserId");
        String role = (String) session.getAttribute("loggedUserRole");

        if (userId == null) return "redirect:/login";

        Enquiry enq = enquiryRepo.findById(id).orElse(null);
        if (enq == null) return "redirect:/enquiry/list";


        if ("COUNSELLOR".equals(role) && !enq.getCounsellor().getId().equals(userId)) {
            return "redirect:/enquiry/list";
        }

        model.addAttribute("enquiryForm", enq);


        if ("ADMIN".equals(role)) {
            model.addAttribute("counsellors", userRepo.findAll());
        }

        return "addEnquiry";
    }


    @PostMapping("/enquiry/save")
    public String saveEnquiry(@ModelAttribute("enquiryForm") Enquiry enq,
                              @RequestParam(required = false) Long counsellorId,
                              HttpSession session,
                              RedirectAttributes ra) {

        Long userId = (Long) session.getAttribute("loggedUserId");
        String role = (String) session.getAttribute("loggedUserRole");

        if (userId == null) return "redirect:/login";

        User counsellor;

        if ("ADMIN".equals(role)) {
            counsellor = userRepo.findById(counsellorId).orElse(null);
        } else {
            counsellor = userRepo.findById(userId).orElse(null);
        }

        enq.setCounsellor(counsellor);
        enquiryRepo.save(enq);
        ra.addFlashAttribute("successMsg", "Enquiry added successfully!");
        return "redirect:/enquiry/list";
    }


    @PostMapping("/enquiry/update")
    public String updateEnquiry(@ModelAttribute("enquiryForm") Enquiry enq,
                                @RequestParam(required = false) Long counsellorId,
                                HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedUserId");
        String role = (String) session.getAttribute("loggedUserRole");

        if (userId == null) return "redirect:/login";

        Enquiry existing = enquiryRepo.findById(enq.getId()).orElse(null);
        if (existing == null) return "redirect:/enquiry/list";

        if ("COUNSELLOR".equals(role) && !existing.getCounsellor().getId().equals(userId)) {
            return "redirect:/enquiry/list";
        }


        existing.setName(enq.getName());
        existing.setPhno(enq.getPhno());
        existing.setClassMode(enq.getClassMode());
        existing.setCourse(enq.getCourse());
        existing.setStatus(enq.getStatus());

        if ("ADMIN".equals(role))
            existing.setCounsellor(userRepo.findById(counsellorId).orElse(null));

        enquiryRepo.save(existing);

        return "redirect:/enquiry/list";
    }




    @GetMapping("/enquiry/list")
    public String list(Model model,
                       @RequestParam(defaultValue="0") int page,
                       @RequestParam(defaultValue="5") int size,
                       HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedUserId");
        String role = (String) session.getAttribute("loggedUserRole");

        if (userId == null) return "redirect:/login";

        Pageable pageable = PageRequest.of(page, size);

        Page<Enquiry> pageEnquiry;

        if (role.equals("ADMIN")) {
            pageEnquiry = enquiryRepo.findAll(pageable);
        } else {
            pageEnquiry = enquiryRepo.findByCounsellorId(userId, pageable);
        }

        model.addAttribute("enquiries", pageEnquiry.getContent());
        model.addAttribute("currentPage", pageEnquiry.getNumber());
        model.addAttribute("totalPages", pageEnquiry.getTotalPages());

        return "viewEnquiry";
    }



    @PostMapping("/delete")
    public String delete(@RequestParam("enqId") Long enqId,
                         HttpSession session,
                        RedirectAttributes redirect) {

        Long userId = (Long) session.getAttribute("loggedUserId");
        String role = (String) session.getAttribute("loggedUserRole");

        if (userId == null) return "redirect:/login";

        Enquiry e = enquiryRepo.findById(enqId).orElse(null);

        if (e != null) {
            if (role.equals("ADMIN") || e.getCounsellor().getId().equals(userId)) {
                enquiryRepo.delete(e);
            }
        }
        redirect.addFlashAttribute("DeleteMsg", "Enquiry deleted successfully!");

        return "redirect:/enquiry/list";
    }



}
