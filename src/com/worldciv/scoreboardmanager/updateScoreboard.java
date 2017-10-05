package com.worldciv.scoreboardmanager;

import com.worldciv.devteam.main;
import com.worldciv.events.player.lightLevelEvent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import static com.worldciv.scoreboardmanager.setScoreboard.*;
import static com.worldciv.scoreboardmanager.updateVision.*;

public class updateScoreboard {

    public static void updateScoreboard(Player x, Objective obj, Team healthteam, Team torchteam, Team blankscoreofficial, Team newsteam) {

                //Variables that need to init every time to update.
                //long health = Math.round(x.getHealth());


                // obj.setDisplayName("World Civilization"); //idea for future: create animated title (look down for updateSidebarTitle). create a function to make things more neat.

                updateVisionData(x); //has to be called before checking if you are blind or not. checkmark or x.
                lightLevelEvent.updateLightLevelEvent(x);

                //blankscore


                Score score = obj.getScore(ChatColor.AQUA + "IGN:");
                score.setScore(9);

                Score score1 = obj.getScore(ChatColor.GRAY + x.getName());
                score1.setScore(8);

                //blankscore2 | line 7

                Score score2 = obj.getScore(ChatColor.AQUA + "News:");
                score2.setScore(6);

                //CHECK ANIMATION SCOREBOARD FOR NEWS

                //blankscore3 | line 4

                // SCORE TO HAVE ✓ OR ✗ MARK

                if (x.getGameMode() == GameMode.CREATIVE || toggleblind.contains(x)) {
                    torchteam.setPrefix(ChatColor.YELLOW + "VISION BYPASS");
                    torchteam.setSuffix(ChatColor.RESET + "");
                } else {

                    if (visionteam.contains(x)) {
                        torchteam.setPrefix(ChatColor.YELLOW + "Vision [V]:");
                        torchteam.setSuffix(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + " ✓");

                    } else if (!visionteam.contains(x)) {
                        torchteam.setPrefix(ChatColor.YELLOW + "Vision [V]:");
                        torchteam.setSuffix(ChatColor.RED + " ✗");
                    }
                }





                //update every tick


    }
}
