package me.aleiv.core.paper;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.commands.PaperCommandManager;
import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import lombok.Getter;
import lombok.Setter;
import me.aleiv.core.paper.commands.GlobalCMD;
import me.aleiv.core.paper.listeners.EasyListener;
import me.aleiv.core.paper.listeners.GlobalListener;
import me.aleiv.core.paper.listeners.HardListener;
import me.aleiv.core.paper.listeners.LobbyListener;
import me.aleiv.core.paper.listeners.MediumListener;
import us.jcedeno.libs.rapidinv.RapidInvManager;

public class Core extends JavaPlugin {

    private static @Getter Core instance;
    private static @Getter @Setter TaskChainFactory taskChainFactory;
    private @Getter Game game;
    private @Getter PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;

        game = new Game(this);
        game.runTaskTimerAsynchronously(this, 0L, 20L);

        taskChainFactory = BukkitTaskChainFactory.create(this);

        RapidInvManager.register(this);
        
        //LISTENERS

        Bukkit.getPluginManager().registerEvents(new GlobalListener(this), this);
        Bukkit.getPluginManager().registerEvents(new LobbyListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EasyListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MediumListener(this), this);
        Bukkit.getPluginManager().registerEvents(new HardListener(this), this);

        //COMMANDS
        
        commandManager = new PaperCommandManager(this);

        commandManager.registerCommand(new GlobalCMD(this));

    }

    @Override
    public void onDisable() {

    }

    public static <T> TaskChain<T> newChain() {
        return taskChainFactory.newChain();
    }

    public static <T> TaskChain<T> newSharedChain(String name) {
        return taskChainFactory.newSharedChain(name);
    }

}