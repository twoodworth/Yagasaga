package me.tedsworth.yagasaga;

import com.google.common.base.Strings;
import com.mysql.fabric.xmlrpc.base.Array;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Yagasaga extends JavaPlugin {


    List<String> reasons = new ArrayList<>();

    public Yagasaga() {
        reasons.add("glitching");
        reasons.add("cheating");
        reasons.add("advertising");
        reasons.add("spamming");
        reasons.add("hacking");
        reasons.add("language");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        /* chat
         * cheating
         * /report (player) (reason) (description)
         */

        if (command.getName().equals("report")) {
            if (args.length < 3) {
                return false;
            }

            String playerName = args[0];

            String reason = args[1].toLowerCase();

            String[] descriptionArgs = Arrays.copyOfRange(args, 2, args.length);
            String description = String.join(" ", descriptionArgs);

            @SuppressWarnings("deprecation")
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
            if (!player.hasPlayedBefore()) {
                sender.sendMessage("Player " + playerName + " could not be found.");
                return true;
            }

            if (!reasons.contains(reason)) {
                sender.sendMessage("invalid reason");
                return false;
            }
            try {
                saveReport(sender, player, reason, description);
            } catch (IOException e) {
                e.printStackTrace();
                sender.sendMessage("There was an error in sending your report.");
                return true;
            }
            sender.sendMessage("Your report has been successfully sent in!");
            return true;
        } if (command.getName().equals("vote")) {
            //language=JSON
            String json = "[" +
                    "\"Sites: \"," +
                    "{" +
                    "    \"text\":\"Minecraft-MP \"," +
                    "    \"clickEvent\":{" +
                    "        \"action\":\"open_url\"," +
                    "        \"value\":\"https://minecraft-mp.com/server/211805/vote/\"" +
                    "    }," +
                    "    \"color\":\"red\"" +
                    "}," +
                    "{" +
                    "    \"text\":\"MCS.org \"," +
                    "    \"clickEvent\":{" +
                    "        \"action\":\"open_url\"," +
                    "        \"value\":\"https://minecraftservers.org/vote/528285\"" +
                    "    }," +
                    "    \"color\":\"aqua\"" +
                    "}," +
                    "{" +
                    "    \"text\":\"TopG \"," +
                    "    \"clickEvent\":{" +
                    "        \"action\":\"open_url\"," +
                    "        \"value\":\"https://topg.org/Minecraft/in-504771\"" +
                    "    }," +
                    "    \"color\":\"yellow\"" +
                    "}," +
                    "{" +
                    "    \"text\":\"MCS List \"," +
                    "    \"clickEvent\":{" +
                    "        \"action\":\"open_url\"," +
                    "        \"value\":\"https://minecraft-server-list.com/server/434654/vote/\"" +
                    "    }," +
                    "    \"color\":\"green\"" +
                    "}," +
                    "{" +
                    "    \"text\":\"MCS.net \"," +
                    "    \"clickEvent\":{" +
                    "        \"action\":\"open_url\"," +
                    "        \"value\":\"https://minecraft-server.net/vote/Yagasaga/\"" +
                    "    }," +
                    "    \"color\":\"blue\"" +
                    "}," +
                    "{" +
                    "    \"text\":\"MCS.biz \"," +
                    "    \"clickEvent\":{" +
                    "        \"action\":\"open_url\"," +
                    "        \"value\":\"https://minecraftservers.biz/servers/143064/vote/\"" +
                    "    }," +
                    "    \"color\":\"gray\"" +
                    "}," +
                    "{" +
                    "    \"text\":\"\\nTrackyServer \"," +
                    "    \"clickEvent\":{" +
                    "        \"action\":\"open_url\"," +
                    "        \"value\":\"https://www.trackyserver.com/server/yagasaga-192027\"" +
                    "    }," +
                    "    \"color\":\"gold\"" +
                    "}," +
                    "{" +
                    "   \"text\":\"MC List\"," +
                    "   \"clickEvent\":{" +
                    "       \"action\":\"open_url\"," +
                    "       \"value\":\"https://minecraftlist.org/vote/11319\"" +
                    "   }," +
                    "    \"color\":\"dark_red\"" +
                    "}" +
                    "]";

            Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    "tellraw " + sender.getName() + " " + json
            );
            return true;
        } else if (command.getName().equals("discord")) {
            //language=JSON
            String json = "[" +
                    "\"Link: \"," +
                    "{" +
                    "    \"text\":\"Click Here \"," +
                    "    \"clickEvent\":{" +
                    "        \"action\":\"open_url\"," +
                    "        \"value\":\"https://discord.gg/8CSdgx9\"" +
                    "    }," +
                    "    \"color\":\"light_purple\"" +
                    "}" +
                    "]";

            Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    "tellraw " + sender.getName() + " " + json
            );
            return true;
        }
        return false;
    }

    public void saveReport(CommandSender reporter, OfflinePlayer player, String reason, String description) throws IOException {
        File reportFile = new File(this.getDataFolder(), "reports.txt");

        reportFile.getParentFile().mkdirs();
        boolean isnewfile = reportFile.createNewFile();
        String date = getCurrentTimeUsingDate();

        String reporterID = "";
        if (reporter instanceof Player) {
            reporterID = ((Player) reporter).getUniqueId().toString();
        }


        try (FileWriter writer = new FileWriter(reportFile, true)) {
            if (isnewfile) {
                writer.append("Date\tReporter\tReporter UUID\tPlayer\tPlayer UUID\tReason\tDescription\n");
            }
            writer.append(date).append("\t")
                    .append(reporter.getName()).append("\t")
                    .append(reporterID).append("\t")
                    .append(String.valueOf(player.getName())).append("\t")
                    .append(String.valueOf(player.getUniqueId())).append("\t")
                    .append(reason).append("\t")
                    .append(description).append("\n");
        }
    }

    public static String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "YYYY/MM/DD hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        return dateFormat.format(date);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }
}
