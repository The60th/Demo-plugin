package com.worldciv.events.player;

import com.worldciv.the60th.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.*;

public class scoreboard implements Listener { //Do I have to explain?

    //Declaring variables for a dummyboard. This board will be used to ONLY STORE DATA on a SECOND BOARD.
    ScoreboardManager dummymanager = Bukkit.getScoreboardManager();
    Scoreboard dummyboard = dummymanager.getNewScoreboard();
    Team dummyteam = dummyboard.registerNewTeam("dummy vision");

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

        if (event.getMessage().substring(0).equals("/tab") && !event.getPlayer().isOp() && !(event.getPlayer() instanceof ConsoleCommandSender)) {
            event.getPlayer().sendMessage("Unknown command. Type \"/help\" for help.");
            event.setCancelled(true);
        }
        if ((event.getMessage().substring(0).equals("/pl") || event.getMessage().substring(0).equals("/help") || event.getMessage().substring(0).equals("/?"))  && !event.getPlayer().isOp()) {
            event.getPlayer().sendMessage(ChatColor.GOLD + "[World-Civ]" + ChatColor.GRAY + " Not allowed to use this special command.");
            event.setCancelled(true);
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer(); //player triggering event

       Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {  //LOOP
            public void run() {

                //REFRESHING SCOREBOARD
                ScoreboardManager manager = Bukkit.getScoreboardManager();   //MAKING SCOREBOARD
                Scoreboard board = manager.getNewScoreboard(); //GETTING SCOREBOARD
                Objective objective = board.registerNewObjective("WorldCiv", "dummy"); //CREATING SIDEBAR INDIVIDUALLY PER PLAYER

                ////// below is to add/remove blind from prefix ////

                if (LightLevelEvent.currentlyBlinded.contains(p)) { //if ur light level is low with no light

                    if(dummyteam.hasPlayer(p)) { //if u r in torch dummyteam
                        dummyteam.removePlayer(p); // remove from dummyteam
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + p.getName() + " group remove Torch");
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab reload");
                        p.sendMessage("removing" + p.getDisplayName() + "from team");
                        p.sendMessage("blinded");
                    }
                }

                if (!LightLevelEvent.currentlyBlinded.contains(p)) { //if ur ilght level is high, u can see
                    if(!dummyteam.hasPlayer(p)) { //if ur not on torch team now u r
                        dummyteam.addPlayer(p);
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + p.getName() + " group add Torch");
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tab reload");
                        p.sendMessage("adding" + p.getDisplayName() + "to team");
                        p.sendMessage("vision");
                    }
                }



                //SCOREBOARD SCORES
                Score blankscore = objective.getScore("🔦🔦🔦🔦");   //EMPTY LINES OF SCORE
                Score blankscore2 = objective.getScore("🔦🔦🔦");
                Score blankscore3 = objective.getScore("🔦🔦");
                Score blankscore4 = objective.getScore("🔦");

                objective.setDisplaySlot(DisplaySlot.SIDEBAR);   //DISPLAY ON SIDEBAR @OBJECTIVE

                objective.setDisplayName(ChatColor.GOLD + "World Civilization");  //NAME OF OBJECTIVE


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

                // SCORE TO HAVE ✓ OR ✗ MARK

                if(dummyteam.hasPlayer(p)) {
                    Score score5 = objective.getScore(ChatColor.RED + "Torch [T]:" + ChatColor.BLUE + " ✓");
                    score5.setScore(3);

                }else if(!dummyteam.hasPlayer(p)) {
                    Score score5 = objective.getScore(ChatColor.RED + "Torch [T]:" + ChatColor.BLUE + " ✗");
                    score5.setScore(3);

                }
                p.setScoreboard(board);

            }
        }, 0, 1);



    }


}
