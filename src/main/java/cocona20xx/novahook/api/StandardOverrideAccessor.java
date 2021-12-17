package cocona20xx.novahook.api;

import cocona20xx.novahook.api.requester.OverrideRequestToken;
import cocona20xx.novahook.api.requester.OverrideTextureRequester;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * Interface used to attach Identifier objects to all LivingEntity child class objects, allowing for said Identifiers to be used as texture overrides.
 * @see WithFeatureStateOverrideAccessor
 */
public interface StandardOverrideAccessor {
    void setBase(Identifier idToSet);
    void setEye(Identifier idToSet);
    Optional<Identifier> getBaseOverride();
    Optional<Identifier> getEyeOverride();

    @SuppressWarnings("rawtypes")
    void setRequestInfo(OverrideRequestToken token, LivingEntity selfRef);

    void setRequester(OverrideTextureRequester requester);
    void doBaseRequest() throws IllegalStateException;
    void doEyeLayerRequest() throws IllegalStateException;

    boolean hasRequestInfo();
}
