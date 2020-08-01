package cpu;
import simulator.network.Link;
import simulator.wrapper.Wrapper;
import simulator.wrapper.wrappers.Mux2To1;
public class Mux10To5 extends Wrapper{
    public Mux10To5(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    //input[0:4] =>Instruction25:21 ,Input[5:9] instruction20:16 ,Input[10] = Select
    public void initialize() {
        Mux2To1 m0 = new Mux2To1("m0","3x1");
        Mux2To1 m1 = new Mux2To1("m1","3x1");
        Mux2To1 m2 = new Mux2To1("m2","3x1");
        Mux2To1 m3 = new Mux2To1("m3","3x1");
        Mux2To1 m4 = new Mux2To1("m4","3x1");

        m4.addInput(getInput(0),getInput(5),getInput(10));//MSB
        m3.addInput(getInput(1),getInput(6),getInput(10));
        m2.addInput(getInput(2),getInput(7),getInput(10));
        m1.addInput(getInput(3),getInput(8),getInput(10));
        m0.addInput(getInput(4),getInput(9),getInput(10));//LSB
        addOutput(m4.getOutput(0),m3.getOutput(0),m2.getOutput(0),m1.getOutput(0),m0.getOutput(0));
    }

}
