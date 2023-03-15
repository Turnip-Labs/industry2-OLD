package turniplabs.industry.entity;

import net.minecraft.src.Block;
import net.minecraft.src.Season;
import sunsetsatellite.energyapi.template.tiles.TileEntityBatteryBox;
import sunsetsatellite.energyapi.util.Connection;
import sunsetsatellite.energyapi.util.Direction;

public class TileEntitySolarGenerator extends TileEntityBatteryBox {

    public TileEntitySolarGenerator(){
        super();
        setCapacity(250);
        setTransfer(25);

        for (Direction dir : Direction.values()) setConnection(dir, Connection.OUTPUT);
    }

    @Override
    public void updateEntity() {
        if (energy < capacity && isFacingSky()) {
            int generatedEnergy = 4;

            if (worldObj.getBlockTemperature(xCoord, zCoord) > 0.85F && worldObj.getBlockHumidity(xCoord, zCoord) < 0.30F) generatedEnergy += 2;
            if (worldObj.getBlockTemperature(xCoord, zCoord) < 0.40F && worldObj.getBlockHumidity(xCoord, zCoord) < 0.30F) generatedEnergy -= 2;
            if (yCoord > 150) generatedEnergy += yCoord / 100;

            if (worldObj.getCurrentSeason() == Season.surfaceWinter || worldObj.getCurrentSeason() == Season.surfaceFall) generatedEnergy -= 2;
            generatedEnergy -= worldObj.skylightSubtracted;

            if (generatedEnergy > 0) energy = Math.min(energy + generatedEnergy, capacity);
        }

        super.updateEntity();
    }

    protected boolean isFacingSky() {
        for(int newYCoord = yCoord + 1; newYCoord < 255; newYCoord++) {
            Block block = Block.getBlock(worldObj.getBlockId(xCoord, newYCoord, zCoord));
            if (block != null && block.isOpaqueCube()) return false;
        }
        return true;
    }

}
