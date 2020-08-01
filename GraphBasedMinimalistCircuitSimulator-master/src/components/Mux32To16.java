package components;

import components.Mux8To4;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class Mux32To16 extends Wrapper {
    public Mux32To16(String label, String stream, Link... links) {
        super(label, stream, links);
    }
    //input 0:15=>15:0 ,input16:31=>15:0 ,input 32 (select)
    @Override
    public void initialize() {
        int i;
        Mux8To4 m0 = new Mux8To4("m0","9x4");
        Mux8To4 m1 = new Mux8To4("m1","9x4");
        Mux8To4 m2 = new Mux8To4("m2","9x4");
        Mux8To4 m3 = new Mux8To4("m3","9x4");
        for(i=0;i<4;i++) {
            m3.addInput(getInput(i));
            m2.addInput(getInput((4+i)));
            m1.addInput(getInput((8+i)));
            m0.addInput(getInput((12+i)));
        }

        for(i=16;i<20;i++) {
            m3.addInput(getInput(i));
            m2.addInput(getInput((4+i)));
            m1.addInput(getInput((8+i)));
            m0.addInput(getInput((12+i)));

        }

        m0.addInput(getInput(32));
        m1.addInput(getInput(32));
        m2.addInput(getInput(32));
        m3.addInput(getInput(32));

        addOutput(m3.getOutput(0),m3.getOutput(1),m3.getOutput(2),m3.getOutput(3),
                m2.getOutput(0),m2.getOutput(1),m2.getOutput(2),m2.getOutput(3),
                m1.getOutput(0),m1.getOutput(1),m1.getOutput(2),m1.getOutput(3),
                m0.getOutput(0),m0.getOutput(1),m0.getOutput(2),m0.getOutput(3));

    }
}