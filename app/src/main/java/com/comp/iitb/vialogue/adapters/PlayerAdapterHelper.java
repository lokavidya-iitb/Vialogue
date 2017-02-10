package com.comp.iitb.vialogue.adapters;

import java.util.List;

import tcking.github.com.giraffeplayer.PlayerDialogAdapter;
import tcking.github.com.giraffeplayer.PlayerModel;
import tcking.github.com.giraffeplayer.SimulationHandler;

/**
 * Created by shubh on 10-02-2017.
 */

public class PlayerAdapterHelper implements PlayerDialogAdapter{



    @Override
    public void bind(SimulationHandler simulationHandler) {

    }

    @Override
    public void timeChanged(int currentPosition, boolean isUser) {

    }

    @Override
    public boolean seekToDifferentPosition(int currentPosition, int mediaIndex) {
        return false;
    }

    @Override
    public int moveTo(List<PlayerModel> playerModelList, int currentPosition, int mediaIndex) {
        return 0;
    }
}
