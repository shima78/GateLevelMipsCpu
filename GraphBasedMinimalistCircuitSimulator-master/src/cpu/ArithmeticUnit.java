package cpu;
import cpu.Compare;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class ArithmeticUnit extends Wrapper {
    public ArithmeticUnit(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        int i;
        AddOrSub addOrSub = new AddOrSub("addOrSub","65x32");
        //Adder adder = new Adder("add","64x32");
        //Subtractor subtractor = new Subtractor("sub","64x32");
        Compare compare = new Compare("comp","64x32");
        addOrSub.addInput(getInput(64));
        for( i =0;i<64;i++){
           // adder.addInput(getInput(i));
            //subtractor.addInput(getInput(i));
            addOrSub.addInput(getInput(i));
            compare.addInput(getInput(i));
        }
        for(i=0;i<32;i++){
            addOutput(addOrSub.getOutput(i));//0:31
        }
        /*for (i=0;i<32;i++){
            addOutput(adder.getOutput(i));//0:31
        }
        for (i=0;i<32;i++){
            addOutput(subtractor.getOutput(i));//32:63
        }*/
        for (i=0;i<32;i++){
            addOutput(compare.getOutput(i));//32:63
        }

    }

}
