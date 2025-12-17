package noobanidus.mods.lootrrmod119.mixins;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.gurken.recurrencemod.block.custom.SkeletonBlock;
import net.gurken.recurrencemod.block.entity.SkeletonBlockBlockEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import noobanidus.mods.lootr.api.LootrAPI;
import noobanidus.mods.lootr.api.inventory.ILootrInventory;
import noobanidus.mods.lootr.config.ConfigManager;
import noobanidus.mods.lootr.data.SpecialChestInventory;
import noobanidus.mods.lootr.util.ChestUtil;
import noobanidus.mods.lootrrmod119.IProvidesLootrInfo;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SkeletonBlock.class)
public class MixinSkeletonBlock {
  @WrapMethod(method = "use")
  private InteractionResult lootr$use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit, Operation<InteractionResult> original) {
    if (!pLevel.isClientSide()) {
      BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
      if (blockEntity instanceof IProvidesLootrInfo info && blockEntity instanceof SkeletonBlockBlockEntity skele) {
        ILootrInventory inventory = LootrAPI.getInventory(pLevel, info.lootr$getUuid(), pPos, (ServerPlayer) pPlayer, skele, (player, container, resourceLocation, l) -> {
          LootTable loottable = pLevel.getServer().getLootData().getLootTable(resourceLocation);
          if (loottable == LootTable.EMPTY) {
            Logger var10000 = LootrAPI.LOG;
            ResourceLocation var10001 = pLevel.dimension().location();
            var10000.error("Unable to fill loot chest in " + var10001 + " at " + pPos + " as the loot table '" + resourceLocation + "' couldn't be resolved! Please search the loot table in `latest.log` to see if there are errors in loading.");
            if (ConfigManager.REPORT_UNRESOLVED_TABLES.get()) {
              player.displayClientMessage(ChestUtil.getInvalidTable(resourceLocation), false);
            }
          }

          if (player instanceof ServerPlayer) {
            CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer) player, resourceLocation);
          }

          LootParams.Builder builder = (new LootParams.Builder((ServerLevel) pLevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pPos));
          if (player != null) {
            builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
          }

          loottable.fill(container, builder.create(LootContextParamSets.CHEST), LootrAPI.getLootSeed(l));
        }, info::lootrInfo$getLootTable, info::lootrInfo$getSeed);
        if (inventory instanceof SpecialChestInventory inventory2) {
          inventory2.setMenuBuilder();
          NetworkHooks.openScreen((ServerPlayer) pPlayer, inventory2, pPos);
        }
        // You can add additional logic here to handle the UUID as needed
        return InteractionResult.SUCCESS;
      }
    }
    return original.call(pState, pLevel, pPos, pPlayer, pHand, pHit);
  }
}
