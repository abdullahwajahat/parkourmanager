package com.example.parkourregion;
regions.put(key.toLowerCase(Locale.ROOT), r);
}
}


public void save() {
if (cfg == null) cfg = YamlConfiguration.loadConfiguration(file);
cfg.set("regions", null);
for (Region r : regions.values()) {
String path = "regions." + r.getName();
int[] min = r.getMin(); int[] max = r.getMax();
cfg.set(path + ".world", r.getWorldName());
cfg.set(path + ".x1", min[0]); cfg.set(path + ".y1", min[1]); cfg.set(path + ".z1", min[2]);
cfg.set(path + ".x2", max[0]); cfg.set(path + ".y2", max[1]); cfg.set(path + ".z2", max[2]);
if (r.getStart() != null) cfg.set(path + ".start", Arrays.asList(r.getStart().getBlockX(), r.getStart().getBlockY(), r.getStart().getBlockZ()));
if (r.getFinish() != null) cfg.set(path + ".finish", Arrays.asList(r.getFinish().getBlockX(), r.getFinish().getBlockY(), r.getFinish().getBlockZ()));
if (r.getFailY() != null) cfg.set(path + ".failY", r.getFailY());
List<String> cps = new ArrayList<>();
for (Location c : r.getCheckpoints()) cps.add(c.getBlockX() + "," + c.getBlockY() + "," + c.getBlockZ());
cfg.set(path + ".checkpoints", cps);
cfg.set(path + ".finish.commands", r.getFinishCommands());
cfg.set(path + ".finish.messageType", r.getFinishMessageType());
cfg.set(path + ".finish.message", r.getFinishMessage());
cfg.set(path + ".finish.sound", r.getFinishSound());
cfg.set(path + ".blacklist", r.getBlacklist());
cfg.set(path + ".cooldown", r.getCooldownMs() == 0 ? "0" : (r.getCooldownMs() + "ms"));
}
try { cfg.save(file); } catch (IOException e) { plugin.getLogger().warning("Could not save regions.yml: " + e.getMessage()); }
}


public Collection<Region> getRegions() { return regions.values(); }
public Region getRegion(String name) { return regions.get(name.toLowerCase(Locale.ROOT)); }
public boolean addRegion(Region r) {
if (regions.containsKey(r.getName().toLowerCase(Locale.ROOT))) return false;
regions.put(r.getName().toLowerCase(Locale.ROOT), r);
save();
return true;
}
public boolean removeRegion(String name) {
if (!regions.containsKey(name.toLowerCase(Locale.ROOT))) return false;
regions.remove(name.toLowerCase(Locale.ROOT));
save();
return true;
}


public static long parseDuration(String s) {
if (s == null) return 0;
if (s.equals("0")) return 0;
s = s.toLowerCase().trim();
try {
if (s.endsWith("ms")) return Long.parseLong(s.replace("ms",""));
if (s.endsWith("s")) return Long.parseLong(s.replace("s","")) * 1000L;
if (s.endsWith("m")) return Long.parseLong(s.replace("m","")) * 60_000L;
if (s.endsWith("h")) return Long.parseLong(s.replace("h","")) * 3_600_000L;
} catch (NumberFormatException ignored) {}
return 0;
}
}
