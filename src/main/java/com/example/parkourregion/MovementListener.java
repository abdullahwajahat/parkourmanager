package com.example.parkourregion;
}
// fail sound and actionbar
if (plugin.getConfig().getBoolean("messages.fail.enabled", true)) {
String snd = plugin.getConfig().getString("messages.fail.sound", "ENTITY_ENDERMAN_TELEPORT");
try { p.playSound(p.getLocation(), Sound.valueOf(snd), 1f, 1f); } catch (Exception ignored) {}
}
String failMsg = plugin.getConfig().getString("messages.fail.message", "&c&lYou Fell!");
p.sendActionBar(Utils.color(failMsg));
}


private void runFinish(Player p, Region r) {
// show message/sound then run commands as console
showFinishMessageAndSound(p, r);
for (String cmd : r.getFinishCommands()) {
String processed = cmd.replace("%player%", p.getName());
// handle tp without world: tp player x y z -> insert world
String[] parts = processed.split(" ");
if (parts.length >= 4 && parts[0].equalsIgnoreCase("tp")) {
if (parts.length == 4) {
// tp <player> x y z -> not expected as processed has player replaced, we expect: tp Player x y z
// Insert player's world after player
processed = "tp " + p.getName() + " " + p.getWorld().getName() + " " + parts[1] + " " + parts[2] + " " + parts[3];
} else if (parts.length == 5) {
// maybe tp player world x y z -> ok
}
}
Bukkit.dispatchCommand(Bukkit.getConsoleSender(), processed);
}
}


private void showFinishMessageAndSound(Player p, Region r) {
// priority: title > subtitle > chat (we stored messageType and message)
String type = r.getFinishMessageType();
String msg = r.getFinishMessage();
if (type != null && msg != null) {
String out = Utils.color(msg.replace("%player%", p.getName()));
if (type.equalsIgnoreCase("title")) {
p.sendTitle(out, "", 10, 70, 20);
} else if (type.equalsIgnoreCase("subtitle")) {
p.sendTitle("", out, 10, 70, 20);
} else {
p.sendMessage(out);
}
} else {
// fallback to messages.yml defaults
List<String> fin = plugin.getConfig().getStringList("messages.finish.messages");
if (!fin.isEmpty()) {
String choose = fin.get((int)(Math.random()*fin.size()));
p.sendMessage(Utils.color(choose.replace("%player%", p.getName())));
}
}
String snd = r.getFinishSound();
if (snd == null) snd = plugin.getConfig().getString("messages.finish.sound", "ENTITY_PLAYER_LEVELUP");
if (snd != null && !snd.equalsIgnoreCase("none")) {
try { p.playSound(p.getLocation(), Sound.valueOf(snd), 1f, 1f); } catch (Exception ignored) {}
}
}
}
