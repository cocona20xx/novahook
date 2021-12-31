package cocona20xx.novahook.api;

import lombok.Getter;
import net.minecraft.client.MinecraftClient;

/**
 * Helper class for Override-related mixins. Note that <b>only the {@link OverrideRequestLimiter#setWaitTime(float)} method should be considered part of the API.</b>
 */
public final class OverrideRequestLimiter {
    @Getter private static float globalWaitTime = 120.0F;
    private final MinecraftClient clientRef;
    private float timeWaited;

    public OverrideRequestLimiter(MinecraftClient clientRef){
        this.clientRef = clientRef;
        this.timeWaited = globalWaitTime + 5.0F;
    }

    /**
     * @return Returns true when the amount of time specified by the global limiter wait time is less than the duration since this function has last returned true.
     *<br> This function will always return true on first call.
     */
    public boolean doTimeCheck(){
        float delta = clientRef.getTickDelta();
        timeWaited += delta;
        if(timeWaited >= globalWaitTime){
            timeWaited = 0;
            return true;
        } else return false;
    }

    /**
     * Sets the global wait time for <b>ALL</b> OverrideRequestLimiters (as they are created by NovaHook's internal mixins). It is highly recommended for the wait time to be above 5 seconds, aka 100 ticks.
     * @param newWait The new wait duration, in clientside ticks (default value is 120 ticks/6 seconds).
     * @throws IllegalArgumentException Thrown if the wait time is less than or equal to 0.
     */
    public static void setWaitTime(float newWait) throws IllegalArgumentException{
        if(newWait <= 0){
            throw new IllegalArgumentException("OverrideRequestLimiter#setWaitTime (static method): Global wait time must be greater than 0. The passed wait time will not be set.");
        } else {
            globalWaitTime = newWait;
        }
    }
}
