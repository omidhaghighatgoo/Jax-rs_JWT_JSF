package dao.daoimpl;

import dao.daoint.GenericDAO;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 * Created by OmiD.HaghighatgoO on 05/28/2018.
 */

@Stateless
public abstract  class GenericDAOImpl<T ,PK extends Serializable> implements GenericDAO<T,PK> {


    private Class< T > clazz;

    @PersistenceContext
    EntityManager entityManager;

    public final void setClazz( Class< T > clazzToSet ){
        this.clazz = clazzToSet;
    }

    public T findById( PK id ){
        return entityManager.find( clazz, id );
    }
    public List<T> findAll(){
        return entityManager.createQuery( "from " + clazz.getName() + " u" )
                .getResultList();
    }

    public void create( T entity ){
        entityManager.persist( entity );
    }

    public T update( T entity ){
        return entityManager.merge( entity );
    }

    public void delete( T entity ){
        entityManager.remove( entity );
    }
    public void deleteById( PK entityId ){
        T entity = findById( entityId );
        delete( entity );
    }

}
