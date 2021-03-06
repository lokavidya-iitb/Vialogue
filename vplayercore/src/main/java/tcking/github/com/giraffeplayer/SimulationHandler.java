package tcking.github.com.giraffeplayer;

import android.app.Activity;

/**
 * Created by shubh on 09-02-2017.
 */

public interface SimulationHandler {
    /**
     * Blocks video play
     * */
    void blockPlay();
    /**
     * notifies Process Complete and resumes play if paused
     * */
    void notifyProcessComplete();

    Activity getActivity();
}
