package cocona20xx.novahook.api;

import cocona20xx.novahook.api.requester.OverrideRequestToken;
import net.minecraft.entity.LivingEntity;

@SuppressWarnings("rawtypes")
public interface StandardOverrideApplier {
    void checkBaseOverride(LivingEntity selfRef, OverrideRequestToken token);
    void checkEyeLayerOverride(LivingEntity selfRef, OverrideRequestToken token);
    boolean doLimitCheck();
}
