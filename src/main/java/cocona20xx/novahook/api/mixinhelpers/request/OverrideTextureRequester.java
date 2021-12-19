package cocona20xx.novahook.api.mixinhelpers.request;

import cocona20xx.novahook.api.OverrideToken;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * Defines the base-level structure of Entity Texture Override Requesters.
 *<br> Most child classes will likely want to extend an abstract child class provided by the API instead.
 */
@SuppressWarnings("rawtypes")
public interface OverrideTextureRequester {

    Optional<Identifier> requestBaseTextureOverride(OverrideToken token, LivingEntity entity);
    Optional<Identifier> requestEyeLayerOverride(OverrideToken token, LivingEntity entity);
    Optional<Identifier> requestFeatureStateKeyOverride(OverrideToken token, String featureOrStateKey, LivingEntity entity);

}
