package components;

import simulator.gates.combinational.And;
import simulator.gates.combinational.Not;
import simulator.gates.combinational.Or;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class Mux4To1 extends Wrapper {
    public Mux4To1(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {

        And and0 = new And("AND0" );
        And and1 = new And("AND1" );
        And and2 = new And("AND2" );
        And and3 = new And("AND3" );

        Or or0 = new Or("OR0");

        Not not0 = new Not("NOT0");
        Not not1 = new Not("NOT1");

        /*
        * A0 and A1 are selectors they are get input index 0 , 1
        * I0 ,I1 ,I2 , I3 are input index 2 to 5
        * and one output from the or gate
        * */
        not0.addInput(getInput(0)); //not A0
        not1.addInput(getInput(0)); //not A1

        and0.addInput(getInput(2) , not0.getOutput(0), not1.getOutput(0)); //~A0 && ~A1 && I0
        and1.addInput(getInput(3) , not0.getOutput(0), getInput(1)); //~A0 && A1 && I1
        and2.addInput(getInput(4) , getInput(0), not1.getOutput(0)); //A0 && ~A1 && I2
        and0.addInput(getInput(5) , getInput(0), getInput(1)); //A0 && A1 && I3

        or0.addInput(and0.getOutput(0),and1.getOutput(0),and2.getOutput(0),and3.getOutput(0));

        addOutput(or0.getOutput(0));





    }
}
