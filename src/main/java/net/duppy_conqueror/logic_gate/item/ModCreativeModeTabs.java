package net.duppy_conqueror.logic_gate.item;

import net.duppy_conqueror.logic_gate.LogicGate;
import net.duppy_conqueror.logic_gate.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber
public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LogicGate.MOD_ID);

    public static final CreativeModeTab LOGIC_GATE_TAB;

    static {
        LOGIC_GATE_TAB = CreativeModeTab.builder()
            .title(Component.translatable("itemgroup.logic_gate"))
            .icon(() -> new ItemStack(ModBlocks.LOGIC_GATE))
            .displayItems((parameters, output) -> output.accept(ModBlocks.LOGIC_GATE))
            .build();

        CREATIVE_MODE_TABS.register(LogicGate.MOD_ID, () -> LOGIC_GATE_TAB);
    }

    public static void init(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(ModBlocks.LOGIC_GATE);
        }
    }
}
