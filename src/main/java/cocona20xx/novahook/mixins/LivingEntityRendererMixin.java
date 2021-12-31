package cocona20xx.novahook.mixins;

import cocona20xx.novahook.NovaHookClient;
import cocona20xx.novahook.api.EntityOverrideDataAccessor;
import cocona20xx.novahook.api.OverrideToken;
import cocona20xx.novahook.internal.MultiEntityCustomEyeLayerFeature;
import cocona20xx.novahook.internal.accessors.FeatureRendererAccessor;
import cocona20xx.novahook.internal.accessors.VanillaEyeLayerDuckInterface;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin{

    @Unique private Identifier currentBaseLayerTexture = null;
    @Unique private Identifier lastBaseLayerTexture = null;
    @Unique private Identifier possibleOverrideTexture = null;
    @Unique private OverrideToken token = null;
    @Unique private boolean firstPassDone = false;

    @Final @Shadow protected List<FeatureRenderer<LivingEntity, EntityModel<LivingEntity>>> features;
    @Final @Shadow protected abstract boolean addFeature(FeatureRenderer<LivingEntity, EntityModel<LivingEntity>> feature);

    @Shadow @Final private static Logger LOGGER;

    @SuppressWarnings("InvalidInjectorMethodSignature") //placed here as IDEA MCDev plugin incorrectly shows a compile-time error that doesn't actually exist.
    @ModifyVariable(
            method = "getRenderLayer(Lnet/minecraft/entity/LivingEntity;ZZZ)Lnet/minecraft/client/render/RenderLayer;",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;",
                    ordinal = 0
                    )

    )
    public Identifier onGetTexture(Identifier identifier){
        if(currentBaseLayerTexture == null || !Objects.equals(identifier, currentBaseLayerTexture)) this.currentBaseLayerTexture = identifier;
        if(possibleOverrideTexture == null){
            return identifier;
        } else return possibleOverrideTexture;
    }


    @SuppressWarnings("rawtypes")
    private void firstPass(){
        boolean eyeAlreadyPresent = false;
        FeatureRendererContext featureCtx = ((LivingEntityRenderer)(Object)this);
        ArrayList<FeatureRenderer<LivingEntity, EntityModel<LivingEntity>>> toRemove = new ArrayList<>();
        for(FeatureRenderer<LivingEntity, EntityModel<LivingEntity>> f : features){
            if(VanillaEyeLayerDuckInterface.quickGet((FeatureRenderer)f)){
                toRemove.add(f);
                eyeAlreadyPresent = true;
            }
        }
        if(!toRemove.isEmpty()){
            for(FeatureRenderer<LivingEntity, EntityModel<LivingEntity>> r : toRemove){
                try{
                    Identifier id = FeatureRendererAccessor.quickWrap(r).getOriginal();
                    features.remove(r);
                    MultiEntityCustomEyeLayerFeature toAdd = new MultiEntityCustomEyeLayerFeature(featureCtx, id); //hopefully won't explode!
                    this.addFeature(toAdd);
                } catch (Exception e){
                    if(NovaHookClient.TESTS_ACTIVE) LOGGER.info(e);
                }
            }
        }
        if(!eyeAlreadyPresent){
            MultiEntityCustomEyeLayerFeature newEye = new MultiEntityCustomEyeLayerFeature(featureCtx);
            this.addFeature(newEye);
        }
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("RETURN") )
    public void onRender(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci){
        if(!firstPassDone){
            firstPass();
            firstPassDone = true;
        }
        EntityOverrideDataAccessor accessed = EntityOverrideDataAccessor.quickWrap(livingEntity);
        if(!Objects.equals(currentBaseLayerTexture, lastBaseLayerTexture)){
            if(token != null) {
                accessed.clearToken(token);
                token = null;
            }
        }
        this.lastBaseLayerTexture = this.currentBaseLayerTexture;
        if(this.token == null){
            this.token = new OverrideToken(accessed.getTypeId(), this.currentBaseLayerTexture, OverrideToken.TokenTypes.base_layer);
            accessed.storeToken(token);
        }
        if(accessed.retrieveOverride(this.token).isPresent()){
            this.possibleOverrideTexture = accessed.retrieveOverride(this.token).get();
        }
    }
}
