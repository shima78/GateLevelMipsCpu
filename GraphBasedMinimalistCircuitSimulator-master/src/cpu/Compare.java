package cpu;

import simulator.control.Simulator;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class Compare extends Wrapper{
    public Compare(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        AddOrSub sub = new AddOrSub("SUB2","65X32" , Simulator.trueLogic);
        for (int i =0 ;i <64 ; i++){
            sub.addInput(getInput(i));
        }
        for (int i =0 ; i <31 ; i++){
            addOutput(Simulator.falseLogic);
        }
        addOutput(sub.getOutput(0)); //if false: first number is bigger
                                            //if true: first number is smaller
    }
}
