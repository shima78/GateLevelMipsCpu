package cpu;

import simulator.control.Simulator;
import simulator.gates.sequential.Clock;

import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class Shift32 extends Wrapper {
    public Shift32(String label, String stream, Link... links ) {
        super(label, stream , links);
    }

    @Override
    public void initialize() {

        for (int i = 2 ;i < 32 ; i++ ){
            addOutput(getInput(i));
        }
        addOutput(Simulator.falseLogic);
        addOutput(Simulator.falseLogic);
    }
}
