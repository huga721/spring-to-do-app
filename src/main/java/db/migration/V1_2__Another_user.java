package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

/**
 * Example of a Java-based migration using Spring JDBC.
 */
public class V1_2__Another_user extends BaseJavaMigration {
    public void migrate(Context context) {
        new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true))
                .execute("INSERT INTO tasks (description, done) VALUES ('TEsting', true)");
    }
}