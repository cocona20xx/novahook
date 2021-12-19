package cocona20xx.novahook.api.mixinhelpers.applier;

import cocona20xx.novahook.api.OverrideToken;
import net.minecraft.entity.LivingEntity;

@SuppressWarnings("rawtypes")
public interface StandardOverrideApplier {
    void checkBaseOverride(LivingEntity selfRef, OverrideToken token);
    void checkEyeLayerOverride(LivingEntity selfRef, OverrideToken token);
    boolean doLimitCheck();
}
