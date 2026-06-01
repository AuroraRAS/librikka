package rikka.librikka.mod;

public class ClientProxy extends CommonProxy {
	@Override
    public void registerModelLoaders() {
        // Handled automatically via ClientRegistrationHandler subscribing to ModelEvent.RegisterGeometryLoaders
    }
}
