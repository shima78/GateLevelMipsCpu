package simulator.wrapper.wrappers;

import simulator.network.Link;
import simulator.wrapper.Wrapper;
import simulator.gates.combinational.And;
import simulator.gates.combinational.Or;
import simulator.gates.combinational.Not;
public class Mux2To1 extends Wrapper {
    public Mux2To1(String label, String stream, Link... links) {
        super(label, stream, links);
    }
    //input 0 =>I1,input1 =>I0, input2 =>s
    @Override
    public void initialize() {
        And a1 = new And("a1",getInput(1),getInput(2));//I1.s
        Not n1 = new Not("n1",getInput(2));//not s
        And a2 = new And("a2",getInput(0),n1.getOutput(0));//I0.s'
        Or o1 = new Or("o1",a1.getOutput(0),a2.getOutput(0));
        addOutput(o1.getOutput(0));
    }


}
