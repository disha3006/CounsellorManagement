package counsellor_mgmt.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enquiries")
public class Enquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phno;
    private String classMode;
    private String course;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counsellor_id")
    private User counsellor;

    private LocalDateTime createdAt = LocalDateTime.now();


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhno() { return phno; }
    public void setPhno(String phno) { this.phno = phno; }

    public String getClassMode() { return classMode; }
    public void setClassMode(String classMode) { this.classMode = classMode; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public User getCounsellor() { return counsellor; }
    public void setCounsellor(User counsellor) { this.counsellor = counsellor; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
