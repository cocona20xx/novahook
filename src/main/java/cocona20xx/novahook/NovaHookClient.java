package cocona20xx.novahook;

import cocona20xx.novahook.internal.testing.TestingRequester;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NovaHookClient implements ClientModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("novahook");
	public static final boolean ENABLE_DEV_MODE = true;
	public static TestingRequester requesterTestingOnly;

	@Override
	public void onInitializeClient() {
		if(ENABLE_DEV_MODE){
			LOGGER.warn("NOTICE: Novahook Developer Test Mode Enabled. Remember to disable before release, silly!");
			requesterTestingOnly = new TestingRequester();
		}
	}
}
