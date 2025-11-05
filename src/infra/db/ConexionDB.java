package infra.db;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConexionDB {
    private static final String URL = "jdbc:h2:./data/db;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static Connection getConnection() {
        try {
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener conexión H2", e);
        }
    }

    public static void initSchemaIfAbsent() {
        try {
            Files.createDirectories(Paths.get("./data"));
            Path marker = Paths.get("./data/.schema_aplicado");

            if (Files.exists(marker)) {
                System.out.println("✓ Base de datos ya inicializada");
                return;
            }

            System.out.println("→ Inicializando base de datos H2...");
            String sql = Files.readString(Path.of("./db/schema.sql"));
            try (Connection cn = getConnection(); Statement st = cn.createStatement()) {
                int count = 0;
                for (String part : sql.split(";")) {
                    String trimmed = part.trim();
                    if (!trimmed.isEmpty()) {
                        st.execute(trimmed);
                        count++;
                    }
                }
                System.out.println("✓ Esquema creado: " + count + " sentencias ejecutadas");
            }
            Files.writeString(marker, "ok");
        } catch (Exception e) {
            throw new RuntimeException("Error inicializando esquema H2", e);
        }
    }
}
