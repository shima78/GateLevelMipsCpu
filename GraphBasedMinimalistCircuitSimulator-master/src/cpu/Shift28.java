package cpu;

import simulator.control.Simulator;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class Shift28 extends Wrapper {
    public Shift28(String label, String stream, Link... links ) {
        super(label, stream , links);
    }

    @Override
    public void initialize() {

        for (int i = 0 ;i < 26 ; i++ ){
            addOutput(getInput(i));
        }
        addOutput(Simulator.falseLogic);
        addOutput(Simulator.falseLogic);
    }
}
