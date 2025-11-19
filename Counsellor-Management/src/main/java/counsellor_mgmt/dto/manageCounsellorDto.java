package counsellor_mgmt.dto;

public class manageCounsellorDto {

    private Long id;
    private String name;
    private String email;
    private Integer activeCount;
    private Integer closedCount;



    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getActiveCount() { return activeCount; }
    public void setActiveCount(Integer activeCount) { this.activeCount = activeCount; }

    public Integer getClosedCount() { return closedCount; }
    public void setClosedCount(Integer closedCount) { this.closedCount = closedCount; }
}
