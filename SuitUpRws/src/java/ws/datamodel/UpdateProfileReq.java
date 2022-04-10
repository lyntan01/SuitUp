/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

/**
 *
 * @author keithcharleschan
 */
public class UpdateProfileReq {

    private String email;
    private String password;
    private String firstName;
    private String LastName;

    public UpdateProfileReq() {
    }

    public UpdateProfileReq(String email, String password, String firstName, String LastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.LastName = LastName;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }
    
    

}
