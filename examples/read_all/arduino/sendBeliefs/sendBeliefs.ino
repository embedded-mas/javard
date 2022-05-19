#include<Embedded_Protocol_2.h> // biblioteca para envio de crenças

Communication com;


int belief_1 = 1;
int belief_2 = 1;


void setup() {
  Serial.begin(9600);
  delay(5000);
}

void loop() {
  
     com.startBelief("count1");
     com.beliefAdd(belief_1++);
     com.endBelief();
     com.startBelief("count2");
     com.beliefAdd(belief_2--);
     com.endBelief();
     com.sendMessage();     
     delay(500);
      
}
