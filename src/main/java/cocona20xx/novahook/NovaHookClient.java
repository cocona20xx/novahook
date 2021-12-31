package cocona20xx.novahook;

import cocona20xx.novahook.api.OverrideSupplierRegistry;
import cocona20xx.novahook.testing.TestOverrideSupplier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NovaHookClient implements ClientModInitializer {

	public static final Logger LOGGER = LogManager.getLogger("novahook");
	public static final boolean TESTS_ACTIVE = false;
	public static final String VERSION_STRING = "1.0.0";

	@Override
	public void onInitializeClient() {
		if(TESTS_ACTIVE){
			LOGGER.warn("NOVAHOOK TEST MODE IS ACTIVE, DISABLE BEFORE RELEASE!");
			OverrideSupplierRegistry.register(new TestOverrideSupplier(), new Identifier("novahook", "testing_supplier"));
		}
		LOGGER.info("NovaHook version: " + VERSION_STRING + " initialized! Loaded " + Integer.toString(OverrideSupplierRegistry.supplierCount()) + " Override Suppliers.");
	}
}
