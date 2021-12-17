package cocona20xx.novahook.impl;

import cocona20xx.novahook.api.RequestRateLimiter;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;

/**
 * The default version of a texture override rate limiter.
 * @see RequestRateLimiter
 */
public class DefaultRequestRateLimiter implements RequestRateLimiter {
    private float optionalDurationLength = -1.0F;
    private float currentDuration = -(Float.MAX_VALUE);

    @Override
    public boolean doDurationCheck(@NotNull MinecraftClient client) {
        float dt = client.getTickDelta();
        float usedDuration = (optionalDurationLength <= 0) ? DEFAULT_DURATION_LENGTH : optionalDurationLength;
        currentDuration += dt;
        if(currentDuration < 0 || currentDuration > usedDuration){
            currentDuration = 0;
            return true;
        } else return false;
    }

    /**
     * @param durationLength The duration to wait between override checks, in client-side ticks. For this implementation specifically, a value <= 0 will have this object use the default wait time of 120 ticks.
     */
    @Override
    public void setDurationLength(float durationLength) {
        this.optionalDurationLength = durationLength;
    }
}
