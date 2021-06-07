package me.aglerr.mobcoins.managers.managers;

import me.aglerr.mobcoins.managers.Manager;
import org.bukkit.entity.Entity;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SpawnerSpawnManager implements Manager {

    private final Set<UUID> entityUUID = new HashSet<>();

    public void addEntity(Entity entity){
        if(this.entityUUID.contains(entity.getUniqueId())) return;
        this.entityUUID.add(entity.getUniqueId());
    }

    public void removeEntity(Entity entity){
        this.entityUUID.remove(entity.getUniqueId());
    }

    public boolean isSpawnFromSpawner(Entity entity){
        return this.entityUUID.contains(entity.getUniqueId());
    }

    @Override
    public void load() {
    }

    @Override
    public void save() {
    }

}
