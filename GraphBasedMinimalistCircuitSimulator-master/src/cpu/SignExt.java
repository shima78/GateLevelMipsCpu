package cpu;
import simulator.control.Simulator;
import simulator.gates.combinational.Or;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class SignExt extends Wrapper {
    public SignExt(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        int i;
        Or or = new Or("or");
        or.addInput(getInput(0), Simulator.falseLogic);
        for(i=0;i<16;i++){
            addOutput(or.getOutput(0));
        }
        for(i=0;i<16;i++){
            addOutput(getInput(i));
        }
    }

}
