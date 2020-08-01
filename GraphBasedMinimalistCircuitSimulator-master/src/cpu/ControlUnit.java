package cpu;

import simulator.control.Simulator;
import simulator.wrapper.Wrapper;
import  simulator.gates.combinational.And;
import simulator.gates.combinational.Or;
import simulator.gates.combinational.Not;
import simulator.network.Link;

public class ControlUnit extends Wrapper {

  public ControlUnit(String label, String stream, Link... links) {
    super(label, stream, links);
  }

  @Override
  public void initialize() {

    And a1 = new And("and1");
    And a2 = new And("and2");
    And a3 = new And("and3");
    And a4 = new And("and4");
    And a5 = new And("and5");
    And a6 = new And("and6");
    Or or1 = new Or("or1");
    Or or2 = new Or("or2");
    Not n0 = new Not("n0",getInput(5));
    Not n1 = new Not("n1",getInput(4));
    Not n2 = new Not("n2",getInput(3));
    Not n3 = new Not("n3",getInput(2));
    Not n4 = new Not("n4",getInput(1));
    Not n5 = new Not("n5",getInput(0));
    a1.addInput(n0.getOutput(0),n1.getOutput(0),n2.getOutput(0),n3.getOutput(0),
      n4.getOutput(0),n5.getOutput(0));//reg dst
    And isAddi = new And("isAddI");
    isAddi.addInput(n0.getOutput(0),n1.getOutput(0),getInput(2),
      n2.getOutput(0),n4.getOutput(0),n5.getOutput(0));
    Not NisAddI = new Not("nIsaddI",isAddi.getOutput(0));

//1111
    a2.addInput(getInput(0),n4.getOutput(0),n3.getOutput(0),
      n2.getOutput(0),getInput(4),getInput(5));//lw

    a3.addInput(getInput(0),
      n4.getOutput(0),getInput(2),
      n2.getOutput(0),
      getInput(4),getInput(5));//sw

    a4.addInput(n5.getOutput(0),n4.getOutput(0),
      n3.getOutput(0),getInput(3),
      n1.getOutput(0),n0.getOutput(0));//branch

    or1.addInput(a2.getOutput(0),a3.getOutput(0),isAddi.getOutput(0));//alu Src
    or2.addInput(a1.getOutput(0),a2.getOutput(0),isAddi.getOutput(0));

    a5.addInput(n0.getOutput(0),getInput(4));//jump
    //jr
      a6.addInput(getInput(0),n0.getOutput(0),n1.getOutput(0),n2.getOutput(0),
        n3.getOutput(0),n4.getOutput(0));//jr

    addOutput(a1.getOutput(0),or1.getOutput(0),a2.getOutput(0),
      or2.getOutput(0),a2.getOutput(0),a3.getOutput(0),a4.getOutput(0),
      a1.getOutput(0),
      a4.getOutput(0),a5.getOutput(0));




    //regDst,ALuSrc,MemToReg,RegWrite,memRead,memWrite,Branch, aluop1,aluop2,j
    //  Simulator.debugger.addTrackItem(or1,or2,a2,a3);
  }
}
