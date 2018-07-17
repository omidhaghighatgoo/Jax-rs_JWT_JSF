package beans;

/**
 * Created by OmiD.HaghighatgoO on 06/05/2018.
 */

import dao.entity.User;

import javax.enterprise.context.RequestScoped;
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
public class UserBean implements Serializable {


    private List<User> userList;

    private String name;
    private String family;
    private String username;
    private String message;


    public String loadUsers() {

        String users = "";
        try {
            String url = "http://localhost:9080/resources/api/loadUsers";

            FacesContext context = FacesContext.getCurrentInstance();
            String jwt = (String) context.getExternalContext().getSessionMap().get("userToken");
            if (jwt == null){
                HttpServletRequest origRequest = (HttpServletRequest) context.getExternalContext().getRequest();
                String contextPath = origRequest.getContextPath();
                context.getExternalContext()
                        .redirect(contextPath + "/pages/login.xhtml");

            }


            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(url);
            userList = target.request().header("Authorization", "Bearer " + jwt).get(List.class);

            Jsonb jsonb = JsonbBuilder.create();
            users = jsonb.toJson(userList);
            message = "انجام شد";


        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public void createUser() {

        try {
            String url = "http://localhost:9080/resources/api/createUser";

            User user = new User();
            user.setName(name);
            user.setFamily(family);
            user.setUserName(username);

            Jsonb jsonb = JsonbBuilder.create();
            String result = jsonb.toJson(user);

            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(url);
            Response response = target.request().post(Entity.entity(result, MediaType.APPLICATION_JSON));

            try {
                if (response.getStatus() != 200) {
                    throw new RuntimeException("Failed with HTTP error code : " + response.getStatus());
                }
                System.out.println("Successfully got result: " + response.readEntity(String.class));
            } finally {
                response.close();
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<User> getUsers() {
        return userList;
    }

    public void setUsers(List<User> userList) {
        this.userList = userList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}