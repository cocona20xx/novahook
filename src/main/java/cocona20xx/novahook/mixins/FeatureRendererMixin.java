package cocona20xx.novahook.mixins;

import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FeatureRenderer.class)
public abstract class FeatureRendererMixin {

    @Unique private Identifier defaultTexture = null;

    @Inject(method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;",
            @At(value = "RETURN"), cancellable = true)
    public void onGetTextureReturn(Entity entity, CallbackInfoReturnable<Identifier> cir){
        if(defaultTexture == null) defaultTexture = cir.getReturnValue();
    }
}
