package symbolics.division.honque;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;


public class HonqueTraquer {
    private static AttachmentType<Integer> HONKS;

    public static void init () {
        HONKS = AttachmentRegistry.createPersistent(
            Identifier.of(Honque.MODID, "honks"),
            Codec.INT
        );
    }

    public static void inc(ServerWorld world) {
        world.getAttachedOrCreate(HONKS, () -> 0);
        world.modifyAttached(HONKS, h -> h + 1);
        System.out.println(world.getAttachedOrCreate(HONKS, () -> 0));
    }
}
