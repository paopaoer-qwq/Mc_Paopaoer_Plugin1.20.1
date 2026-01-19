package org.example.paopaoerPlugin1201;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PaopaoerPlugin extends JavaPlugin {

    // 插件启动时执行（带图形的Logo + 启动整点报时任务）
    @Override
    public void onEnable() {
        // 输出图形Logo
        String logo =
                "  ╔══════════════════════════════════════════╗\n"
                        + "  ║          ██████╗ █████╗ ██████╗          ║\n"
                        + "  ║          ██╔══██╗██╔══██╗██╔══██╗         ║\n"
                        + "  ║          ██████╔╝███████║PAOPAOER║        ║\n"
                        + "  ║          ██╔═══╝ ██╔══██║██╔══██╗         ║\n"
                        + "  ║          ██║     ██║  ██║██║  ██║         ║\n"
                        + "  ║          ╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝         ║\n"
                        + "  ║  ┌──────────────────────────────────┐   ║\n"
                        + "  ║  │  🚀 破土豆服务器插件 · PAOPAOER  🚀  │   ║\n"
                        + "  ║  └──────────────────────────────────┘   ║\n"
                        + "  ╚══════════════════════════════════════════╝\n"
                        + "===============================================\n"
                        + "PaopaoerPlugin 已成功加载！\n"
                        + "Spigot/Paper 1.20.1\n"
                        + "核心指令：/paopaoerzuishuaile（烟花特效）\n"
                        + "工具指令：/paopaoer time（查看当前时间）\n"
                        + "整点报时功能已启用！\n"
                        + "===============================================";

        getLogger().info(logo);

        // 启动整点报时定时任务
        startHourlyAnnouncementTask();
    }

    // 插件停止时执行
    @Override
    public void onDisable() {
        getLogger().info("  ╔══════════════════════════════════════════╗");
        getLogger().info("  ║          PaopaoerPlugin 下次再见～        ║");
        getLogger().info("  ╚══════════════════════════════════════════╝");
    }

    // 统一指令处理逻辑（新增/paopaoer主指令+子指令解析）
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // 处理原有指令 /paopaoerzuishuaile
        if (cmd.getName().equalsIgnoreCase("paopaoerzuishuaile")) {
            handleFireworkCommand(sender);
            return true;
        }

        // 处理新增主指令 /paopaoer
        if (cmd.getName().equalsIgnoreCase("paopaoer")) {
            // 解析子指令
            if (args.length == 0) {
                // 无参数：显示指令帮助
                sendCommandHelp(sender);
                return true;
            }

            // 匹配子指令 time
            String subCommand = args[0].toLowerCase();
            switch (subCommand) {
                case "time":
                    handleTimeCommand(sender);
                    break;
                default:
                    // 未知子指令：提示帮助
                    sender.sendMessage("§c未知子指令！输入 /paopaoer 查看可用指令。");
                    break;
            }
            return true;
        }

        return false;
    }

    /**
     * 处理 /paopaoerzuishuaile 指令（原有烟花特效逻辑）
     */
    private void handleFireworkCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String msg = "§a✨ 破土豆服务器专属彩蛋 ✨\n"
                    + "§6🎉 恭喜触发 /paopaoerzuishuaile 指令！\n";
            player.sendMessage(msg);

            // 播放音效
            player.playSound(player.getLocation(), "entity.firework_rocket.launch", 1.0f, 1.0f);
            // 生成烟花
            spawnCustomFirework(player.getLocation());

        } else {
            getLogger().info("===============================================");
            getLogger().info("控制台触发了指令：/paopaoerzuishuaile");
            getLogger().info("paopaoerzuishuaile～");
            getLogger().info("===============================================");
        }
    }

    /**
     * 处理 /paopaoer time 子指令（查看当前现实时间）
     */
    private void handleTimeCommand(CommandSender sender) {
        // 获取当前现实时间并格式化
        LocalTime now = LocalTime.now();
        // 格式化：HH:mm:ss（24小时制） + 友好中文格式
        String time24 = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String timeFriendly = formatHourlyTime(now.getHour()) + now.getMinute() + "分" + now.getSecond() + "秒";

        // 构建提示消息
        String msg = "§e[服务器时间] §6当前现实时间：\n"
                + "§724小时制：§f" + time24 + "\n"
                + "§7友好格式：§f" + timeFriendly;

        // 区分玩家/控制台输出
        if (sender instanceof Player) {
            sender.sendMessage(msg);
        } else {
            getLogger().info("【当前时间查询】24小时制：" + time24 + " | 友好格式：" + timeFriendly);
        }
    }

    /**
     * 发送 /paopaoer 指令帮助信息
     */
    private void sendCommandHelp(CommandSender sender) {
        String helpMsg = "§e===== PAOPAOER 插件指令帮助 =====\n"
                + "§6/paopaoer time §7- 查看当前现实时间\n"
                + "§6/paopaoerzuishuaile §7- 触发烟花特效彩蛋\n"
                + "§e================================";

        if (sender instanceof Player) {
            sender.sendMessage(helpMsg);
        } else {
            getLogger().info("===== PAOPAOER 插件指令帮助 =====");
            getLogger().info("/paopaoer time - 查看当前现实时间");
            getLogger().info("/paopaoerzuishuaile - 触发烟花特效彩蛋");
            getLogger().info("================================");
        }
    }

    /**
     * 启动整点报时定时任务
     */
    private void startHourlyAnnouncementTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                LocalTime now = LocalTime.now();
                if (now.getMinute() == 0 && now.getSecond() == 0) {
                    String timeStr = formatHourlyTime(now.getHour());
                    String announceMsg = "§e[服务器报时] §6当前时间：" + timeStr + "整！";

                    for (Player player : getServer().getOnlinePlayers()) {
                        player.sendMessage(announceMsg);
                        player.playSound(player.getLocation(), "block.bell.use", 1.0f, 1.0f);
                        spawnSmallFirework(player.getLocation());
                    }

                    getLogger().info("【整点报时】当前现实时间：" + now.format(DateTimeFormatter.ofPattern("HH:mm")));
                }
            }
        }.runTaskTimer(this, 0L, 20L);
    }

    /**
     * 格式化小时为友好的中文提示
     */
    private String formatHourlyTime(int hour) {
        String period;
        int displayHour;

        if (hour == 0) {
            period = "凌晨";
            displayHour = 0;
        } else if (hour >= 1 && hour < 6) {
            period = "凌晨";
            displayHour = hour;
        } else if (hour >= 6 && hour < 12) {
            period = "上午";
            displayHour = hour;
        } else if (hour == 12) {
            period = "中午";
            displayHour = 12;
        } else if (hour > 12 && hour < 18) {
            period = "下午";
            displayHour = hour - 12;
        } else {
            period = "晚上";
            displayHour = hour - 12;
        }

        return period + displayHour + "点";
    }

    /**
     * 生成自定义彩色烟花（指令触发用）- 修复位置污染
     */
    private void spawnCustomFirework(Location location) {
        // 关键修复：克隆位置后再偏移，不修改原位置对象
        Location fireworkLoc = location.clone().add(0, 1, 0);
        Firework firework = fireworkLoc.getWorld().spawn(fireworkLoc, Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        FireworkEffect effect = FireworkEffect.builder()
                .with(FireworkEffect.Type.STAR)
                .withColor(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
                .withFade(Color.WHITE)
                .withFlicker()
                .withTrail()
                .build();

        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(2);
        firework.setFireworkMeta(fireworkMeta);
        firework.detonate();
    }

    /**
     * 生成整点报时的小烟花 - 修复位置污染
     */
    private void spawnSmallFirework(Location location) {
        // 关键修复：克隆位置后再偏移，不修改原位置对象
        Location fireworkLoc = location.clone().add(0, 2, 0);
        Firework firework = fireworkLoc.getWorld().spawn(fireworkLoc, Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        Color goldColor = Color.fromRGB(255, 215, 0);
        Color yellowFade = Color.fromRGB(255, 255, 0);

        FireworkEffect effect = FireworkEffect.builder()
                .with(FireworkEffect.Type.BALL)
                .withColor(goldColor)
                .withFade(yellowFade)
                .build();

        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(1);
        firework.setFireworkMeta(fireworkMeta);
        firework.detonate();
    }
}