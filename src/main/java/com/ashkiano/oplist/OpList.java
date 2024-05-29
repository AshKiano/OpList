package com.ashkiano.oplist;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OpList extends JavaPlugin implements CommandExecutor, Listener {

    @Override
    public void onEnable() {
        this.getCommand("oplist").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
        Metrics metrics = new Metrics(this, 22046);
        this.getLogger().info("Thank you for using the OpList plugin! If you enjoy using this plugin, please consider making a donation to support the development. You can donate at: https://donate.ashkiano.com");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("oplist")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    player.sendMessage(ChatColor.GREEN + "List of OP players:");

                    for (Player opPlayer : Bukkit.getOnlinePlayers()) {
                        if (opPlayer.isOp()) {
                            TextComponent message = new TextComponent(opPlayer.getName() + " ");
                            message.setColor(ChatColor.RED);

                            TextComponent deopButton = new TextComponent("[Click to deop]");
                            deopButton.setColor(ChatColor.YELLOW);
                            deopButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/deop " + opPlayer.getName()));

                            message.addExtra(deopButton);
                            player.spigot().sendMessage(message);
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You must be an OP to use this command.");
                }
            } else {
                sender.sendMessage("This command can only be used by players.");
            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();

        if (message.startsWith("/deop ")) {
            String[] parts = message.split(" ");
            if (parts.length == 2) {
                String targetName = parts[1];
                Player target = Bukkit.getPlayer(targetName);

                if (target != null && target.isOp()) {
                    target.setOp(false);
                    player.sendMessage(ChatColor.GREEN + "Player " + target.getName() + " has been deopped.");
                    target.sendMessage(ChatColor.RED + "You have been deopped.");
                } else {
                    player.sendMessage(ChatColor.RED + "Player not found or is not an OP.");
                }
                event.setCancelled(true);
            }
        }
    }
}