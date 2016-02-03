package skygrid8.handlers;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import skygrid8.config.GridBlock;
import skygrid8.config.GridRegistry;
import skygrid8.core.SG_Settings;
import skygrid8.core.SkyGrid8;

public class ConfigHandler
{
	public static Configuration config;
	
	public static void initConfigs()
	{
		if(config == null)
		{
			SkyGrid8.logger.log(Level.ERROR, "Config attempted to be loaded before it was initialised!");
			return;
		}
		
		config.load();
		
		SG_Settings.height = config.getInt("Grid Height", Configuration.CATEGORY_GENERAL, 128, 1, 255, "How high should the grid of blocks be");
		SG_Settings.dist = config.getInt("Grid Spacing", Configuration.CATEGORY_GENERAL, 3, 0, 15, "How much open space should there be between blocks") + 1;
		SG_Settings.populate = config.getBoolean("Natural Populate", Configuration.CATEGORY_GENERAL, false, "Naturally populate the grid with trees and plants");
		SG_Settings.rngSpacing = config.getBoolean("Random Spacing", Configuration.CATEGORY_GENERAL, false, "Randomise the spacing between 1 and the configured value (per chunk)");
		GridRegistry.loadBlocks();
		
		boolean flag = false;
		
		if(config.getCategory(Configuration.CATEGORY_GENERAL).containsKey("Overworld Grid Blocks")) // Legacy config
		{
			String[] tmp = config.getStringList("Overworld Grid Blocks", Configuration.CATEGORY_GENERAL, new String[0], "Which blocks should be present in the grid");
			
			GridRegistry.blockOverworld = new ArrayList<GridBlock>();
			
			for(String s : tmp)
			{
				Block b = Block.getBlockFromName(s);
				
				if(b != null)
				{
					GridRegistry.blockOverworld.add(new GridBlock(b));
				}
			}
			
			config.getCategory(Configuration.CATEGORY_GENERAL).remove("Overworld Grid Blocks");
			flag = true;
		}
		
		if(config.getCategory(Configuration.CATEGORY_GENERAL).containsKey("Nether Grid Blocks")) // Legacy config
		{
			String[] tmp = config.getStringList("Nether Grid Blocks", Configuration.CATEGORY_GENERAL, new String[0], "Which blocks should be present in the grid");
			
			GridRegistry.blocksNether = new ArrayList<GridBlock>();
			
			for(String s : tmp)
			{
				Block b = Block.getBlockFromName(s);
				
				if(b != null)
				{
					GridRegistry.blocksNether.add(new GridBlock(b));
				}
			}
			
			config.getCategory(Configuration.CATEGORY_GENERAL).remove("Nether Grid Blocks");
			flag = true;
		}
		
		if(config.getCategory(Configuration.CATEGORY_GENERAL).containsKey("Crops")) // Legacy config
		{
			config.getCategory(Configuration.CATEGORY_GENERAL).remove("Crops");
		}
		
		if(flag)
		{
			GridRegistry.saveBlocks();
		}
		
		config.save();
		
		SkyGrid8.logger.log(Level.INFO, "Loaded configs...");
	}
}
