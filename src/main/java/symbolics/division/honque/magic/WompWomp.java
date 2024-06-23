package symbolics.division.honque.magic;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import symbolics.division.honque.Honque;

public final record WompWomp(String action, int value) implements CustomPayload {
    public static final CustomPayload.Id<WompWomp> ID = new CustomPayload.Id<>(Identifier.of(Honque.MODID, "whats_cookin_good_lookin"));
    public static final PacketCodec<ByteBuf, WompWomp> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, WompWomp::action,
            PacketCodecs.INTEGER, WompWomp::value,
            WompWomp::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
