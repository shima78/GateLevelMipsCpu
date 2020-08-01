package cpu;

import simulator.gates.combinational.And;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class And32 extends Wrapper {
    public And32(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        And[] ands = new And[32];
        for (int i =0 ; i<32 ; i++){
            ands[i] = new And("ADN"+i , getInput(31-i) , getInput(63 - i));
        }
        for (int i = 31 ; i >= 0 ; i-- ){
            addOutput(ands[i].getOutput(0));
        }
    }
}
