package cocona20xx.novahook.internal.testing;

import cocona20xx.novahook.api.requester.OverrideRequestToken;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("rawtypes")
public class TestingRequestToken implements OverrideRequestToken<Boolean> {
    private Class ofClassTokenRepresents = null;
    private boolean data = false;

    @Override
    public void setRepresents(Class ofClass) {
        ofClassTokenRepresents = ofClass;
    }

    @Override
    public void setOfTokenData(Boolean tokenData) {
        data = tokenData;
    }

    @Override @Nullable
    public Class getRepresents() {
        return ofClassTokenRepresents;
    }

    @Override @Nullable
    public Boolean getTokenData() {
        return data;
    }

    @Override
    public Class<TestingRequestToken> getThisClass() {
        return TestingRequestToken.class;
    }
}
