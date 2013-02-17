/*
 * Copyright (C) 2012 Calenria <https://github.com/Calenria/> and contributors
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3.0 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package com.github.calenria.commanddisabler.listener;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.github.calenria.commanddisabler.CommandDisabler;

/**
 * Eventlistener Klasse.
 * 
 * @author Calenria
 * 
 */
public class CommandDisablerListener implements Listener {
    /**
     * Bukkit Logger.
     */
    @SuppressWarnings("unused")
    private static Logger   log    = Logger.getLogger("Minecraft");
    /**
     * BungeeTools Plugin.
     */
    private CommandDisabler plugin = null;

    /**
     * Registriert die Eventhandlers
     * 
     * @param btPlugin
     *            BungeeTools Plugin
     */
    public CommandDisablerListener(final CommandDisabler btPlugin) {
        this.plugin = btPlugin;
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    public void onPlayerCommand(final PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().toLowerCase().substring(1);

        if (command.startsWith("jail") && !event.getPlayer().hasPermission("commanddisabler.jail")) {

            try {
                String player = command.split(" ")[1];
                OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(player);
                if (oPlayer.isOnline()) {
                    oPlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/ungod " + player);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (String disablecmd : this.plugin.config.getDisable()) {
            if (!disablecmd.startsWith(command)) {
                continue;
            }
            if (!event.getPlayer().hasPermission("commanddisabler.command." + disablecmd)) {
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Keine Rechte"));
                event.setCancelled(true);
                return;
            }
        }
    }

}
