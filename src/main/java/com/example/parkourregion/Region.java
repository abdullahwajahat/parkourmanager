package com.example.parkourregion;
public class Region {
private final String name;
private final String worldName;
private final int x1, y1, z1, x2, y2, z2;
private Location start;
private Location finish;
private Integer failY = null; // nullable
private final List<Location> checkpoints = new ArrayList<>();
private final List<String> finishCommands = new ArrayList<>();
private String finishMessageType = null; // "title"/"subtitle"/"chat"
private String finishMessage = null; // content if chat or title/subtitle
private String finishSound = null;
private List<String> blacklist = new ArrayList<>();
private long cooldownMs = 0;


public Region(String name, Location a, Location b) {
this.name = name;
World w = a.getWorld();
this.worldName = w.getName();
this.x1 = Math.min(a.getBlockX(), b.getBlockX());
this.y1 = Math.min(a.getBlockY(), b.getBlockY());
this.z1 = Math.min(a.getBlockZ(), b.getBlockZ());
this.x2 = Math.max(a.getBlockX(), b.getBlockX());
this.y2 = Math.max(a.getBlockY(), b.getBlockY());
this.z2 = Math.max(a.getBlockZ(), b.getBlockZ());
}


public String getName() { return name; }
public String getWorldName() { return worldName; }
public int[] getMin() { return new int[]{x1,y1,z1}; }
public int[] getMax() { return new int[]{x2,y2,z2}; }


public boolean contains(Location loc) {
if (loc == null) return false;
if (!loc.getWorld().getName().equals(worldName)) return false;
int x = loc.getBlockX(); int y = loc.getBlockY(); int z = loc.getBlockZ();
return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
}


// getters/setters for start/finish/checkpoints
public void setStart(Location l) { this.start = l; }
public Location getStart() { return start; }
public void setFinish(Location l) { this.finish = l; }
public Location getFinish() { return finish; }
public void setFailY(Integer y) { this.failY = y; }
public Integer getFailY() { return failY; }
public List<Location> getCheckpoints() { return checkpoints; }
public List<String> getFinishCommands() { return finishCommands; }
public void setFinishMessageType(String t) { this.finishMessageType = t; }
public String getFinishMessageType() { return finishMessageType; }
public void setFinishMessage(String m) { this.finishMessage = m; }
public String getFinishMessage() { return finishMessage; }
public void setFinishSound(String s) { this.finishSound = s; }
public String getFinishSound() { return finishSound; }
public List<String> getBlacklist() { return blacklist; }
public void setCooldownMs(long ms) { this.cooldownMs = ms; }
public long getCooldownMs() { return cooldownMs; }
}
