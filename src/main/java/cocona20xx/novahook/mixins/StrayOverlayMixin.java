package cocona20xx.novahook.mixins;

import cocona20xx.novahook.api.EntityOverrideDataAccessor;
import cocona20xx.novahook.api.OverrideToken;
import cocona20xx.novahook.internal.accessors.FeatureRendererAccessor;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.StrayOverlayFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StrayOverlayFeatureRenderer.class)
public abstract class StrayOverlayMixin <T extends MobEntity & RangedAttackMob, M extends EntityModel<T>> extends FeatureRenderer<T, M> implements FeatureRendererAccessor {

    @Shadow @Final private static Identifier SKIN;
    @Shadow @Final private SkeletonEntityModel<T> model;
    @Unique private OverrideToken token;

    public StrayOverlayMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/mob/MobEntity;FFFFFF)V",
            at = @At("RETURN"),
            cancellable = true
    )

    public void onRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T mobEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci){
        EntityOverrideDataAccessor accessed = EntityOverrideDataAccessor.quickWrap(mobEntity);
        if(token == null){
            token = new OverrideToken(accessed.getTypeId(), this.getOriginal(), OverrideToken.TokenTypes.feature);
            accessed.storeToken(token);
        }
        if(accessed.retrieveOverride(token).isPresent()){
            Identifier newId = accessed.retrieveOverride(token).get();
            render(this.getContextModel(), this.model, newId, matrixStack, vertexConsumerProvider, i, mobEntity, f, g, j, k, l, h, 1.0F, 1.0F, 1.0F);
            ci.cancel();
        }
    }

    @Override
    public Identifier getOriginal() {
            return SKIN;
    }
}
