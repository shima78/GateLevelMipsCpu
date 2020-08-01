package cpu;

import simulator.gates.combinational.Nand;
import simulator.gates.combinational.Or;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class Or32 extends Wrapper {
    public Or32(String label, String stream, Link... links ) {
        super(label, stream , links);
    }

    @Override
    public void initialize() {
        Or[] ors = new Or[32];
        for (int i = 0; i < 32; i++) {
            ors[i] = new Or("NADN" + i, getInput(31 - i), getInput(63 - i));
        }
        for (int i = 31; i >= 0; i--) {
            addOutput(ors[i].getOutput(0));
        }
    }
}
