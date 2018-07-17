import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.jaxrs.JAXRSArchive;


/**
 * Created by OmiD.HaghighatgoO on 06/02/2018.
 */


public class starter {

    public static void main(String[] args) {

        try {
            Swarm swarm = new Swarm();
            swarm.start() ;
            swarm.deploy() ;

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
