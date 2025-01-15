package net.cc.staple;

import net.cc.staple.listener.PlayerListener;
import net.cc.staple.player.PlayerManager;
import net.cc.staple.player.TpaManager;
import net.cc.staple.storage.impl.MySQLManager;
import net.cc.staple.storage.Storage;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class StaplePlugin extends JavaPlugin {

    public static String serverName;

    private Storage storage;
    private PlayerManager playerManager;
    private TpaManager tpaManager;

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        serverName = getConfig().getString("server");
        StapleConfig.load(this);

        setupStorage();
        if (this.storage == null) {
            getLogger().severe("Storage was not initialized correctly. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        setupListeners();
        setupManagers();
    }

    @NotNull
    public Storage getStorage() {
        return storage;
    }

    @NotNull
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    @NotNull
    public TpaManager getTpaManager() {
        return tpaManager;
    }

    private void setupStorage() {
        try {
            String url = getConfig().getString("mysql.url");
            String username = getConfig().getString("mysql.username");
            String password = getConfig().getString("mysql.password");
            this.storage = new MySQLManager(this, url, username, password);
        } catch (ClassNotFoundException e) {
            getLogger().severe("Error while setting up SQL storage: " + e.getMessage());
        }
    }

    private void setupManagers() {
        playerManager = new PlayerManager(this);
        tpaManager = new TpaManager(this);
    }

    private void setupListeners() {
        new PlayerListener(this);
    }
}
