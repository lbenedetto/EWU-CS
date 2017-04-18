module Test;

reg o1,o0,A,B;

operation op(o0,o1,Nd,Xd,Od,Ad,out);
decoder dc(A,B,Ad,Od,Xd,Nd);

initial
begin

#1 $display("ANDS (currently set to 0)");

o0=0;o1=0;A=0;B=0;
#2 $display("A=%b B=%b o0=%b o1=%b output=%b",A,B,o0,o1,out);

o0=0;o1=1;A=0;B=0;
#2 $display("A=%b B=%b o0=%b o1=%b output=%b",A,B,o0,o1,out);

o0=1;o1=0;A=0;B=0;
#2 $display("A=%b B=%b o0=%b o1=%b output=%b",A,B,o0,o1,out);

o0=1;o1=1;A=0;B=0;
#2 $display("A=%b B=%b o0=%b o1=%b output=%b",A,B,o0,o1,out);

#1 $display("XORS");

o0=0;o1=0;A=0;B=1;
#2 $display("A=%b B=%b o0=%b o1=%b output=%b",A,B,o0,o1,out);

o0=0;o1=1;A=0;B=1;
#2 $display("A=%b B=%b o0=%b o1=%b output=%b",A,B,o0,o1,out);

o0=1;o1=0;A=0;B=1;
#2 $display("A=%b B=%b o0=%b o1=%b output=%b",A,B,o0,o1,out);

o0=1;o1=1;A=0;B=1;
#2 $display("A=%b B=%b o0=%b o1=%b output=%b",A,B,o0,o1,out);

#1 $display("ORS");

o0=0;o1=0;A=1;B=0;
#2 $display("A=%b B=%b o0=%b o1=%b output=%b",A,B,o0,o1,out);

o0=0;o1=1;A=1;B=0;
#2 $display("A=%b B=%b o0=%b o1=%b output=%b",A,B,o0,o1,out);

o0=1;o1=0;A=1;B=0;
#2 $display("A=%b B=%b o0=%b o1=%b output=%b",A,B,o0,o1,out);

o0=1;o1=1;A=1;B=0;
#2 $display("A=%b B=%b o0=%b o1=%b output=%b",A,B,o0,o1,out);

#1 $display("NOTS");

o0=0;o1=0;A=1;B=1;
#2 $display("A=%b B=%b o0=%b o1=%b output=%b",A,B,o0,o1,out);

o0=0;o1=1;A=1;B=1;
#2 $display("A=%b B=%b o0=%b o1=%b output=%b",A,B,o0,o1,out);

o0=1;o1=0;A=1;B=1;
#2 $display("A=%b B=%b o0=%b o1=%b output=%b",A,B,o0,o1,out);

o0=1;o1=1;A=1;B=1;
#2 $display("A=%b B=%b o0=%b o1=%b output=%b",A,B,o0,o1,out);

end
endmodule

module decoder(iA,iB,A,O,X,N);
  input iA, iB;
  output A, O, X, N;
  assign A = ~iA & ~iB;
  assign O = iA & ~iB;
  assign X = ~iA & iB;
  assign N = iA & iB;
endmodule decoder

module operation(O0,O1,N,X,O,A);
  input O0, O1, N, X, O, A;
  output out;
  wire t0, t1, t2, t3;
  assign t0 = 0;
  assign t1 = O0 | O1;
  assign t2 = O0 ^ O1;
  assign t3 = ~O0;
  assign out1 = A & t0;
  assign out2 = O & t1;
  assign out3 = X & t2;
  assign out4 = N & t3;
  assign out = out1 | out2 | out3 | out4;

endmodule operation
