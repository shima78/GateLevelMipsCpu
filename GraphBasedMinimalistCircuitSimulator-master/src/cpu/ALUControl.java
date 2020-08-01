package cpu;

import simulator.gates.combinational.And;
import simulator.gates.combinational.Not;
import simulator.gates.combinational.Or;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class ALUControl extends Wrapper {
    public ALUControl(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        // input 0 aluop1, input 1 aluop1, input(2345) =>F3210
        Or or2 = new Or("or2",getInput(2),getInput(5));
        And operation0 = new And("operation0",getInput(0),or2.getOutput(0));

        Not n1 = new Not("n1",getInput(0));
        Not n2 = new Not("n2",getInput(3));
        Or operation1 = new Or("operation1",n1.getOutput(0),n2.getOutput(0));

        And a1 = new And("a1",getInput(0),getInput(4));
        Or operation2 = new Or("operation2",getInput(1),a1.getOutput(0));//operation2

//        //is jr
//        Not n0 = new Not("n0",getInput(2));
//        And a2 = new And("a2",n0.getOutput(0),getInput(3),
//          getInput(4),getInput(5)); //if 0 its jump
//        Not n3 = new Not("n3",a2.getOutput(0)); // in mishe jump

        addOutput(operation2.getOutput(0),operation1.getOutput(0),operation0.getOutput(0));


    }
}
