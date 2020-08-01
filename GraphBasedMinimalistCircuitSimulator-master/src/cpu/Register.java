package cpu;


import simulator.wrapper.wrappers.DFlipFlop;
import simulator.network.Link;
import simulator.wrapper.Wrapper;
import simulator.wrapper.wrappers.Mux2To1;

public class Register extends Wrapper {

   // private Mux64X32 mux64X32;
    public Register(String label, String stream, Link... links)
    {
        super(label, stream, links);
    }
    @Override
    public void initialize() {
      DFlipFlop[] register = new DFlipFlop[32];
      Mux2To1[] mux2To1s = new Mux2To1[32];
      int i;
        for(i=0;i<32;i++){
            mux2To1s[i] = new Mux2To1("m"+i,"3x1");
            register[i] = new DFlipFlop("ff"+i,"2x2",getInput(33));
        }

        for ( i = 0 ;i<32 ;i++){
          mux2To1s[i].addInput(register[i].getOutput(0),getInput(i),getInput(32));
          register[i].addInput(mux2To1s[i].getOutput(0));
        }
        for (i=0;i<32;i++){
          addOutput(register[i].getOutput(0));
        }
    }
}
