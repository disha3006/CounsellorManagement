package counsellor_mgmt.service;

import counsellor_mgmt.dto.manageCounsellorDto;
import counsellor_mgmt.entity.User;
import counsellor_mgmt.repo.EnquiryRepository;
import counsellor_mgmt.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CounsellorService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EnquiryRepository enquiryRepo;


    public List<manageCounsellorDto> getDashboardInfo() {

        List<User> counsellors = userRepo.findByRole("COUNSELLOR");
        List<manageCounsellorDto> counsellor_list = new ArrayList<>();

        for (User u : counsellors) {

            int active = enquiryRepo.countByCounsellorIdAndStatus(u.getId(), "Open");
            int closed = enquiryRepo.countByCounsellorIdAndStatus(u.getId(), "Enrolled");

            manageCounsellorDto dto = new manageCounsellorDto();

            dto.setId(u.getId());
            dto.setName(u.getName());
            dto.setEmail(u.getEmail());
            dto.setActiveCount(active);
            dto.setClosedCount(closed);
            counsellor_list.add(dto);
        }

        return counsellor_list;
    }
}

