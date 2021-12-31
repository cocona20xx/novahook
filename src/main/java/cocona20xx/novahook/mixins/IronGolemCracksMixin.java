package cocona20xx.novahook.mixins;

import cocona20xx.novahook.api.EntityOverrideDataAccessor;
import cocona20xx.novahook.api.OverrideToken;
import cocona20xx.novahook.internal.accessors.FeatureRendererAccessor;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.IronGolemCrackFeatureRenderer;
import net.minecraft.client.render.entity.model.IronGolemEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Objects;

@Mixin(IronGolemCrackFeatureRenderer.class)
public abstract class IronGolemCracksMixin extends FeatureRenderer<IronGolemEntity, IronGolemEntityModel<IronGolemEntity>> implements FeatureRendererAccessor {

    @Shadow @Final private static Map<IronGolemEntity.Crack, Identifier> DAMAGE_TO_TEXTURE;
    @Unique private IronGolemEntity.Crack currentCrack = IronGolemEntity.Crack.NONE;
    @Unique private IronGolemEntity.Crack lastCrack = null;
    @Unique private OverrideToken token = null;


    public IronGolemCracksMixin(FeatureRendererContext<IronGolemEntity, IronGolemEntityModel<IronGolemEntity>> context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/IronGolemEntity;FFFFFF)V",
            at = @At("RETURN"),
            cancellable = true
    )
    public void onRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, IronGolemEntity ironGolemEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci){
        if(!ironGolemEntity.isInvisible()){
            EntityOverrideDataAccessor accessed = EntityOverrideDataAccessor.quickWrap(ironGolemEntity);
            currentCrack = ironGolemEntity.getCrack();
            if(!Objects.equals(currentCrack, lastCrack)){
                if(token != null) accessed.clearToken(token);
                token = null;
            }
            lastCrack = currentCrack;
            if(!currentCrack.equals(IronGolemEntity.Crack.NONE)){
                if(token == null){
                    token = new OverrideToken(accessed.getTypeId(), getOriginal(), OverrideToken.TokenTypes.feature);
                    accessed.storeToken(token);
                }
            }
            if(accessed.retrieveOverride(token).isPresent()){
                Identifier newSkin = accessed.retrieveOverride(token).get();
                renderModel(getContextModel(), newSkin, matrixStack, vertexConsumerProvider, i, ironGolemEntity, 1.0F, 1.0F, 1.0F);
                ci.cancel();
            }
        }
    }

    @Override
    public @Nullable Identifier getOriginal() {
        return DAMAGE_TO_TEXTURE.get(currentCrack);
    }
}
