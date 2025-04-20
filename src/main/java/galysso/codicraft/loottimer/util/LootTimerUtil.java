package galysso.codicraft.loottimer.util;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;

public class LootTimerUtil {
    public static ComponentType<?> getBoundsToPlayerComponent() {
        return Registries.DATA_COMPONENT_TYPE.get(Identifier.of("rpginventory:bounds_to_player"));
    }
}
