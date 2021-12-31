package cocona20xx.novahook.internal.accessors;

import net.minecraft.client.render.entity.feature.FeatureRenderer;

public interface CustomEyeLayerDuckInterface {
    default boolean duck(){
        return false;
    }

    @SuppressWarnings("rawtypes")
    static <E extends FeatureRenderer> boolean quickGet(E eyeRenderer){
        return ((CustomEyeLayerDuckInterface)eyeRenderer).duck();
    }
}
