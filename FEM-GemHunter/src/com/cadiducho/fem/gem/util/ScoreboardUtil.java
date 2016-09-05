package com.cadiducho.fem.gem.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class ScoreboardUtil {

    private final Scoreboard scoreboard;

    private final Objective objective;

    private boolean reset;

    private final HashMap<Integer, String> scores;

    public ScoreboardUtil(String displayName, String score) {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = this.scoreboard.registerNewObjective(score, "dummy");
        this.objective.setDisplayName(displayName);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.scores = new HashMap<>();
    }

    public void setName(String name) {
        this.objective.setDisplayName(name);
    }

    public void text(int score, String text) {
        if (scores.containsKey(score)) {
            String textX = scores.get(score);
            if (!(text.equalsIgnoreCase(textX))) {
                scoreboard.resetScores(textX);
                objective.getScore(text).setScore(score);
                scores.put(score, text);
            }
        } else {
            scores.put(score, text);
            objective.getScore(text).setScore(score);
        }
    }

    public void team(String name, String preffix) {
        Team team = scoreboard.getTeam(name);
        if (team == null) {
            scoreboard.registerNewTeam(name);
            scoreboard.getTeam(name).setPrefix(preffix);
        }
    }

    public void reset() {
        if (!(isReset())) {
            for (int x : scores.keySet()) {
                getScoreboard().resetScores(scores.get(x));
            }
            scores.clear();
            setReset(true);
        }
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public boolean isReset() {
        return reset;
    }

    public void build(Player player) {
        player.setScoreboard(scoreboard);
    }

    public Team getTeam(String name) {
        return scoreboard.getTeam(name);
    }
}
