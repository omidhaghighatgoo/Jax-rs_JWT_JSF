package dao.daoimpl;

import dao.daoint.UserDAO;
import dao.entity.User;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by OmiD.HaghighatgoO on 05/28/2018.
 */

@Stateless
public class UserDaoImpl extends GenericDAOImpl<User, Long> implements UserDAO {

    @PostConstruct
    public void init() {
        super.setClazz(User.class);
    }

    @PersistenceContext(unitName = "users")
    private EntityManager em;

    public void insert(User user) {
        em.persist(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public User findByUsername(String username){

        User user = null;
        try{
         user =(User)  em.createNamedQuery("User.findByUsername")
                .setParameter("userName",username)
                .getSingleResult() ;}
        catch (Exception e){
           e.printStackTrace(); 
        }

     return  user ;
    }


    public List<User> findAllUser() {
        return super.findAll();
    }
}
