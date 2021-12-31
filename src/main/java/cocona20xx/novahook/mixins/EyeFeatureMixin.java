package cocona20xx.novahook.mixins;

import cocona20xx.novahook.internal.accessors.CustomEyeLayerDuckInterface;
import cocona20xx.novahook.internal.accessors.VanillaEyeLayerDuckInterface;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(EyesFeatureRenderer.class)
public abstract class EyeFeatureMixin<T extends Entity, M extends EntityModel<T>> extends FeatureRenderer<T, M> implements VanillaEyeLayerDuckInterface, CustomEyeLayerDuckInterface {

    public EyeFeatureMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public boolean duckVanilla() {
        return true;
    }
    @Override
    public boolean duck(){
        return false;
    }

}
