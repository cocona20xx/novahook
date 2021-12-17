package cocona20xx.novahook.api.requester;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * Defines the base-level structure of Entity Texture Override Requesters.
 *<br> Most child classes will likely want to extend an abstract child class provided by the API instead.
 */
@SuppressWarnings("rawtypes")
public interface OverrideTextureRequester {

    Optional<Identifier> requestBaseTextureOverride(OverrideRequestToken token, LivingEntity entity);
    Optional<Identifier> requestEyeLayerOverride(OverrideRequestToken token, LivingEntity entity);
    Optional<Identifier> requestFeatureStateKeyOverride(OverrideRequestToken token, String featureOrStateKey, LivingEntity entity);

}
