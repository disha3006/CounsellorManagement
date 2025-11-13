package counsellor_mgmt.repo;

import counsellor_mgmt.entity.Enquiry;
import counsellor_mgmt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {

    long countByStatus(String open);
}

