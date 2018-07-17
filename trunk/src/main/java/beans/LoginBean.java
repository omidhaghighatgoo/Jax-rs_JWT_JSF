package beans;

/**
 * Created by OmiD.HaghighatgoO on 06/05/2018.
 */

import dao.entity.User;
import jwtutil.JWTokenUtility;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class LoginBean implements Serializable {


    private String username;
    private String password;

    private String errormsg;

    private User user;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public void login() {

        try {
            String url = "http://localhost:9080/resources/api/auth";

            String jwt = JWTokenUtility.buildJWT(username, password);

            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(url);
            Response response = target.request().header("Authorization", "Bearer " + jwt).get();
            response.getLength();

            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest origRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            String contextPath = origRequest.getContextPath();


            if(response.getStatus() == 401 || response.getStatus() == 403){
                errormsg = "خطای ورود" ;
                context.addMessage(null, new FacesMessage("خطا",  "نام کاربری یا رمز عبور اشتباه است") );
            }
            else {

                context.getExternalContext().getSessionMap().put("userToken", response.readEntity(String.class));

                context.getExternalContext()
                        .redirect(contextPath + "/pages/home.xhtml");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test(){
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage("خطا",  "Your message: " + "salam") );
    }

    public void logOut() {

        try {


            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest origRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            String contextPath = origRequest.getContextPath();




                context.getExternalContext().getSessionMap().clear();
                context.getExternalContext()
                        .redirect(contextPath + "/pages/login.xhtml");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}