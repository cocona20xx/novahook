package cocona20xx.novahook.mixins;

import cocona20xx.novahook.api.OverrideToken;
import cocona20xx.novahook.internal.accessors.CustomEyeLayerDuckInterface;
import cocona20xx.novahook.api.EntityOverrideDataAccessor;
import cocona20xx.novahook.internal.accessors.FeatureRendererAccessor;
import cocona20xx.novahook.internal.accessors.VanillaEyeLayerDuckInterface;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FeatureRenderer.class)
public class FeatureRendererMixin implements FeatureRendererAccessor, VanillaEyeLayerDuckInterface, CustomEyeLayerDuckInterface {

    @Unique private Identifier defaultTexture = null;
    @Unique private OverrideToken token = null;

    @Inject(method = "getTexture",
            at = @At(value = "RETURN"), cancellable = true)
    public void onGetTextureReturn(Entity entity, CallbackInfoReturnable<Identifier> cir) {
        defaultTexture = cir.getReturnValue();
        EntityOverrideDataAccessor accessed = EntityOverrideDataAccessor.quickWrap((LivingEntity) entity);
        if (defaultTexture != null) {
            if(token == null){
                token = new OverrideToken(accessed.getTypeId(), defaultTexture, OverrideToken.TokenTypes.feature);
                accessed.storeToken(token);
            }
            if (accessed.retrieveOverride(token).isPresent()) {
                cir.setReturnValue(accessed.retrieveOverride(token).get());
                cir.cancel();
            }
        }
    }
    @Override
    public Identifier getOriginal() {
        return defaultTexture;
    }

    @Override
    public boolean duckVanilla() {
        return false;
    }
    @Override
    public boolean duck(){
        return false;
    }
}
