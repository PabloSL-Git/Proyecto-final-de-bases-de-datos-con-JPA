package utilidades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("bibliotecaPU");

    // Obtener EntityManager
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Cerrar fábrica (solo al cerrar la app)
    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
