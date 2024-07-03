package symbolics.division.honque.compat;

import net.fabricmc.loader.api.FabricLoader;
import symbolics.division.honque.Honque;

import java.lang.reflect.InvocationTargetException;

public interface ModCompatibility {

    void initialize();

    String COMPAT_PACKAGE_PREFIX = "symbolics.division.honque.compat.";

    private static void tryInit(String compatClass, String modRef) {
        var loader = FabricLoader.getInstance();
        if (!loader.isModLoaded(modRef)) {
            Honque.LOGGER.debug("Did not find mod " + modRef + ", skipping compat check");
            return;
        }

        Honque.LOGGER.debug("Loading compat for mod " + modRef);

        String className = COMPAT_PACKAGE_PREFIX + compatClass;
        ModCompatibility compat;
        try {
             compat = (ModCompatibility)Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException err) {
            Honque.LOGGER.error("Unable to find compatibility class: " + className);
            if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
                throw new RuntimeException(err);
            }
            return;
        } catch (NoSuchMethodException | InstantiationException | IllegalArgumentException | InvocationTargetException | IllegalAccessException | ClassCastException err) {
            // you can tell from the number of exceptions we handle here that our code
            // is robust and sensible
            Honque.LOGGER.error("Failed to run constructor for: " + className);
            if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
                throw new RuntimeException(err);
            }
            return;
        }
        compat.initialize();
    }

    static void init() {
        Honque.LOGGER.debug("Loading compatibilities");
        tryInit("MawCompat", "magnificent-maw");
        tryInit("BombasticCompat", "bombastic");
        tryInit("ConfettiCompat", "confetti");
        tryInit("AmarongCompat", "amarong");
    }
}
