package cpu;

import simulator.gates.combinational.And;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class LogicalUnit extends Wrapper {
    public LogicalUnit(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        int i;
        And32 and = new And32("and","64x32");
        Or32 or = new Or32("or","64x32");
        for( i =0;i<64;i++){
            and.addInput(getInput(i));
            or.addInput(getInput(i));

        }
        for (i=0;i<32;i++){
            addOutput(and.getOutput(i));//0:31
        }
        for (i=0;i<32;i++){
            addOutput(or.getOutput(i));//31:62
        }

    }

}
