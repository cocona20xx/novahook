package cocona20xx.novahook.api.mixinhelpers.accessor;

import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * @see StandardOverrideAccessor
 */
public interface WithFeatureStateOverrideAccessor extends StandardOverrideAccessor {

    void addOverrideForFeatureState(String keyOfOverride, Identifier defaultId);

    Optional<Identifier> getOverrideFor(String keyOfOverride);

    Optional<Identifier> requestFeatureStateOfKey(String keyOfOverride) throws IllegalStateException;
    void requestAllFeatureKeys() throws IllegalStateException;
}
