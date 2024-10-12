package com.spektrsoyuz.staple.util;

import com.spektrsoyuz.staple.StaplePlugin;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public abstract class CacheManager<T> {

    protected final StaplePlugin plugin;
    private final Map<UUID, T> cache;

    public CacheManager() {
        this.plugin = StaplePlugin.getInstance();
        this.cache = new ConcurrentHashMap<>();

        long oneMinuteTicks = 20L * 60;
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new SaveTask(), oneMinuteTicks, oneMinuteTicks);
    }

    public abstract T createInstance(UUID playerId);

    public abstract void loadData(UUID playerId, T instance, Consumer<T> callback);

    public abstract void saveData(T instance);

    public T get(UUID playerId) {
        return cache.get(playerId);
    }

    public void add(UUID playerId, T instance) {
        cache.put(playerId, instance);
    }

    public void remove(UUID playerId) {
        cache.remove(playerId);
    }

    public void load(UUID playerId) {
        T instance = createInstance(playerId);
        loadData(playerId, instance, loaded_instance -> add(playerId, loaded_instance));
    }

    public void getAndLoad(UUID playerId, Consumer<T> callback) {
        T instance = createInstance(playerId);
        loadData(playerId, instance, loaded_instance -> {
            add(playerId, loaded_instance);
            callback.accept(loaded_instance);
        });
    }

    public void saveAll() {
        for (UUID playerId : cache.keySet()) {
            T instance = cache.get(playerId);
            saveData(instance);
        }
    }

    private class SaveTask implements Runnable {
        @Override
        public void run() {
            Set<T> cacheValues = new HashSet<>(cache.values());
            for (T instance : cacheValues) {
                saveData(instance);
            }
        }
    }
}
