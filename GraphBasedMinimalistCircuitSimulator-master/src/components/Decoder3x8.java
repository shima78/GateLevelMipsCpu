package components;

import simulator.control.Simulator;
import simulator.gates.combinational.And;
import simulator.gates.combinational.Not;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class Decoder3x8 extends Wrapper {
    public Decoder3x8(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        And a0 = new And("AND0"  );
        And a1 = new And("AND1"  );
        And a2 = new And("AND2"  );
        And a3 = new And("AND3"  );
        And a4 = new And("AND4"  );
        And a5 = new And("AND5"  );
        And a6 = new And("AND6"  );
        And a7 = new And("AND7"  );

        Not not0 = new Not("NOT0");
        Not not1 = new Not("NOT1");
        Not not2 = new Not("NOT2");

        a0.addInput(getInput(3),not0.getOutput(0) , not1.getOutput(0) , not2.getOutput(0)); //0
        a1.addInput(getInput(3),not2.getOutput(0), not1.getOutput(0), getInput(0)); //1
        a2.addInput(getInput(3),not2.getOutput(0),not0.getOutput(0), getInput(1)); //2
        a3.addInput(getInput(3),not2.getOutput(0), getInput(1),getInput(0)); //3
        a4.addInput(getInput(3),not0.getOutput(0), not1.getOutput(0) , getInput( 2)); //4
        a5.addInput(getInput(3),not1.getOutput(0), getInput(2) , getInput(0)); //5
        a6.addInput(getInput(3),not0.getOutput(0), getInput(2), getInput(0)); //6
        a7.addInput(getInput(3),getInput(0),getInput(1),getInput(2)); //7


        addOutput(a0.getOutput(0),a1.getOutput(0),a2.getOutput(0),a3.getOutput(0),a4.getOutput(0),a5.getOutput(0),a6.getOutput(0),a7.getOutput(0));


    }
}
