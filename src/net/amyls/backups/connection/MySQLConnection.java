package net.amyls.backups.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import net.amyls.backups.BackupPlugin;
import net.amyls.backups.connection.files.Logger;
import net.amyls.backups.connection.stores.Callback;
import net.amyls.backups.connection.stores.Store;

/**
 * @Author: amyls.
 * @Website: https://420club.xyz/
 * @Discord: amyls.#0004
 *
 * @Create 28.05.2022
 * @Copyright
 */

public class MySQLConnection implements Store {
    private final String host;

    private final String user;

    private final String pass;

    private final String name;

    private final String prefix;

    private final int port;

    private Connection conn;

    private long time;

    private Executor executor;

    private AtomicInteger ai;

    public MySQLConnection(String host, int port, String user, String pass, String name, String prefix) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.name = name;
        this.prefix = prefix;
        this.executor = Executors.newSingleThreadExecutor();
        this.time = System.currentTimeMillis();
        this.ai = new AtomicInteger();
        (new BukkitRunnable() {
            public void run() {
                if (System.currentTimeMillis() - MySQLConnection.this.time > 30000L)
                    MySQLConnection.this.update(false, "DO 1");
            }
        }).runTaskTimer((Plugin)BackupPlugin.getInst(), 600L, 600L);
    }

    public boolean connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.name, this.user, this.pass);
            Logger.info(new String[] { "Connected to the MySQL server!" });
            return true;
        } catch (ClassNotFoundException e) {
            Logger.warning(new String[] { "JDBC driver not found!", "Error: " + e.getMessage() });
            e.printStackTrace();
        } catch (SQLException e2) {
            Logger.warning(new String[] { "Can not connect to a MySQL server!", "Error: " + e2.getMessage() });
            e2.printStackTrace();
        }
        return false;
    }

    public void update(boolean now, final String update) {
        this.time = System.currentTimeMillis();
        Runnable r = new Runnable() {
            public void run() {
                try {
                    MySQLConnection.this.conn.createStatement().executeUpdate(update.replace("{P}", MySQLConnection.this.prefix));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };
        if (now) {
            r.run();
        } else {
            this.executor.execute(r);
        }
    }

    public ResultSet update(String update) {
        try {
            Statement statement = this.conn.createStatement();
            statement.executeUpdate(update.replace("{P}", this.prefix), 1);
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next())
                return rs;
        } catch (SQLException e) {
            Logger.warning(new String[] { "An error occurred with given query '" + update.replace("{P}", this.prefix) + "'!", "Error: " + e.getMessage() });
            e.printStackTrace();
        }
        return null;
    }

    public void disconnect() {
        if (this.conn != null)
            try {
                this.conn.close();
            } catch (SQLException e) {
                Logger.warning(new String[] { "Can not close the connection to the MySQL server!", "Error: " + e.getMessage() });
                e.printStackTrace();
            }
    }

    public void reconnect() {
        connect();
    }

    public boolean isConnected() {
        try {
            return (!this.conn.isClosed() || this.conn == null);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet query(String query) {
        try {
            return this.conn.createStatement().executeQuery(query.replace("{P}", this.prefix));
        } catch (SQLException e) {
            Logger.warning(new String[] { "An error occurred with given query '" + query.replace("{P}", this.prefix) + "'!", "Error: " + e.getMessage() });
            e.printStackTrace();
            return null;
        }
    }

    public void query(final String query, final Callback<ResultSet> cb) {
        (new Thread(new Runnable() {
            public void run() {
                try {
                    ResultSet rs = MySQLConnection.this.conn.createStatement().executeQuery(query.replace("{P}", MySQLConnection.this.prefix));
                    cb.done(rs);
                } catch (SQLException e) {
                    cb.error(e);
                    e.printStackTrace();
                }
            }
        },
        "MySQL Thread #" + this.ai.getAndIncrement())).start();
    }

    public Connection getConnection() {
        return this.conn;
    }
}
