package cocona20xx.novahook.api.mixinhelpers.applier;

import cocona20xx.novahook.api.OverrideToken;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

@SuppressWarnings("rawtypes")
public interface WithFeatureStateOverrideApplier extends StandardOverrideApplier {
    void assignOverrideWithKey(String keyOfOverride, Identifier defaultTexture);
    void getOverrideForKey(String keyOfOverride, LivingEntity selfRef, OverrideToken token);
}
