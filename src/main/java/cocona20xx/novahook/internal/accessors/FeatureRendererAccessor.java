package cocona20xx.novahook.internal.accessors;

import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.util.Identifier;


public interface FeatureRendererAccessor {
    Identifier getOriginal();
    @SuppressWarnings("rawtypes")
    static FeatureRendererAccessor quickWrap(FeatureRenderer toWrap){
        return (FeatureRendererAccessor)toWrap;
    }
}
