package dao.entity;

import javax.persistence.*;

/**
 * Created by OmiD.HaghighatgoO on 05/28/2018.
 */

@Entity
@Table(name = "APPUSER")
@NamedQueries({
        @NamedQuery(
                name="User.findByUsername",
                query="SELECT au FROM User au WHERE au.userName = :userName  "
        )
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_ID")
    private long userId;

    @Column(name="USERNAME" , nullable = false , unique = true)
    private String userName ;

    @Column(name="NAME" , nullable = false )
    private String name ;

    @Column(name="FAMILY" , nullable = false )
    private String family ;

    //TODO :   change nullable to false after testing
    @Column(name="PASSWORD" , nullable = true)
    private String password ;

    //TODO :   change nullable to false after testing
    @Column (name="ROLE" , nullable = true)
    private String role ;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
