package Service.serviceint;

import dao.entity.User;

import javax.ejb.Stateless;
import java.io.Serializable;
import java.util.List;

/**
 * Created by OmiD.HaghighatgoO on 06/10/2018.
 */


public interface UserService   {

    public void createUser(User user) ;

    public List loadAll() ;

    public User findByUsername(String username);
}
