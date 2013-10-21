package me.parapenguin.overcast.scrimmage.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class RegionUtil {
	
	public static List<Block> circle(Block block, double r, double h, boolean hollow, boolean sphere) {
		List<Location> locs = circle(true, block.getLocation(), r, h, hollow, sphere);
		List<Block> blocks = new ArrayList<Block>();
		
		for(Location loc : locs)
			blocks.add(loc.getBlock());
		
		return blocks;
	}
	
	public static List<Location> circle(Location loc, double r, double h, boolean hollow, boolean sphere) {
		return circle(false, loc, r, h, hollow, sphere);
	}

	public static List<Location> circle(boolean block, Location loc, double r, double h, Boolean hollow, Boolean sphere) {
		List<Location> circleblocks = new ArrayList<Location>();
		double cx = loc.getBlockX();
		double cy = loc.getBlockY();
		double cz = loc.getBlockZ();
		
		if(block) {
			cx = cx + 0.5D;
			cy = cy + 0.5D;
			cz = cz + 0.5D;
		}
		
		for (double x = cx - r; x <= cx + r; x++) {
			for (double z = cz - r; z <= cz + r; z++) {
				for (double y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y++) {
					double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
					if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
						Location l = new Location(loc.getWorld(), x, y, z);
						circleblocks.add(l);
					}
				}
			}
		}

		return circleblocks;
	}
	
	public static List<Block> square(Block block, int radius, boolean hollow) {
		Location centre = block.getLocation();
		
		List<Location> locs = square(true, centre, radius, hollow);
		List<Block> blocks = new ArrayList<Block>();
		
		for(Location loc : locs)
			blocks.add(loc.getBlock());
		
		return blocks;
	}
	
	public static List<Location> square(Location centre, int radius, boolean hollow) {
		return square(false, centre, radius, hollow);
	}
	
	public static List<Location> square(boolean block, Location centre, int radius, boolean hollow) {
		List<Location> locs = new ArrayList<Location>();
		
		Location corner1 = new Location(centre.getWorld(), centre.getX() + radius, centre.getY(), centre.getY() + radius);
		Location corner2 = new Location(centre.getWorld(), centre.getX() - radius, centre.getY(), centre.getY() - radius);
		if(block) {
			corner1 = new Location(centre.getWorld(), centre.getBlockX() + radius, centre.getBlockY(), centre.getBlockY() + radius);
			corner2 = new Location(centre.getWorld(), centre.getBlockX() - radius, centre.getBlockY(), centre.getBlockY() - radius);
		}
		
		double xMin, xMax, yPos, zMin, zMax = 0;

		xMin = Math.min(corner1.getBlockX(), corner2.getBlockX());
		xMax = Math.max(corner1.getBlockX(), corner2.getBlockX());
		yPos = Math.min(corner1.getBlockY(), corner2.getBlockY());
		zMin = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
		zMax = Math.max(corner1.getBlockZ(), corner2.getBlockZ());
		
		double x = xMin;
		double y = yPos;
		double z = zMin;
		
		if(!hollow) {
			while(x <= xMax) {
				while(z <= zMax) {
					locs.add(new Location(centre.getWorld(), x, y, z));
					z = z + 1;
				}
				x = x + 1;
				z = zMin;
			}
		} else {
			while(x <= xMax) {
				locs.add(new Location(centre.getWorld(), x, y, z));
				x = x + 1;
			}
			
			while(z <= zMax) {
				locs.add(new Location(centre.getWorld(), x, y, z));
				z = z + 1;
			}
			
			while(x <= xMin) {
				locs.add(new Location(centre.getWorld(), x, y, z));
				x = x - 1;
			}
			
			while(z <= zMin) {
				locs.add(new Location(centre.getWorld(), x, y, z));
				z = z - 1;
			}
		}
		
		return locs;
	}
	
	public static Boolean isInside(Location loc, Location corner1, Location corner2) {
		double xMin, xMax, yMin, yMax, zMin, zMax = 0;
		
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();

		xMin = Math.min(corner1.getX(), corner2.getX());
		xMax = Math.max(corner1.getX(), corner2.getX());
		yMin = Math.min(corner1.getY(), corner2.getY());
		yMax = Math.max(corner1.getY(), corner2.getY());
		zMin = Math.min(corner1.getZ(), corner2.getZ());
		zMax = Math.max(corner1.getZ(), corner2.getZ());

		return (x >= xMin && x <= xMax && y >= yMin && y <= yMax && z >= zMin && z <= zMax);
	}

	public static List<Block> contains(Location corner1, Location corner2){
		List<Block> blocks = new ArrayList<Block>();
		
		int xMin, xMax, yMin, yMax, zMin, zMax = 0;
		xMin = Math.min(corner1.getBlockX(), corner2.getBlockX());
		xMax = Math.max(corner1.getBlockX(), corner2.getBlockX());
		yMin = Math.min(corner1.getBlockY(), corner2.getBlockY());
		yMax = Math.max(corner1.getBlockY(), corner2.getBlockY());
		zMin = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
		zMax = Math.max(corner1.getBlockZ(), corner2.getBlockZ());
		
		World world = corner1.getWorld();
		
		int px = xMin;
		while (px <= xMax) {
			int py = yMin;
			while (py <= yMax) {
				int pz = zMin;
				while (pz <= zMax) {
					blocks.add(world.getBlockAt(new Location(world, px, py, pz)));
					pz++;
				}
				py++;
			}
			px++;
		}
		
		return blocks;
	}

}