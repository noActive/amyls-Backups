package net.amyls.backups.connection.stores;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * @Author: amyls.
 * @Website: https://420club.xyz/
 * @Discord: amyls.#0004
 *
 * @Create 28.05.2022
 * @Copyright
 */

public interface Store {
    Connection getConnection();

    boolean connect();

    void disconnect();

    void reconnect();

    boolean isConnected();

    ResultSet query(String paramString);

    void query(String paramString, Callback<ResultSet> paramCallback);

    void update(boolean paramBoolean, String paramString);

    ResultSet update(String paramString);
}
