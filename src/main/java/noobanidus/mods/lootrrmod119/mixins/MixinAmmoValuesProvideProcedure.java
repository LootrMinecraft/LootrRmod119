package noobanidus.mods.wariumtweakedfix.mixins;

import net.mcreator.wariumtweaked.procedures.AmmoValuesProvideProcedure;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AmmoValuesProvideProcedure.class)
public class MixinAmmoValuesProvideProcedure {
  @Unique
  private static CompoundTag torchNbtFix$tempCreate(ItemStack stack) {
    if (!stack.hasTag()) {
      return new CompoundTag();
    }

    return stack.getTag();
  }

  @Redirect(method = "execute(Lnet/minecraftforge/eventbus/api/Event;Lnet/minecraft/world/entity/Entity;)V", at=@At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getOrCreateTag()Lnet/minecraft/nbt/CompoundTag;", remap = true), require = 0)
  private static CompoundTag WariumTweakedFix(ItemStack instance) {
    return torchNbtFix$tempCreate(instance);
  }
}
