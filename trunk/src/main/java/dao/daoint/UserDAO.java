package dao.daoint;


import dao.entity.User;

import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by OmiD.HaghighatgoO on 05/28/2018.
 */
@Stateless
public interface UserDAO {


    public List<User> findAllUser() ;

    public void insert(User user);

    public User findByUsername(String username) ;








}
