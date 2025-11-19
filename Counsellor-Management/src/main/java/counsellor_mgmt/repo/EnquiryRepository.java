package counsellor_mgmt.repo;

import counsellor_mgmt.entity.Enquiry;
import counsellor_mgmt.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;


public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {

    long countByStatus(String open);
    Page<Enquiry> findByCounsellorId(Long userId, Pageable pageable);

    long countByCounsellorId(Long userId);

    long countByStatusAndCounsellorId(String status, Long userId);

    int countByCounsellorIdAndStatus(Long counsellorId, String status);

}

