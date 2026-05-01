package utilidades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("bibliotecaPU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Llamar solo al cerrar la aplicación
    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
