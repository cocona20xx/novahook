package cocona20xx.novahook.internal.testing;

import cocona20xx.novahook.api.requester.OverrideRequestToken;
import cocona20xx.novahook.api.requester.OverrideTextureRequester;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

@SuppressWarnings("rawtypes")
public class TestingRequester implements OverrideTextureRequester {
    @Override
    public Optional<Identifier> requestBaseTextureOverride(OverrideRequestToken token, LivingEntity entity) {
        Class clazz = entity.getClass();
        if(token.getThisClass().equals(TestingRequestToken.class)){
            TestingRequestToken castToken = ((TestingRequestToken) token);
            if(clazz.equals(castToken.getRepresents()) && Boolean.TRUE.equals(castToken.getTokenData())){ //will only happen on cows since they are used for the test
                return Optional.of(new Identifier("novahook", "textures/cow_test_base.png"));
            } else return Optional.empty();
        }
        else return Optional.empty();
    }

    @Override
    public Optional<Identifier> requestEyeLayerOverride(OverrideRequestToken token, LivingEntity entity) {
        Class clazz = entity.getClass();
        if(token.getThisClass().equals(TestingRequestToken.class)){
            TestingRequestToken castToken = ((TestingRequestToken) token);
            if(clazz.equals(castToken.getRepresents()) && Boolean.TRUE.equals(castToken.getTokenData())){ //will only happen on cows since they are used for the test
                return Optional.of(new Identifier("novahook", "textures/cow_test_layer.png"));
            } else return Optional.empty();
        }
        else return Optional.empty();
    }

    @Override
    public Optional<Identifier> requestFeatureStateKeyOverride(OverrideRequestToken token, String featureOrStateKey, LivingEntity entity) {
        return Optional.empty();
    }
}
