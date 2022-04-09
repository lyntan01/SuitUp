package ws.datamodel;

import entity.SupportTicketEntity;

/**
 *
 * @author lyntan
 */
public class UpdateSupportTicketReq {
    
    private String email;
    private String password;
    private SupportTicketEntity supportTicketEntity;

    public UpdateSupportTicketReq(String email, String password, SupportTicketEntity supportTicketEntity) {
        this.email = email;
        this.password = password;
        this.supportTicketEntity = supportTicketEntity;
    }

    public UpdateSupportTicketReq() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SupportTicketEntity getSupportTicketEntity() {
        return supportTicketEntity;
    }

    public void setSupportTicketEntity(SupportTicketEntity supportTicketEntity) {
        this.supportTicketEntity = supportTicketEntity;
    }
    
    
    
}
