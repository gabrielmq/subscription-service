package io.github.gabrmsouza.subscription.infrastructure.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;

public final class JdbcUtils {
    private JdbcUtils() {}

    public static Instant getInstant(final ResultSet rs, final String prop) throws SQLException {
        if (prop == null || prop.isBlank() || rs == null) {
            return null;
        }
        var ts = rs.getTimestamp(prop);
        return ts != null ? ts.toInstant() : null;
    }

    public static LocalDate getLocalDate(final ResultSet rs, final String prop) throws SQLException {
        if (prop == null || prop.isBlank() || rs == null) {
            return null;
        }
        var ts = rs.getDate(prop);
        return ts != null ? ts.toLocalDate() : null;
    }
}
