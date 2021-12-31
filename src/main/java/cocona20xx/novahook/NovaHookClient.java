package cocona20xx.novahook;

import cocona20xx.novahook.api.OverrideSupplierRegistry;
import cocona20xx.novahook.testing.TestOverrideSupplier;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NovaHookClient implements ClientModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("novahook");
	public static final boolean TESTS_ACTIVE = true;

	@Override
	public void onInitializeClient() {
		if(TESTS_ACTIVE){
			LOGGER.warn("NOVAHOOK TEST MODE IS ACTIVE, DISABLE BEFORE RELEASE!");
			OverrideSupplierRegistry.register(new TestOverrideSupplier(), new Identifier("novahook", "testing_supplier"));
		}
	}
}
