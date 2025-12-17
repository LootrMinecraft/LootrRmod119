package noobanidus.mods.lootrrmod119.mixins;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RandomizableContainerBlockEntity.class)
public interface AccessorMixinRandomizableContainerBlockEntity {
  @Accessor("lootTable")
  ResourceLocation lootr$getLootTable();
  @Accessor("lootTableSeed")
  long lootr$getLootTableSeed();
}
