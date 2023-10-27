#include <FS.h>
#include "SPIFFS.h"

#include <M5Stack.h>



#define ledPinW 21
#define ledPinR 22
uint16_t counter = 15;

int ledStateR = LOW;     // the current state of LED
int ledStateW = LOW;
int lastButtonState;    // the previous state of button
int currentButtonState; // the current state of button


void setup() {
  // setup pin 5 as a digital output pin
  M5.begin(true, false, true, false);
  M5.Power.begin();
  Serial.begin(9600);
  if (!SPIFFS.begin(true)) {
        Serial.println("SPIFFS Mount Failed");
        return;
    }
  /*M5.Lcd.setTextWrap(true, true); 
  M5.Lcd.setTextSize(2);
  M5.Lcd.setTextColor(GREEN);*/
  pinMode (ledPinW, OUTPUT);
  pinMode (ledPinR, OUTPUT);
  M5.Lcd.drawJpgFile(SPIFFS, "/logo.jpg", 0, 0);
  delay(1520);
  M5.Lcd.drawJpgFile(SPIFFS, "/menu_m5V2.jpg", 0, 0);
}

void loop() { 
  M5.update();

  /*if(M5.BtnA.isPressed() && !M5.BtnA.isPressed()) {
  Serial.println("The button is pressed");

    // toggle state of LED
  ledStateR = !ledStateR;

    // control LED arccoding to the toggled state
  digitalWrite(ledPin, ledStateR); 
  }*/

  /*if(M5.BtnB.wasPressed() && !M5.BtnB.isPressed()) {
  Serial.println("The button is pressed");

    // toggle state of LED
  ledStateW = !ledStateW;

    // control LED arccoding to the toggled state
  digitalWrite(ledPin, ledStateW); 
  }*/

  //digitalWrite (ledPin, HIGH);  // turn on the LED
  //delay(500); // wait for half a second or 500 milliseconds
  //digitalWrite (ledPin, LOW); // turn off the LED
  //delay(500); // wait for half a second or 500 milliseconds

  if (M5.BtnA.wasPressed()) {    //If the key is pressed. 
    //M5.Lcd.println("Button A is Pressed.");
    ledStateW = !ledStateW;
    digitalWrite (ledPinW, ledStateW);  // turn on the LED
    M5.Lcd.drawJpgFile(SPIFFS, "/ocupadoPress.jpg", 0, 0);
    delay(50);
    M5.Lcd.drawJpgFile(SPIFFS, "/menu_m5V2.jpg", 0, 0);
    delay(350);
  }
  if (M5.BtnB.wasPressed()) {    //If the key is pressed. 
    //M5.Lcd.println("Button B is Pressed.");
    ledStateR = !ledStateR;
    digitalWrite (ledPinR, ledStateR);  // turn on the LED
    M5.Lcd.drawJpgFile(SPIFFS, "/librePress.jpg", 0, 0);
    delay(50);
    M5.Lcd.drawJpgFile(SPIFFS, "/menu_m5V2.jpg", 0, 0);
    delay(350);
  }
  if (M5.BtnC.wasPressed()) {    //If the key is pressed. 
    //M5.Lcd.println("Button B is Pressed.");
    ledStateR = !ledStateR;
    digitalWrite (ledPinR, ledStateR);  // turn on the LED
    M5.Lcd.drawJpgFile(SPIFFS, "/reservadaPress.jpg", 0, 0);
    delay(50);
    M5.Lcd.drawJpgFile(SPIFFS, "/menu_m5V2.jpg", 0, 0);
    delay(350);
  }
  /*if (counter % 15 == 0){
    M5.Lcd.fillScreen(BLACK);
  } */

}
