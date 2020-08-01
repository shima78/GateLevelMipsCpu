package simulator.wrapper.wrappers;

import simulator.network.Link;
import simulator.wrapper.Wrapper;
import simulator.wrapper.wrappers.Mux32To16;
public class Mux64x32 extends Wrapper {

    public Mux64x32(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    //input 0:31=>31:0 ,input32:63=>31:0 ,input 64 (select)
    @Override
    public void initialize() {
        int i = 0;
        Mux32To16 m0 = new Mux32To16("m0","33x16");
        Mux32To16 m1 = new Mux32To16("m1","33x16");

        for (i=0;i<16;i++){
            m1.addInput(getInput(i));
            m0.addInput(getInput(16+i));//16 32

        }
        for (i=32;i<48;i++){
            m1.addInput(getInput(i));
            m0.addInput(getInput(16+i));
        }
        m1.addInput(getInput(64));
        m0.addInput(getInput(64));
        for(i=0;i<16;i++){
            addOutput(m1.getOutput(i));
        }
        for(i=0;i<16;i++){
            addOutput(m0.getOutput(i));
        }

    }
}
