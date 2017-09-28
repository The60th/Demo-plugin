package com.worldciv.events.player;

import com.worldciv.the60th.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;


public class scoreboard implements Listener{

    //declare all vars for global
    final ScoreboardManager manager = Bukkit.getScoreboardManager();
    final Scoreboard board = manager.getNewScoreboard();
    final Scoreboard emptyboard = manager.getNewScoreboard();
    final Team team = board.registerNewTeam("vision status");

    Objective objective = board.registerNewObjective("scoreboard", "dummy");


    Score blankscore = objective.getScore("🔦🔦🔦🔦");
    Score blankscore2 = objective.getScore("🔦🔦🔦");
    Score blankscore3 = objective.getScore("🔦🔦");
    Score blankscore4 = objective.getScore("🔦");


    @SuppressWarnings("deprecation")
    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e) {

      Player p = e.getPlayer();

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {

            public void run() {

                team.setPrefix(ChatColor.RED + "[T] " + ChatColor.RESET);
                team.setCanSeeFriendlyInvisibles(true); // up to you to decide if players should see invis ppl if same team (if vision is on)

                objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                objective.setDisplayName(ChatColor.GOLD + "World Civilization");

                //

                blankscore.setScore(10);

                Score score = objective.getScore(ChatColor.AQUA + "IGN:");
                score.setScore(9);

                Score score1 = objective.getScore(ChatColor.GRAY + p.getName());
                score1.setScore(8);

                blankscore2.setScore(7);

                Score score2 = objective.getScore(ChatColor.AQUA + "Health:");
                score2.setScore(6);

                long health = Math.round(p.getHealth());

                Score score3 = objective.getScore(ChatColor.GRAY + String.valueOf(health));
                score3.setScore(5);

                blankscore3.setScore(4);

                if(team.hasPlayer(p)) {
                    Score score5 = objective.getScore(ChatColor.RED + "Torch [T]:" + ChatColor.BLUE + "✓");
                    score5.setScore(3);
                }else if(!team.hasPlayer(p)) {
                    Score score5 = objective.getScore(ChatColor.RED + "Torch [T]:" + ChatColor.BLUE + "✗");
                    score5.setScore(3);
                }

                ////// below is to add/remove blind from prefix ////

                if (LightLevelEvent.currentlyBlinded.contains(p)) {
                    p.sendMessage("blinded");

                    if(team.hasPlayer(p)) {
                        team.removePlayer(p);
                        p.sendMessage("removing" + p.getDisplayName() + "from team");
                    }
                }

                if (!LightLevelEvent.currentlyBlinded.contains(p)) {
                    p.sendMessage("vision");

                    if(!team.hasPlayer(p)) {
                        team.addPlayer(p);
                        p.sendMessage("adding" + p.getDisplayName() + "to team");
                    }

                }
                p.setScoreboard(board);


            }
        }, 0, 10);

    }
}