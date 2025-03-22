package site.easy.to.build.crm.service.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseCleanupService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Liste des tables à exclure
    private static final List<String> EXCLUDED_TABLES = List.of("roles", "users", "user_roles");

    public void truncateAllTablesExceptExcluded() {
        // Obtenir les noms des tables
        List<String> tables = jdbcTemplate.queryForList(
            "SELECT table_name FROM information_schema.tables WHERE table_schema = DATABASE()",
            String.class
        );

        // Désactiver les contraintes de clé étrangère
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

        for (String table : tables) {
            if (!EXCLUDED_TABLES.contains(table)) {
                // Truncate la table
                jdbcTemplate.execute("TRUNCATE TABLE `" + table + "`");

                // Réinitialise l'AUTO_INCREMENT
                jdbcTemplate.execute("ALTER TABLE `" + table + "` AUTO_INCREMENT = 1");
            }
        }

        // Réactiver les contraintes de clé étrangère
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }
}
