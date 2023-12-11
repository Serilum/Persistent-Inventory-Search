package com.natamus.persistentinventorysearch.fabric.mixin;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CreativeModeInventoryScreen.class, priority = 1001)
public abstract class CreativeModeInventoryScreenMixin {
	@Shadow private static CreativeModeTab selectedTab;
	@Shadow private EditBox searchBox;
	@Shadow protected abstract void refreshSearchResults();

	private static String searchQuery = "";

	@Inject(method = "init()V", at = @At(value = "TAIL"))
	private void CreativeModeInventoryScreen_init(CallbackInfo ci) {
		if (selectedTab.getType().equals(CreativeModeTab.Type.SEARCH)) {
			if (!searchQuery.equals("")) {
				searchBox.setValue(searchQuery);
				refreshSearchResults();
			}
		}
	}
	@Inject(method = "keyReleased(III)Z", at = @At(value = "HEAD"))
	public void CreativeModeInventoryScreen_keyReleased(int a, int b, int c, CallbackInfoReturnable<Boolean> cir) {
		if (searchBox.isFocused() && searchBox.isVisible()) {
			searchQuery = searchBox.getValue();
		}
	}

	@Inject(method = "selectTab(Lnet/minecraft/world/item/CreativeModeTab;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen;refreshSearchResults()V"))
	private void selectTab(CreativeModeTab p_98561_, CallbackInfo ci) {
		if (selectedTab.getType().equals(CreativeModeTab.Type.SEARCH)) {
			if (!searchQuery.equals("")) {
				searchBox.setValue(searchQuery);
			}
		}
	}
}
