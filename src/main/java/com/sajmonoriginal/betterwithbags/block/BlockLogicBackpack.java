package com.sajmonoriginal.betterwithbags.block;

import com.sajmonoriginal.betterwithbags.item.BackpackItems;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class BlockLogicBackpack extends BlockLogic {

    public BlockLogicBackpack(Block<?> block) {
        super(block, Material.cloth);
        setBlockBounds(0.1875, 0.0, 0.1875, 0.8125, 0.625, 0.8125);
    }

    @Override
    public boolean isCubeShaped() {
        return false;
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public boolean blocksLight() {
        return false;
    }

    @Override
    public void onBlockPlacedByMob(World world, int x, int y, int z, Side side, Mob mob, double xOffset, double yOffset) {
        Direction dir = mob.getHorizontalPlacementDirection(side).getOpposite();
        world.setBlockMetadataWithNotify(x, y, z, dir.getId());
    }

    @Override
    public void onBlockPlacedOnSide(World world, int x, int y, int z, Side side, double xOffset, double yOffset) {
        if (!side.isHorizontal()) {
            side = Side.SOUTH;
        }
        Direction dir = side.getDirection();
        world.setBlockMetadataWithNotify(x, y, z, dir.getId());
    }

    @Override
    public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xOffset, double yOffset) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (!(te instanceof TileEntityBackpack)) {
            return false;
        }

        TileEntityBackpack backpackTE = (TileEntityBackpack) te;

        if (player.isSneaking()) {
            if (!world.isClientSide) {
                pickUpBackpack(world, x, y, z, player, backpackTE);
            }
            return true;
        }

        openBackpackScreen(player, backpackTE);
        return true;
    }

    @net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
    private void openBackpackScreen(Player player, TileEntityBackpack backpackTE) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
        mc.displayScreen(new com.sajmonoriginal.betterwithbags.gui.ScreenBackpackBlock(player.inventory, backpackTE));
    }

    private void pickUpBackpack(World world, int x, int y, int z, Player player, TileEntityBackpack backpackTE) {
        ItemStack backpackItem = createItemFromTileEntity(backpackTE);
        world.setBlockWithNotify(x, y, z, 0);
        player.inventory.insertItem(backpackItem, false);
    }

    private ItemStack createItemFromTileEntity(TileEntityBackpack backpackTE) {
        BackpackTier tier = backpackTE.getTier();
        ItemStack stack = new ItemStack(BackpackItems.getItemForTier(tier));

        ItemStack[] contents = new ItemStack[backpackTE.getContainerSize()];
        for (int i = 0; i < contents.length; i++) {
            contents[i] = backpackTE.getItem(i);
        }
        ItemBackpack.setBackpackContents(stack, contents);

        return stack;
    }

    @Override
    public ItemStack[] getBreakResult(World world, EnumDropCause cause, int x, int y, int z, int meta, TileEntity tileEntity) {
        if (tileEntity instanceof TileEntityBackpack) {
            return new ItemStack[]{createItemFromTileEntity((TileEntityBackpack) tileEntity)};
        }
        return new ItemStack[]{new ItemStack(BackpackItems.BACKPACK_SMALL)};
    }
}
