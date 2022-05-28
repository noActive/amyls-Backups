package net.amyls.backups.connection.stores;

/**
 * @Author: amyls.
 * @Website: https://420club.xyz/
 * @Discord: amyls.#0004
 *
 * @Create 28.05.2022
 * @Copyright
 */

public interface Entry {
    void insert();

    void update(boolean paramBoolean);

    void delete();
}
