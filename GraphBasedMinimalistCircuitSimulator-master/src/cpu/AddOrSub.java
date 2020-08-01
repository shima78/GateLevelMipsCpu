package cpu;

import simulator.gates.combinational.Xor;
import simulator.network.Link;
import simulator.wrapper.Wrapper;
import simulator.wrapper.wrappers.FullAdder;

public class AddOrSub extends Wrapper {
    public AddOrSub(String label, String stream, Link... links ) {
        super(label, stream , links);
    }

    @Override
    public void initialize() {
        Xor[] xors = new Xor[32];
        for (int i =0 ;i <32 ;i++){
            xors[i] = new Xor("XOR" + i ,getInput(0) ,getInput(33 + i));
        }
        FullAdder[] fullAdders = new FullAdder[32];
        for (int i = 0; i < 32; ++i)
            fullAdders[i] = new FullAdder("FA" + i, "3X2", getInput(32 - i), xors[31 - i].getOutput(0));

        fullAdders[0].addInput(getInput(0));

        for (int i = 1; i < 32; ++i)
            fullAdders[i].addInput(fullAdders[i-1].getOutput(0));

        //addOutput(fullAdders[31].getOutput(0));
        for (int i = 31; i >= 0; --i)
            addOutput(fullAdders[i].getOutput(1));

    }
}
