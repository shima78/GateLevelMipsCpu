package cpu;

import simulator.control.Simulator;
import simulator.gates.combinational.And;
import simulator.gates.combinational.Not;
import simulator.gates.combinational.Or;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class ALU extends Wrapper {
    public ALU(String label, String stream, Link... links) {
        super(label, stream, links);
    }
    //output 32 = zero
    @Override
    public void initialize() {
        int i;
        Mux8To1[] mux8To1s = new Mux8To1[32];
        Or or = new Or("or");
        Not n = new Not("n");
        ArithmeticUnit arithmeticUnit = new ArithmeticUnit("AU","65x64");
        LogicalUnit logicalUnit = new LogicalUnit("LU","64x64");
        for( i =0;i<64;i++){
            arithmeticUnit.addInput(getInput(i));
            logicalUnit.addInput(getInput(i));
        }
        arithmeticUnit.addInput(getInput(64));



        for (i=0;i<32;i++){
            or.addInput(arithmeticUnit.getOutput((32+i)));
        }
        n.addInput(or.getOutput(0));//zero


        for(i=0;i<32;i++){
            mux8To1s[i] = new Mux8To1("m"+i ,"11x1");
            mux8To1s[i].addInput(logicalUnit.getOutput(i));//and
            mux8To1s[i].addInput(logicalUnit.getOutput(32+i));//or
            mux8To1s[i].addInput(arithmeticUnit.getOutput(i));//add
            mux8To1s[i].addInput(Simulator.falseLogic);//3
            mux8To1s[i].addInput(Simulator.falseLogic);//4
            mux8To1s[i].addInput(Simulator.falseLogic);//5
            mux8To1s[i].addInput(arithmeticUnit.getOutput(i));//sub
            mux8To1s[i].addInput(arithmeticUnit.getOutput(32+i));//7 111
            mux8To1s[i].addInput(getInput(64),getInput(65),getInput(66));

        }
        for(i=0;i<32;i++){
            addOutput(mux8To1s[i].getOutput(0));
        }
        addOutput(n.getOutput(0));

    }
}
