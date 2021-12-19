package cocona20xx.novahook.mixins;

import cocona20xx.novahook.api.OverrideToken;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashMap;
import java.util.List;


@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin{

    @Unique private Identifier defaultBaseLayerTexture = null;
    @Unique private final HashMap<OverrideToken, Identifier> overrides = new HashMap<>();

    @Final @Shadow protected List<FeatureRenderer<LivingEntity, EntityModel<LivingEntity>>> features;
    @Final @Shadow protected abstract boolean addFeature(FeatureRenderer<LivingEntity, EntityModel<LivingEntity>> feature);

    @SuppressWarnings("InvalidInjectorMethodSignature") //placed here as IDEA MCDev plugin incorrectly shows a compile-time error that doesn't actually exist.
    @ModifyVariable(
            method = "getRenderLayer(Lnet/minecraft/entity/LivingEntity;ZZZ)Lnet/minecraft/client/render/RenderLayer;",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;",
                    ordinal = 0
                    )

    )
    public Identifier mixin(Identifier identifier){
        if(defaultBaseLayerTexture == null) defaultBaseLayerTexture = identifier;
        return identifier;
    }



}
