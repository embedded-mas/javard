/* sudo chmod a+rw /dev/ttyUSB0 */

const int LIGHT_A = 9;
const int LIGHT_B = 10;
void setup() 
{
  
  Serial.begin(9600);
}

void loop() 
{
  //Serial.print("teste");
  while (Serial.available() > 0 ) {

     String str = Serial.readString();
     Serial.print(str);
  }
}

