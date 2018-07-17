package dao.daoint;

import javax.ejb.Stateless;
import java.io.Serializable;
import java.util.List;

/**
 * Created by OmiD.HaghighatgoO on 05/28/2018.
 */

@Stateless
public interface GenericDAO<T , PK> {

    public void create( T entity );
    List<T> findAll();
    public T findById( PK id ) ;
    public T update( T entity );
    void delete( T entity );
    public void deleteById( PK entityId );


}
