package galysso.codicraft.loottimer;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LootTimerMain implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");

	@Override
	public void onInitialize() {
		LootTimerMain.LOGGER.info("Initialisation du client de Mon Mod!");
	}
}