package config;



import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Created by OmiD.HaghighatgoO on 06/10/2018.
 */

@DeclareRoles({"users", "admin"})
@SecurityDomain("other")
@ApplicationPath("resources")
public class JAXRSConfiguration extends Application {
}
