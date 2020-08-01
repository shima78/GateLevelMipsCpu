package simulator;

import components.Mux64X32;
import cpu.*;
import simulator.control.Simulator;
import simulator.gates.combinational.And;
import simulator.gates.combinational.ByteMemory;
import simulator.gates.combinational.Not;
import simulator.gates.sequential.BigClock;
import simulator.gates.sequential.Clock;
import simulator.wrapper.wrappers.*;

public class Sample {


  public static void main(String[] args) {
    int i =0;
    int j=0;
    // ByteMemory D_mem = new ByteMemory("MEMORY");
    BigClock ck = new BigClock("ck");
    Adder adder0 = new Adder("adder0","64x33");

    //        == pc register ==
    Register programCounter = new Register("pc","34x32");

    And initPC = new And("initPC");
    for(i=0;i<32;i++){
      initPC.addInput(programCounter.getOutput(i));
    }

    Mux64x32 initPcMux = new Mux64x32("initialPc","65x32");
    //if pc!= -1 => +4
    for (i=0;i<29;i++){
      initPcMux.addInput(Simulator.falseLogic);
    }
    initPcMux.addInput(Simulator.trueLogic);
    initPcMux.addInput(Simulator.falseLogic);
    initPcMux.addInput(Simulator.falseLogic);
    //if pc == -1 =>+1
    for (i=0;i<31;i++){
      initPcMux.addInput(Simulator.falseLogic);
    }
    initPcMux.addInput(Simulator.trueLogic);
    //control
    initPcMux.addInput(initPC.getOutput(0));

    //Pc+4
    for(i=0;i<32;i++){
      adder0.addInput(programCounter.getOutput(i));
    }
    for(i=0;i<32;i++){
      adder0.addInput(initPcMux.getOutput(i));
    }

//        I Mem
    ByteMemory I_mem = new ByteMemory("MEMORY");
    Boolean[][] temp = new Boolean[65536][8];
//        initial instruction memory
    for (i = 0; i < 65536; ++i) {
      for ( j = 0; j < 8; ++j) {
        temp[i][j] = false;
      }
    }
    temp[0][0] = true;
    temp[0][4] = true;
    temp[0][5] = true;//10 00 11 00
    temp[1][5] = true;
    temp[1][7] = true;//10 10
    //lw $5 ,1($0)
    temp[4][0] = true;
    temp[4][4] = true;
    temp[4][5] = true;//10 00 11
    temp[5][6] = true;
    temp[5][7] = true;
    temp[7][5] = true;
    //lw $3 , 5($0)
    temp[8][0] = true;
    temp[8][4] = true;
    temp[8][5] = true;//10 00 11
    temp[9][5] = true;
    temp[11][4] = true;
    //lw $4 ,9(&0)
    temp[13][0] = true;
    temp[13][2] = true;//$5
    temp[13][5] = true;//$4
    temp[14][2] = true;
    temp[14][3] = true;//$6
    temp[15][2] = true;//add
    //add $6 ,$5 ,$4
    temp[16][0] = true ;
    temp[16][2] = true ;
    temp[16][4] = true ;
    temp[16][5] = true ;//opcode
    temp[17][5] = true;
    temp[17][6] = true;//$6
    temp[19][4] = true;
    temp[19][5] = true;//12 in 12($0)
    //sw $6 , 12($0)
    temp[21][0] = true;
    temp[21][2] = true;//$5
    temp[21][5] = true;//$4
    temp[22][2] = true;
    temp[22][3] = true;//$6
    temp[23][2] = true;
    temp[23][6] = true;//sub
    //sub $6 ,$5 ,$4
    temp[24][2] = true;
    temp[25][7] = true;
    temp[27][5] = true;//addi
    I_mem.setMemory(temp);

    //Data memory
    Boolean[][] tempD = new Boolean[65536][8];
    ByteMemory D_mem = new ByteMemory("d_mem");
//        initial instruction memory
    for (i = 0; i < 65536; ++i) {
      for ( j = 0; j < 8; ++j) {
        tempD[i][j] = false;
      }
    }
    tempD[3][3] = true;
    tempD[3][4] = true;
    tempD[3][5] = true;
    tempD[3][7] = true;// = 29 == 0 1 1 1 0 1
    tempD[7][2] = true;
    tempD[7][4] = true;

    tempD[7][7] = true;// = 41 == 1 0 1 0 0 1
    tempD[11][4] = true;
    tempD[11][5] = true;// = 12 == 0 0 1 1 0 0

    D_mem.setMemory(tempD);


    //write
    I_mem.addInput(Simulator.falseLogic);

    //address
    for (j=0;j<16;j++) {
      I_mem.addInput(programCounter.getOutput(j+16));//
    }

    //input
    for ( i = 0; i < 16; ++i) {
      I_mem.addInput(Simulator.falseLogic);
    }
    for ( i = 16; i < 32; ++i) {
      I_mem.addInput(Simulator.trueLogic);
    }


//        Control Unit
    ControlUnit cu = new ControlUnit("Cu" , "6x10");
    //0:5 control unit
    for (i = 0 ;i<6;i++){
      cu.addInput(I_mem.getOutput(i));
    }
    And isBranch = new And("AND");
    //SignExt
    SignExt signExt = new SignExt("signExt","16x32");
    for(i=0;i<16;i++){
      signExt.addInput(I_mem.getOutput(16+i));
    }

    //Shift 2 bit
    Shift32 shift32 = new Shift32("shift","32x32");
    for(i=0;i<32;i++){
      shift32.addInput(signExt.getOutput(i));
    }

    //adder alu res
    Adder adder1 = new Adder("adder1","64x33");
    for (i=0;i<32;i++){
      adder1.addInput(adder0.getOutput(i+1));
    }
    for(i=0;i<32;i++) {
      adder1.addInput(shift32.getOutput(i));
    }
//
    //  Mux pc src
    Mux64x32 pc_multiplexers = new Mux64x32("mux64x32","65x32");
    for(i=0;i<32;i++){
      pc_multiplexers.addInput(adder0.getOutput(i+1));//0
    }
    for(i=0;i<32;i++){
      pc_multiplexers.addInput(adder1.getOutput(i+1));//1
    }
    pc_multiplexers.addInput(cu.getOutput(6));//select


//        Mux jump
//        shift 26 to 28
    Shift28 shift28 = new Shift28("shift28" , "26x28");
    for (i=6;i<32;i++){
      shift28.addInput(I_mem.getOutput(i));
    }
    //mux jump o output adder1
    Mux64X32 pcSrcMux = new Mux64X32("MUX64x32" , "65x32");

    //jump mux
    // 32 bit from mux pc
    for (i=0;i<32;i++){
      pcSrcMux.addInput(pc_multiplexers.getOutput(i));//0
    }
//        first 31 : 28 bit
    pcSrcMux.addInput(adder0.getOutput(31),
      adder0.getOutput(30),
      adder0.getOutput(29),
      adder0.getOutput(28));
//        now 28 bit from shift
    for (i=0;i<28;i++){
      pcSrcMux.addInput(shift28.getOutput(i));//1
    }

    pcSrcMux.addInput(cu.getOutput(9));//select jump ezafe beshe
    //Update pc
    for(i=0;i<32;i++){
      programCounter.addInput(pcSrcMux.getOutput(i));
    }
    programCounter.addInput(Simulator.trueLogic,ck.getOutput(0));

//////////////end pc
    //        Creating RegisterFile
    RegisterFile registerFile = new RegisterFile("RegisterFile" , "49X64");

//        6:10 Read 1
    for ( i=6;i<11;i++){
      registerFile.addInput(I_mem.getOutput(i));
    }
////        11:15 Read 2
    for (i=11 ;i<16;i++){
      registerFile.addInput(I_mem.getOutput(i));
    }

    Mux2To1 mux2To1_zero = new Mux2To1("Mux2x1_0" , "3x1");
    Mux2To1 mux2To1_one = new Mux2To1("Mux2x1_1" , "3x1");
    Mux2To1 mux2To1_two = new Mux2To1("Mux2x1_2" , "3x1");
    Mux2To1 mux2To1_three = new Mux2To1("Mux2x1_3" , "3x1");
    Mux2To1 mux2To1_four = new Mux2To1("Mux2x1_4" , "3x1");

//        input read 2 inout write and selector
    mux2To1_zero.addInput(I_mem.getOutput(11) , I_mem.getOutput(16), cu.getOutput(0));
    mux2To1_one.addInput(I_mem.getOutput(12) , I_mem.getOutput(17), cu.getOutput(0));
    mux2To1_two.addInput(I_mem.getOutput(13) , I_mem.getOutput(18), cu.getOutput(0));
    mux2To1_three.addInput(I_mem.getOutput(14) , I_mem.getOutput(19), cu.getOutput(0));
    mux2To1_four.addInput(I_mem.getOutput(15) , I_mem.getOutput(20), cu.getOutput(0));

//        output mux in write
    registerFile.addInput(mux2To1_zero.getOutput(0));
    registerFile.addInput(mux2To1_one.getOutput(0));
    registerFile.addInput(mux2To1_two.getOutput(0));
    registerFile.addInput(mux2To1_three.getOutput(0));
    registerFile.addInput(mux2To1_four.getOutput(0));
    Mux64X32 regFileWriteDataMux = new Mux64X32("MUX64x32" , "65x32");

    //   write data coming from data memory
    for(i=0;i<32;i++){
      registerFile.addInput(regFileWriteDataMux.getOutput(i));
    }
    Not not = new Not("NOT", ck.getOutput(0)) ;
    registerFile.addInput(cu.getOutput(3),not.getOutput(0));

    //  ALU control
    //    6 inputs and 3 outputs
    ALUControl aluControl = new ALUControl("ALUControl" , "6x3");
    //    inout 0 is aluop0 and input 1 is aluop1 the rest are from i-mem
    aluControl.addInput(cu.getOutput(7)); //0
    aluControl.addInput(cu.getOutput(8)); //1
//        i-mem 0 to 3
    aluControl.addInput(I_mem.getOutput(28));
    aluControl.addInput(I_mem.getOutput(29));
    aluControl.addInput(I_mem.getOutput(30));
    aluControl.addInput(I_mem.getOutput(31));
//
////jr
//    Mux64x32 jrMux = new Mux64x32("jr","65x32");
//    for(i=0;i<32;i++){
//      jrMux.addInput(pcSrcMux.getOutput(i));//0
//    }
//    for(i=0;i<32;i++){
//      jrMux.addInput(registerFile.getOutput(i));//
//    }
//    And isJr = new And("isJr",cu.getOutput(10),aluControl.getOutput(3));
//    jrMux.addInput(isJr.getOutput(0));//cu 10$ aluontol 3



    //      ALU
    ALU alu= new  ALU("alu","67x33");
    //        read 2  || signExtend
    Mux64X32 aluSrcMux = new Mux64X32("MUX64x32" , "65x32");
    for (i=0;i<32;i++){
      aluSrcMux.addInput(registerFile.getOutput(32+i)); //32 ta 64 from regFile/0
    }
    for (i=0;i<32;i++){
      aluSrcMux.addInput(signExt.getOutput(i));//1
    }
//     select ALUSrc
    aluSrcMux.addInput(cu.getOutput(1));//aluSrc

//        32 input read data 1 , 32 bit read data 2 both from regFile , 3 bits from aluControl
//        read data 1
    for (i=0;i<32;i++){
      alu.addInput(registerFile.getOutput(i));//0 :31 regfile
    }
    //input 2
    for (i=0;i<32;i++){
      alu.addInput(aluSrcMux.getOutput(i));
    }

    //alu control
    alu.addInput(aluControl.getOutput(0),aluControl.getOutput(1),aluControl.getOutput(2));
//    //isBranch
    isBranch.addInput(cu.getOutput(2),alu.getOutput(32));
//end Alu




    //write
    D_mem.addInput(cu.getOutput(5));//cu 5
    //address
    for (j=0;j<16;j++) {
      D_mem.addInput(alu.getOutput(j+16));//address
    }
    //  data is from read 2
    for(i=0 ;i<32;i++){
      D_mem.addInput(registerFile.getOutput(i+32));
    }
    //end D-mem

    //Write Back
    //  mux 64x32 MemToReg is selector
    //  first 32 bits read data from D_mem , second alu es
    //  data from D_mem
    for (i=0 ;i<32;i++){
      regFileWriteDataMux.addInput(alu.getOutput(i));//0
    }
//        Alu result
    for (i=0;i<32;i++){
      regFileWriteDataMux.addInput(D_mem.getOutput(i));//1
    }

//        selector is MemToReg from Cu
    regFileWriteDataMux.addInput(cu.getOutput(2));


    Simulator.debugger.addTrackItem(ck,aluControl,cu,programCounter,I_mem,signExt,shift28,shift32,alu,registerFile,D_mem);
    Simulator.debugger.setDelay(0);
    Simulator.circuit.startCircuit(10);
  }
}
