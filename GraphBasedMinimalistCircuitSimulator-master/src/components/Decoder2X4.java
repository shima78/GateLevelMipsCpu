package components;

import simulator.gates.combinational.And;
import simulator.gates.combinational.Not;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class Decoder2X4 extends Wrapper {
    public Decoder2X4(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        Not not0 = new Not("NOT0" ) ;
        Not not1 = new Not("NOT1" ) ;

        And a0 = new And("AND0");
        And a1= new And("AND1");
        And a2 = new And("AND2");
        And a3 = new And("AND3");

        not0.addInput(getInput(0));
        not1.addInput(getInput(1));

        a0.addInput(not0.getOutput(0) , not1.getOutput(0));
        a1.addInput(getInput(0) , not1.getOutput(0));
        a2.addInput(not0.getOutput(0), getOutput(1));
        a3.addInput(getOutput(0) , getOutput(0));

        addOutput(a0.getOutput(0), a1.getOutput(0),a2.getOutput(0), a3.getOutput(0));

    }
}
