package components;
import simulator.network.Link;
import simulator.wrapper.Wrapper;
public class Mux8To4 extends Wrapper {
    public Mux8To4(String label, String stream, Link... links) {
        super(label, stream, links);
    }
    //input 0:3=>3:0 ,input4:7=>3:0 ,input 8 (select)
    @Override
    public void initialize() {
        Mux2To1 m0 = new Mux2To1("m0","3x1");
        Mux2To1 m1 = new Mux2To1("m1","3x1");
        Mux2To1 m2 = new Mux2To1("m2","3x1");
        Mux2To1 m3 = new Mux2To1("m3","3x1");

        m3.addInput(getInput(0),getInput(4),getInput(8));
        m2.addInput(getInput(1),getInput(5),getInput(8));
        m1.addInput(getInput(2),getInput(6),getInput(8));
        m0.addInput(getInput(3),getInput(7),getInput(8));

        addOutput(m3.getOutput(0),m2.getOutput(0),m1.getOutput(0),m0.getOutput(0));
    }

}