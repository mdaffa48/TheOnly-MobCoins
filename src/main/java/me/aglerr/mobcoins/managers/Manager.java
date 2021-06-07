package me.aglerr.mobcoins.managers;

public interface Manager {

    /**
     * Manager load logic, called on server startup
     */
    void load();

    /**
     * Manager save logic, called on server stopped
     */
    void save();

}
