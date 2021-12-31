package cocona20xx.novahook.internal.accessors;


import net.minecraft.client.render.entity.feature.FeatureRenderer;

public interface VanillaEyeLayerDuckInterface {
    default boolean duckVanilla(){
        return false;
    }

    @SuppressWarnings("rawtypes")
    static <E extends FeatureRenderer> boolean quickGet(E eyeRenderer){
        return ((VanillaEyeLayerDuckInterface)eyeRenderer).duckVanilla();
    }
}
