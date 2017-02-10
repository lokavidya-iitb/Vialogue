package tcking.github.com.giraffeplayer;

import java.util.List;

/**
 * Created by shubh on 08-02-2017.
 */

public interface PlayerDialogAdapter {
    /**
     * bind your view use simulation handler to pause and resume simulation
     */
    void bind(SimulationHandler simulationHandler);

    /**
     * executes after video time changes
     *
     * @param currentPosition: current position of video in milliseconds
     * @param isUser:          is time changed by user
     */
    void timeChanged(int currentPosition, boolean isUser);

    /**
     * if after notifying complete task you want to seek to a different position
     * if returns true moveTo will be called
     * if returns false will continue playing video
     **/
    boolean seekToDifferentPosition(int currentPosition, int mediaIndex);

    /**
     * Gets called after process complete of simulation handler is called
     * Set the next seek value
     *
     * @param playerModelList: list of playerModels so you can change player order
     * @param currentPosition: currentPosition of the video in milliseconds
     * @param mediaIndex:      index of the player model thats playing
     * @return position in milliseconds to move
     **/
    int moveTo(List<PlayerModel> playerModelList, int currentPosition, int mediaIndex);
}
