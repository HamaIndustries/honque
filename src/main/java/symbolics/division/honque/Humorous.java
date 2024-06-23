package symbolics.division.honque;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Humorous {
    // i dont wannt do datagen taggg
    private static final Identifier ID = Identifier.of(Honque.MODID, "humorous");
    public static final ComponentType<Boolean> COMPONENT =
            Registry.register(
                    Registries.DATA_COMPONENT_TYPE,
                    ID,
                    ComponentType.<Boolean>builder()
                            .codec(Codec.BOOL)
                            .packetCodec(PacketCodecs.BOOL)
                            .cache()
                            .build()
            );


}
