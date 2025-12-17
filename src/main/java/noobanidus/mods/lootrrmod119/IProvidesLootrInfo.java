package noobanidus.mods.lootrrmod119;

import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public interface IProvidesLootrInfo {
  UUID lootr$getUuid ();

  ResourceLocation lootrInfo$getLootTable();

  long lootrInfo$getSeed();
}
