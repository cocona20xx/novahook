package cocona20xx.novahook.mixins;

import cocona20xx.novahook.api.StandardOverrideAccessor;
import cocona20xx.novahook.api.WithFeatureStateOverrideAccessor;
import cocona20xx.novahook.api.requester.OverrideRequestToken;
import cocona20xx.novahook.api.requester.OverrideTextureRequester;
import com.google.common.collect.ArrayListMultimap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(LivingEntity.class)
public class LivingStandardMixin implements StandardOverrideAccessor, WithFeatureStateOverrideAccessor {
    @Unique
    private boolean isClientSide = false;
    @Unique
    private Identifier baseTextureOverride = null;
    @Unique
    private Identifier eyeLayerTextureOverride = null;
    @Unique
    private final ArrayListMultimap<String, Identifier> featureStateOverrides = ArrayListMultimap.create();
    @Unique @SuppressWarnings("rawtypes")
    private OverrideRequestToken token = null;
    @Unique
    private OverrideTextureRequester requester = null;
    @Unique
    private LivingEntity selfRef;
    @Unique
    private boolean hasRequestInfo = false;

    @Override
    public void setBase(Identifier idToSet) {
        if(isClientSide) this.baseTextureOverride = idToSet;
    }
    @Override
    public void setEye(Identifier idToSet) {
        if(isClientSide) this.eyeLayerTextureOverride = idToSet;
    }
    @Override
    public Optional<Identifier> getBaseOverride() {
        if(baseTextureOverride != null) return Optional.of(baseTextureOverride);
        else return Optional.empty();
    }
    @Override
    public Optional<Identifier> getEyeOverride() {
        if(eyeLayerTextureOverride != null) return Optional.of(eyeLayerTextureOverride);
        else return Optional.empty();
    }

    @Override @SuppressWarnings("rawtypes")
    public void setRequestInfo(OverrideRequestToken token, LivingEntity selfRef) {
        if(isClientSide) {
            this.token = token;
            this.selfRef = selfRef;
            this.hasRequestInfo = true;
        }
    }

    @Override
    public void setRequester(OverrideTextureRequester requester) {
        if(isClientSide) this.requester = requester;
    }

    @Override
    public void doBaseRequest() throws IllegalStateException {
        if(isClientSide) {
            if (requester == null || token == null)
                throw new IllegalStateException("LivingEntity Mixin (NovaHook): Texture Request cannot be made, as requester or request token is null.");
            else {
                requester.requestBaseTextureOverride(token, selfRef).ifPresent(this::setBase);
            }
        }
    }

    @Override
    public void doEyeLayerRequest() throws IllegalStateException {
        if(isClientSide) {
            if(requester == null || token == null) throw new IllegalStateException("LivingEntity Mixin (NovaHook): Texture Request cannot be made, as requester or request token is null.");
            else {
                requester.requestEyeLayerOverride(token, selfRef).ifPresent(this::setEye);
            }
        }

    }

    @Override
    public boolean hasRequestInfo() {
        return hasRequestInfo;
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    @SuppressWarnings("rawtypes")
    public void onConstructor(EntityType entityType, World world, CallbackInfo ci){
        isClientSide = world.isClient();

    }

    @Override
    public void addOverrideForFeatureState(String keyOfOverride, Identifier defaultId) {
        if(isClientSide){
            if(featureStateOverrides.containsKey(keyOfOverride)){
                featureStateOverrides.removeAll(keyOfOverride);
            }
            featureStateOverrides.put(keyOfOverride, defaultId);
        }
    }

    @Override
    public Optional<Identifier> getOverrideFor(String keyOfOverride) {
        if (featureStateOverrides.containsKey(keyOfOverride)) {
            Identifier possiblyNull = featureStateOverrides.get(keyOfOverride).get(0);
            if (possiblyNull == null) return Optional.empty();
            else return Optional.of(possiblyNull);
        } else return Optional.empty();
    }

    @Override
    public Optional<Identifier> requestFeatureStateOfKey(String keyOfOverride) throws IllegalStateException {
        if(!isClientSide) return Optional.empty();
        else {
            if(requester == null || token == null) throw new IllegalStateException("LivingEntity Mixin (NovaHook): Texture Request cannot be made, as requester or request token is null.");
            else {
                Identifier possible = requester.requestFeatureStateKeyOverride(token, keyOfOverride, selfRef).orElse(null);
                if (possible == null) return Optional.empty();
                else return Optional.of(possible);
            }
        }
    }

    @Override
    public void requestAllFeatureKeys() throws IllegalStateException {
        if(isClientSide){
            if(requester == null || token == null) throw new IllegalStateException("LivingEntity Mixin (NovaHook): Texture Request cannot be made, as requester or request token is null.");
            else {
                for(String k : featureStateOverrides.keySet()){
                    Identifier poss = requestFeatureStateOfKey(k).orElse(null);
                    if(poss != null){
                        featureStateOverrides.removeAll(k);
                        featureStateOverrides.put(k, poss);
                    }
                }
            }
        }
    }
}

