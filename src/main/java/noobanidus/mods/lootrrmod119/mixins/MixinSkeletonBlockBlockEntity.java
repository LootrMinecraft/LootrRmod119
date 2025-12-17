package noobanidus.mods.lootrrmod119.mixins;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.gurken.recurrencemod.block.entity.SkeletonBlockBlockEntity;
import net.gurken.recurrencemod.screen.SkeletonBlockMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import noobanidus.mods.lootr.api.MenuBuilder;
import noobanidus.mods.lootrrmod119.IProvidesLootrInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(SkeletonBlockBlockEntity.class)
public class MixinSkeletonBlockBlockEntity implements IProvidesLootrInfo {
  @Unique
  private UUID lootr$uuid;

  @Shadow
  @Final
  protected ContainerData data;

  public UUID lootr$getUuid() {
    if (lootr$uuid == null) {
      lootr$uuid = UUID.randomUUID();
    }
    return lootr$uuid;
  }

/*  public MenuBuilder lootr$getMenuBuilder() {
    return new MenuBuilder() {
      @Override
      public AbstractContainerMenu build(int i, Inventory inventory, Container container, int i1) {
        return new SkeletonBlockMenu(i, inventory, this, data);
      }
    };
  }*/

  @Override
  public ResourceLocation lootrInfo$getLootTable() {
    return ((AccessorMixinRandomizableContainerBlockEntity) (Object) this).lootr$getLootTable();
  }

  @Override
  public long lootrInfo$getSeed() {
    return ((AccessorMixinRandomizableContainerBlockEntity) (Object) this).lootr$getLootTableSeed();
  }

  @WrapMethod(method = "saveAdditional")
  private void lootrrmod119$saveAdditional(CompoundTag nbt, Operation<Void> original) {
    original.call(nbt);
    nbt.putUUID("LootrUUID", lootr$getUuid());
  }

  @WrapMethod(method = "load")
  private void lootrrmod119$load(CompoundTag nbt, Operation<Void> original) {
    original.call(nbt);
    if (nbt.hasUUID("LootrUUID")) {
      lootr$uuid = nbt.getUUID("LootrUUID");
    }
  }
}
