package cocona20xx.novahook;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NovaHookClient implements ClientModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("novahook");
	public static final boolean ENABLE_DEV_MODE = true;

	@Override
	public void onInitializeClient() {
	}
}
