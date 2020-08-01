package cpu;
import simulator.control.Simulator;
import simulator.network.Link;
import simulator.wrapper.Wrapper;
import simulator.wrapper.wrappers.FullAdder;

public class FourBitAdder extends Wrapper {
    int flag;
    public FourBitAdder(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public void initialize() {
        flag= 0;
        FullAdder[] fullAdders = new FullAdder[4];
        for (int i = 0; i < 4; ++i)
            fullAdders[i] = new FullAdder("FA" + i, "3X2", getInput(3 - i), getInput(7 - i));

        fullAdders[0].addInput(Simulator.falseLogic);

        for (int i = 1; i < 4; ++i)
            fullAdders[i].addInput(fullAdders[i-1].getOutput(0));

        for (int i = 3; i >= 0; --i)
            addOutput(fullAdders[i].getOutput(1));
        }
    }
