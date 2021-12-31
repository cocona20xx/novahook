package cocona20xx.novahook.mixins;

import cocona20xx.novahook.NovaHookClient;
import cocona20xx.novahook.api.EntityOverrideDataAccessor;
import cocona20xx.novahook.api.OverrideToken;
import cocona20xx.novahook.internal.accessors.FeatureRendererAccessor;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.LlamaDecorFeatureRenderer;
import net.minecraft.client.render.entity.model.LlamaEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(LlamaDecorFeatureRenderer.class)
public abstract class LlamaDecorMixin extends FeatureRenderer<LlamaEntity, LlamaEntityModel<LlamaEntity>> implements FeatureRendererAccessor {
    @Shadow @Final private static Identifier[] LLAMA_DECOR;
    @Shadow @Final private static Identifier TRADER_LLAMA_DECOR;
    @Shadow @Final private LlamaEntityModel<LlamaEntity> model;
    @Unique private DyeColor lastDyeColor = null;
    @Unique private DyeColor currentDyeColor = null;
    @Unique private boolean isTraderLlama = true;
    @Unique private OverrideToken token = null;

    public LlamaDecorMixin(FeatureRendererContext<LlamaEntity, LlamaEntityModel<LlamaEntity>> context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/LlamaEntity;FFFFFF)V",
            at = @At("RETURN"),
            cancellable = true
    )
    public void onRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, LlamaEntity llamaEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci){
        boolean currentTraderStatus = llamaEntity.isTrader();
        EntityOverrideDataAccessor accessed = EntityOverrideDataAccessor.quickWrap(llamaEntity);
        if(isTraderLlama != currentTraderStatus){
            if(!currentTraderStatus){
                currentDyeColor = llamaEntity.getCarpetColor();
                lastDyeColor = llamaEntity.getCarpetColor();
            } else {
                currentDyeColor = null;
                lastDyeColor = null;
            }
            isTraderLlama = currentTraderStatus;
            if(token != null){
                accessed.clearToken(token);
                token = null;
            }
        }
        currentDyeColor = llamaEntity.getCarpetColor();
        if(!Objects.equals(currentDyeColor, lastDyeColor)){
            if(token != null){
                accessed.clearToken(token);
                token = null;
            }
        }
        lastDyeColor = currentDyeColor;
        if(token == null){
            if(isTraderLlama){
                token = new OverrideToken(accessed.getTypeId(), TRADER_LLAMA_DECOR, OverrideToken.TokenTypes.feature);
                accessed.storeToken(token);
            } else {
                if(currentDyeColor == null) currentDyeColor = llamaEntity.getCarpetColor(); //failsafe
                Identifier colorId = LLAMA_DECOR[currentDyeColor.getId()];
                token = new OverrideToken(accessed.getTypeId(), colorId, OverrideToken.TokenTypes.feature);
                accessed.storeToken(token);
            }
        }
        if(accessed.retrieveOverride(token).isPresent()){
            Identifier newSkin = accessed.retrieveOverride(token).get();
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(newSkin));
            model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            ci.cancel();
        }
    }

    @Override
    public Identifier getOriginal() {
        if(isTraderLlama){
            return TRADER_LLAMA_DECOR;
        } else {
            return LLAMA_DECOR[currentDyeColor.getId()];
        }
    }
}
