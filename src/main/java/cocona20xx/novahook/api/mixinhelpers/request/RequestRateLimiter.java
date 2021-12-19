package cocona20xx.novahook.api.mixinhelpers.request;

import net.minecraft.client.MinecraftClient;

/**
 * Defines the structure for helper classes used to time the duration each Entity's renderer waits to check for overrides.
 */
public interface RequestRateLimiter {

    float DEFAULT_DURATION_LENGTH = 120.0F;

    /**
     * Checks if the amount of client tick deltatime that has passed since overrides have last updated exceeds the delay duration, in order to rate limit override checks.
     *<br> Note that entity renderers should always check for overrides upon creation of the entity.
     * @param client The current instance of the game client, used to get the client deltatime.
     * @return Returns true if the amount of time passed exceeds the wait duration.
     */
    boolean doDurationCheck(MinecraftClient client);

    /**
     * Sets the wait duration, in client-side ticks. As with server-side ticks, each tick is 1/20th of a second.
     * @param durationLength The duration to wait between override checks, in client-side ticks.
     */
    void setDurationLength(float durationLength);
}
